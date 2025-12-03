package com.pjatk.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.pjatk.boot.HexaTicketApplication;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(
        classes = HexaTicketApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureHttpGraphQlTester
class FullIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(
            DockerImageName.parse("mongo:latest")
    );

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        // Fake mailer config
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> "3025");
        registry.add("spring.mail.username", () -> "test");
        registry.add("spring.mail.password", () -> "test");
    }

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void shouldCompleteFullUserJourney() {
        // 1. CREATE EVENT
        // Tworzymy Event z 10 miejscami
        String createEventMutation = """
            mutation {
                createEvent(input: {
                    name: "Test Integration Event",
                    description: "E2E Test",
                    startDate: "2026-10-10T20:00:00",
                    venue: "Java Hall",
                    totalSeats: 10,
                    price: 100.00,
                    category: "TEST"
                }) { id }
            }
        """;

        String eventId = graphQlTester.document(createEventMutation)
                .execute()
                .path("createEvent.id").entity(String.class).get();

        assertThat(eventId).isNotNull();

        // 2. BOOK TICKET (Używamy nazwy 'bookEvent' ze schematu!)
        // Nie pytamy o 'status', bo usunąłeś go z typu Ticket w schemacie
        String bookMutation = String.format("""
            mutation {
                bookEvent(input: {
                    eventId: "%s",
                    ownerName: "Jan Testowy",
                    ownerEmail: "jan@test.pl"
                }) {
                    id
                    ticketCode
                    # status - USUNIĘTO (brak w schemacie)
                }
            }
        """, eventId);

        String ticketId = graphQlTester.document(bookMutation)
                .execute()
                .path("bookEvent.id").entity(String.class).get(); // bookEvent!

        // 3. VERIFY EVENT SEATS (Dekrementacja)
        // Sprawdzamy czy AvailableSeats spadło (10 -> 9)
        String verifyEventQuery = """
            query { 
                events(category: "TEST") { 
                    id 
                    availableSeats 
                } 
            }
        """;

        graphQlTester.document(verifyEventQuery)
                .execute()
                .path("events[0].availableSeats").entity(Integer.class).isEqualTo(9);

        // 4. MY TICKETS (Weryfikacja Query Mieszanego)
        // Sprawdzamy, czy adapter łączy dane z Eventem
        String myTicketsQuery = """
            query {
                myTickets(email: "jan@test.pl") {
                    ownerName
                    ownerEmail
                    event {
                        name
                        venue
                    }
                }
            }
        """;

        graphQlTester.document(myTicketsQuery)
                .execute()
                .path("myTickets[0].ownerEmail").entity(String.class).isEqualTo("jan@test.pl")
                .path("myTickets[0].event.name").entity(String.class).isEqualTo("Test Integration Event");

        // 5. CANCEL TICKET
        String cancelMutation = String.format("""
            mutation {
                cancelTicket(ticketId: "%s")
            }
        """, ticketId);

        graphQlTester.document(cancelMutation)
                .execute()
                .path("cancelTicket").entity(String.class).isEqualTo(ticketId);

        // 6. FINAL VERIFY (Inkrementacja)
        // Sprawdzamy czy miejsce wróciło (9 -> 10) po anulowaniu
        graphQlTester.document(verifyEventQuery)
                .execute()
                .path("events[0].availableSeats").entity(Integer.class).isEqualTo(10);
    }
}
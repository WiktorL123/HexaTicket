package com.pjatk.test;

import com.pjatk.boot.HexaTicketApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean; // <--- KLUCZOWY IMPORT
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        // Mail properties mogą być dowolne, bo i tak mockujemy beana
    }

    @Autowired
    private GraphQlTester graphQlTester;

    // --- MOCKOWANIE MAILA ---
    // Spring zastąpi prawdziwą implementację (Brevo) tym mockiem w kontekście testu.
    // Metoda send() nie rzuci błędu Connection Refused, bo nic nie zrobi.
    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void shouldCompleteFullUserJourney() {
        // 1. CREATE EVENT
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
        String bookMutation = String.format("""
            mutation {
                bookEvent(input: {
                    eventId: "%s",
                    ownerName: "Jan Testowy",
                    ownerEmail: "jan@test.pl"
                }) {
                    id
                    ticketCode
                    # Usunięto pole 'status', bo nie masz go w type Ticket
                }
            }
        """, eventId);

        String ticketId = graphQlTester.document(bookMutation)
                .execute()
                .path("bookEvent.id").entity(String.class).get();

        // WERYFIKACJA MAILA (Czy serwis spróbował wysłać?)
        // Oczekujemy 1 wywołania po rezerwacji
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

        // 3. VERIFY EVENT SEATS (Dekrementacja)
        String verifyEventQuery = """
            query { 
                events(category: "TEST") { 
                    availableSeats 
                } 
            }
        """;

        graphQlTester.document(verifyEventQuery)
                .execute()
                .path("events[0].availableSeats").entity(Integer.class).isEqualTo(9);

        // 4. MY TICKETS (Weryfikacja Agregacji)
        String myTicketsQuery = """
            query {
                myTickets(email: "jan@test.pl") {
                    ownerName
                    ownerEmail
                    event {
                        name
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

        // WERYFIKACJA MAILA (Czy serwis wysłał drugi mail o anulowaniu?)
        // Teraz powinno być 2 wywołania łącznie (1 book + 1 cancel)
        verify(javaMailSender, times(2)).send(any(SimpleMailMessage.class));

        // 6. FINAL VERIFY (Inkrementacja - zwrot miejsca)
        graphQlTester.document(verifyEventQuery)
                .execute()
                .path("events[0].availableSeats").entity(Integer.class).isEqualTo(10);
    }
}
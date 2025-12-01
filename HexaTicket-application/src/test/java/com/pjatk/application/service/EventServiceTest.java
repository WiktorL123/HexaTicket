package com.pjatk.application.service;

import com.pjatk.core.domain.event.Event;
import com.pjatk.core.domain.event.EventStatus;
import com.pjatk.core.exception.NotFoundException;
import com.pjatk.core.exception.TooEarlyDateException;
import com.pjatk.core.exception.TooMuchSeatsException;
import com.pjatk.core.port.out.EventsRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventsRepositoryPort eventsRepositoryPort;

    @InjectMocks
    private EventService service;
    private final String NON_EXISTING_ID = "non_existing_id";
    private final String EXISING_ID_FIRST="exiting_id_first";
    private final String EXISING_ID_SECOND="exiting_id_second";
    private List<Event> existingEvents;
    private Event exisingEventOne = new Event(
            EXISING_ID_FIRST,
            "test_one",
            "description_one",
            "venue_one",
            LocalDateTime.now().plusDays(1),
            100,
            50,
            BigDecimal.valueOf(100),
            "CATEGORY_ONE",
            EventStatus.ACTIVE);
    private Event existingEventTwo = new Event(
            EXISING_ID_SECOND,
            "test_two",
            "description_two",
            "venue_two",
            LocalDateTime.now().plusDays(2),
            150,
            100,
            BigDecimal.valueOf(150),
            "CATEGORY_TWO",
            EventStatus.ACTIVE
    );

    private Event createEmptyPatch() {

        return new Event();
    }
    @BeforeEach
    void setUp(){
            existingEvents = List.of(exisingEventOne,existingEventTwo);
    }

    @Test
    void shouldFindAllExisingEvents(){
        int page = 0;
        int size = 10;
        String category = null;
        LocalDateTime startDate = null;

        when(eventsRepositoryPort.findAll(
                anyInt(),
                anyInt(),
                any(),
                any()
        )).thenReturn(existingEvents);

        ;
        List<Event> result = service.findAll(page, size, category, startDate);

        assertEquals(2, result.size());
        assertEquals(existingEvents.get(0).getName(), result.get(0).getName(), "nazwy się nie zgadzają");
    }
    @Test
    void shouldReturnEmptyListWhenCannotFindAll(){
        int page = 0;
        int size = 10;
        String category = null;
        LocalDateTime startDate = null;
        when(eventsRepositoryPort.findAll(anyInt(), anyInt(), any(), any())).thenReturn(new ArrayList<>());

        List<Event> emptyResult = service.findAll(page, size, category, startDate );

        assertEquals(0, emptyResult.size());
    }
    @Test
    void shouldFindEventById(){
        when(eventsRepositoryPort.getById(EXISING_ID_FIRST)).thenReturn(Optional.of(exisingEventOne));

        Event result = service.getById(EXISING_ID_FIRST);

        assertEquals(exisingEventOne, result, "wyniki się nie zgadzają");
    }

    @Test
    void shouldReturn404EventNotFoundWhenNotFound(){
        when(eventsRepositoryPort.getById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            service.getById(NON_EXISTING_ID);
        }, "powinien rzucic NotFoundException");
    }

    @Test
    void shouldCreateNewEventAndReturnIt(){
        LocalDateTime futureDate = LocalDateTime.now().plusDays(3); // Data bezpieczna (ponad 24h)

        Event newEvent = new Event();
        newEvent.setName("Test Koncert");
        newEvent.setStartDate(futureDate);
        newEvent.setTotalSeats(500);
        newEvent.setPrice(BigDecimal.valueOf(150));

        Event expectedEventFromDb = new Event(
                "GENERATED_ID",
                "Test Koncert",
                null,
                null,
                futureDate,
                500,
                500,
                BigDecimal.valueOf(150),
                null,
                EventStatus.ACTIVE
        );
        when(eventsRepositoryPort.save(any(Event.class))).thenReturn(expectedEventFromDb);

        Event result = service.create(newEvent);

        verify(eventsRepositoryPort, times(1)).save(any(Event.class));

        assertEquals(result.getEventStatus(), expectedEventFromDb.getEventStatus(), "status musi sie zgadzać");
        assertEquals(result.getTotalSeats(), expectedEventFromDb.getTotalSeats(), "calkowita ilosc miejsc musi sie zgadzać");


    }

    @Test
    void shouldThrowTooEarlyDateExceptionWhenEventIsTooClose(){
        LocalDateTime tooEarlyDate = LocalDateTime.now().plusDays(1);

        Event newEvent = new Event();

        newEvent.setStartDate(tooEarlyDate);
        newEvent.setTotalSeats(100);

        assertThrows(TooEarlyDateException.class, () -> {
            service.create(newEvent);
        }, "powinien rzucic TooEarlyDateException jesli data jest za wczesna");
        verify(eventsRepositoryPort, never()).save(any(Event.class));
    }


    @Test
    void shouldSuccessfullyDeleteExistingEvent() {
        String eventIdToDelete = existingEvents.get(0).getId();


        when(eventsRepositoryPort.getById(eventIdToDelete)).thenReturn(Optional.of(existingEvents.get(0)));


        service.delete(eventIdToDelete);

        verify(eventsRepositoryPort, times(1)).delete(eventIdToDelete);

        verify(eventsRepositoryPort, times(1)).getById(eventIdToDelete);
    }


    @Test
    void shouldThrowNotFoundExceptionWhenDeletingNonExistingEvent() {
        String nonExistingId = "non-existing-id";

        when(eventsRepositoryPort.getById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            service.delete(nonExistingId);
        }, "Powinien rzucić NotFoundException, ponieważ Event nie istnieje.");

        verify(eventsRepositoryPort, never()).delete(anyString());
    }


    @Test
    void shouldUpdateOnlyNameAndDescription() {
        String targetId = EXISING_ID_FIRST;
        Event targetEvent = exisingEventOne;

        Event updatePatch = createEmptyPatch();
        updatePatch.setName("Nowa Nazwa");
        updatePatch.setDescription("Nowy Opis");

        when(eventsRepositoryPort.getById(targetId)).thenReturn(Optional.of(targetEvent));
        when(eventsRepositoryPort.save(any(Event.class))).thenReturn(targetEvent);

        // Act
        Event updatedEvent = service.updatePartially(targetId, updatePatch);

        // Assert
        assertEquals("Nowa Nazwa", updatedEvent.getName(), "Nazwa powinna zostać zaktualizowana.");
        assertEquals("Nowy Opis", updatedEvent.getDescription(), "Opis powinien zostać zaktualizowany.");
        assertEquals(100, updatedEvent.getTotalSeats(), "TotalSeats nie powinno się zmienić.");
        assertEquals(50, updatedEvent.getAvailableSeats(), "AvailableSeats nie powinno się zmienić.");
        verify(eventsRepositoryPort, times(1)).save(any(Event.class));
    }

    @Test
    void shouldIncreaseTotalSeatsAndAdjustAvailableSeats() {
        // Arrange
        String targetId = EXISING_ID_FIRST;
        Event targetEvent = exisingEventOne; // totalSeats=100, availableSeats=50 -> sprzedano 50

        Event updatePatch = createEmptyPatch();
        updatePatch.setTotalSeats(200);

        when(eventsRepositoryPort.getById(targetId)).thenReturn(Optional.of(targetEvent));
        when(eventsRepositoryPort.save(any(Event.class))).thenReturn(targetEvent);

        Event updatedEvent = service.updatePartially(targetId, updatePatch);

        assertEquals(200, updatedEvent.getTotalSeats(), "TotalSeats powinno być 200.");
        assertEquals(150, updatedEvent.getAvailableSeats(), "AvailableSeats powinno wzrosnąć do 150.");
        verify(eventsRepositoryPort, times(1)).save(any(Event.class));
    }

    @Test
    void shouldAllowManualDecreaseOfAvailableSeatsWithinLimit() {
        // Arrange
        String targetId = EXISING_ID_FIRST;
        Event targetEvent = exisingEventOne; // totalSeats=100, availableSeats=50

        Event updatePatch = createEmptyPatch();
        updatePatch.setAvailableSeats(40);

        when(eventsRepositoryPort.getById(targetId)).thenReturn(Optional.of(targetEvent));
        when(eventsRepositoryPort.save(any(Event.class))).thenReturn(targetEvent);

        Event updatedEvent = service.updatePartially(targetId, updatePatch);

        assertEquals(40, updatedEvent.getAvailableSeats(), "AvailableSeats powinno być ustawione ręcznie na 40.");
        verify(eventsRepositoryPort, times(1)).save(any(Event.class));
    }


    @Test
    void shouldThrowNotFoundExceptionIfEventDoesNotExist() {
        String nonExistingId = NON_EXISTING_ID;
        when(eventsRepositoryPort.getById(nonExistingId)).thenReturn(Optional.empty()); // Brak obiektu

        assertThrows(NotFoundException.class, () -> {
            service.updatePartially(nonExistingId, createEmptyPatch());
        }, "Powinien rzucić NotFoundException.");
        verify(eventsRepositoryPort, never()).save(any(Event.class));
    }

    @Test
    void shouldThrowTooMuchSeatsExceptionWhenDecreasingTotalSeatsBelowReserved() {
        String targetId = EXISING_ID_FIRST;
        Event targetEvent = exisingEventOne;

        Event updatePatch = createEmptyPatch();
        updatePatch.setTotalSeats(40);

        when(eventsRepositoryPort.getById(targetId)).thenReturn(Optional.of(targetEvent));

        assertThrows(TooMuchSeatsException.class, () -> {
            service.updatePartially(targetId, updatePatch);
        }, "Powinien rzucić TooMuchSeatsException, gdy TotalSeats < sprzedanych (40 < 50).");
        verify(eventsRepositoryPort, never()).save(any(Event.class));
    }

    @Test
    void shouldThrowTooMuchSeatsExceptionWhenAvailableSeatsExceedTotal() {
        String targetId = EXISING_ID_FIRST;
        Event targetEvent = exisingEventOne;

        Event updatePatch = createEmptyPatch();
        updatePatch.setAvailableSeats(101);

        when(eventsRepositoryPort.getById(targetId)).thenReturn(Optional.of(targetEvent));

        assertThrows(TooMuchSeatsException.class, () -> {
            service.updatePartially(targetId, updatePatch);
        }, "Powinien rzucić TooMuchSeatsException, gdy AvailableSeats > TotalSeats.");
        verify(eventsRepositoryPort, never()).save(any(Event.class));
    }
}

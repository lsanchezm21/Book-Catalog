package com.example.catalog;

import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;
import com.example.catalog.service.BookNotFoundException;
import com.example.catalog.service.BookService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private final BookService service = new BookService();
    private final BookRequestDTO request = new BookRequestDTO("978-1", "DDD", "Evans", 2003);

    @Test
    void startsEmpty() {
        assertTrue(service.list().isEmpty());
    }

    @Test
    void createAssignsDifferentIdsAndStoresBooks() {
        BookResponseDTO first = service.create(request);
        BookResponseDTO second = service.create(request);

        assertNotEquals(first.id(), second.id());
        assertEquals(2, service.list().size());
        assertEquals("DDD", service.get(first.id()).title());
    }

    @Test
    void updateKeepsIdAndChangesData() {
        BookResponseDTO created = service.create(request);

        BookResponseDTO updated = service.update(created.id(),
                new BookRequestDTO("978-1", "Updated", "Evans", 2004));

        assertEquals(created.id(), updated.id());
        assertEquals("Updated", service.get(created.id()).title());
        assertEquals(2004, service.get(created.id()).year());
    }

    @Test
    void deleteRemovesTheBook() {
        BookResponseDTO created = service.create(request);

        service.delete(created.id());

        assertTrue(service.list().isEmpty());
    }

    @Test
    void getThrowsWhenBookDoesNotExist() {
        assertThrows(BookNotFoundException.class, () -> service.get(99L));
    }

    @Test
    void updateThrowsWhenBookDoesNotExist() {
        assertThrows(BookNotFoundException.class, () -> service.update(99L, request));
    }

    @Test
    void deleteThrowsWhenBookDoesNotExist() {
        assertThrows(BookNotFoundException.class, () -> service.delete(99L));
    }
}

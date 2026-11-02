package com.example.catalog;

import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;
import com.example.catalog.service.BookService;
import com.example.catalog.service.InvalidBookException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceValidationTest {

    private final BookService service = new BookService();

    @Test
    void createRejectsBlankTitle() {
        assertThrows(InvalidBookException.class,
                () -> service.create(new BookRequestDTO("978-1", "  ", "Evans", 2003)));
    }

    @Test
    void createRejectsNullIsbn() {
        assertThrows(InvalidBookException.class,
                () -> service.create(new BookRequestDTO(null, "DDD", "Evans", 2003)));
    }

    @Test
    void createRejectsBlankAuthor() {
        assertThrows(InvalidBookException.class,
                () -> service.create(new BookRequestDTO("978-1", "DDD", "", 2003)));
    }

    @Test
    void createRejectsNegativeYear() {
        assertThrows(InvalidBookException.class,
                () -> service.create(new BookRequestDTO("978-1", "DDD", "Evans", -1)));
    }

    @Test
    void rejectedBookIsNotStored() {
        assertThrows(InvalidBookException.class,
                () -> service.create(new BookRequestDTO("978-1", "", "Evans", 2003)));
        assertTrue(service.list().isEmpty());
    }

    @Test
    void updateRejectsInvalidDataAndKeepsOriginal() {
        BookResponseDTO created = service.create(new BookRequestDTO("978-1", "DDD", "Evans", 2003));

        assertThrows(InvalidBookException.class,
                () -> service.update(created.id(), new BookRequestDTO("978-1", " ", "Evans", 2003)));
        assertEquals("DDD", service.get(created.id()).title());
    }

    @Test
    void acceptsYearZero() {
        BookResponseDTO created = service.create(new BookRequestDTO("978-1", "DDD", "Evans", 0));
        assertEquals(0, created.year());
    }
}

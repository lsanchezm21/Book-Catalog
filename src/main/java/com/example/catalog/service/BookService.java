package com.example.catalog.service;

import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;
import com.example.catalog.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookService {

    private final Map<Long, Book> books = new ConcurrentSkipListMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<BookResponseDTO> list() {
        return books.values().stream().map(this::toResponse).toList();
    }

    public BookResponseDTO get(Long id) {
        Book book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException("Book not found: " + id);
        }
        return toResponse(book);
    }

    public BookResponseDTO create(BookRequestDTO request) {
        validate(request);
        Long id = nextId.getAndIncrement();
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        books.put(id, book);
        return toResponse(book);
    }

    public BookResponseDTO update(Long id, BookRequestDTO request) {
        validate(request);
        if (!books.containsKey(id)) {
            throw new BookNotFoundException("Book not found: " + id);
        }
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        books.put(id, book);
        return toResponse(book);
    }

    public void delete(Long id) {
        if (books.remove(id) == null) {
            throw new BookNotFoundException("Book not found: " + id);
        }
    }

    private void validate(BookRequestDTO request) {
        if (isBlank(request.isbn())) {
            throw new InvalidBookException("isbn must not be blank");
        }
        if (isBlank(request.title())) {
            throw new InvalidBookException("title must not be blank");
        }
        if (isBlank(request.author())) {
            throw new InvalidBookException("author must not be blank");
        }
        if (request.year() < 0) {
            throw new InvalidBookException("year must not be negative");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private BookResponseDTO toResponse(Book book) {
        return new BookResponseDTO(book.id(), book.isbn(), book.title(), book.author(), book.year());
    }
}

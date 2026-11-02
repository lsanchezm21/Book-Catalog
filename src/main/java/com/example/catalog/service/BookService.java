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
        return toResponse(books.get(id));
    }

    public BookResponseDTO create(BookRequestDTO request) {
        Long id = nextId.getAndIncrement();
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        books.put(id, book);
        return toResponse(book);
    }

    public BookResponseDTO update(Long id, BookRequestDTO request) {
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        books.put(id, book);
        return toResponse(book);
    }

    public void delete(Long id) {
        books.remove(id);
    }

    private BookResponseDTO toResponse(Book book) {
        return new BookResponseDTO(book.id(), book.isbn(), book.title(), book.author(), book.year());
    }
}
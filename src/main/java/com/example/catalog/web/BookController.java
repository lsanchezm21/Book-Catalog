package com.example.catalog.web;

import org.springframework.http.ResponseEntity;
import java.net.URI;

import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;
import com.example.catalog.model.Book;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/books")
public class BookController {

    private final Map<Long, Book> books = new ConcurrentSkipListMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @GetMapping
    public List<BookResponseDTO> list() {
        return books.values().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public BookResponseDTO getOne(@PathVariable("id") Long id) {
        return toResponse(books.get(id));
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> create(@RequestBody BookRequestDTO request) {
        Long id = nextId.getAndIncrement();
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        books.put(id, book);
        return ResponseEntity
                .created(URI.create("/books/" + id))
                .body(toResponse(book));
    }

    @PutMapping("/{id}")
    public BookResponseDTO update(@PathVariable("id") Long id, @RequestBody BookRequestDTO request) {
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        books.put(id, book);
        return toResponse(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        books.remove(id);
        return ResponseEntity.noContent().build();
    }

    private BookResponseDTO toResponse(Book book) {
        return new BookResponseDTO(book.id(), book.isbn(), book.title(), book.author(), book.year());
    }
}
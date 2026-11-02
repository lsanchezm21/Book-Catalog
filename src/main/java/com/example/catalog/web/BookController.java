package com.example.catalog.web;

import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;
import com.example.catalog.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookResponseDTO> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public BookResponseDTO getOne(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> create(@RequestBody BookRequestDTO request) {
        BookResponseDTO created = service.create(request);
        return ResponseEntity
                .created(URI.create("/books/" + created.id()))
                .body(created);
    }

    @PutMapping("/{id}")
    public BookResponseDTO update(@PathVariable("id") Long id, @RequestBody BookRequestDTO request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
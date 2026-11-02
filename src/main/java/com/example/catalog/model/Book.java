package com.example.catalog.model;

public record Book(Long id, String isbn, String title, String author, int year) {
}
package com.duo.book;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {
    private BookRepository repository;

    BookController(BookRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/api/boeken")
    public List<Book> allBooks()
    {
        return repository.findAll();
    }

    @GetMapping("/api/boeken/{id}")
    public Optional<Book> findBookByID(@PathVariable Long id)
    {
        return repository.findById(id);
    }



}

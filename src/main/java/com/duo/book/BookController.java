package com.duo.book;

import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/api/boeken")
    public Book addBook(@RequestBody Book newBook)
    {
       return repository.save(newBook);
    }
}

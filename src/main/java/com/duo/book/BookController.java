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

    //get all books
    @GetMapping("/api/boeken")
    public List<Book> allBooks()
    {
        return repository.findAll();
    }

    //get single book by id
    @GetMapping("/api/boeken/{id}")
    public Optional<Book> findBookByID(@PathVariable Long id)
    {
        return repository.findById(id);
    }

    //add new book
    @PostMapping("/api/boeken")
    public Book addBook(@RequestBody Book newBook)
    {
       return repository.save(newBook);
    }

    //Update book by id
    @PutMapping("api/boeken/{id}")
    public Book updateBook(@RequestBody Book newBook, @PathVariable Long id)
    {
        return repository.findById(id)
                .map(book -> {
                    newBook.setId(id);
                    book.setTitel(newBook.getTitel());
                    book.setUitgever(newBook.getUitgever());
                    return repository.save(book);
                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return repository.save(newBook);
                });
    }

    //delete book with id
    @DeleteMapping("api/boeken/{id}")
    public void deleteBook(@PathVariable Long id)
    {
        repository.deleteById(id);
    }
}

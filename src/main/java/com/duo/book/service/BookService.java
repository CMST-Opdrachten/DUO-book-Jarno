package com.duo.book.service;

import com.duo.book.BookNotFoundExceptoin;
import com.duo.book.objects.Book;
import com.duo.book.repositories.BookRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @Async
    public List<Book> findAllThread(){

        return repository.findAll();
    }

    @Async
    public Book findBookThread(Long id)
    {
        return repository.findById(id).orElseThrow(() -> new BookNotFoundExceptoin(id));
    }

    @Async
    public Book addBookThread(Book book)
    {
        return repository.save(book);
    }

    @Async
    public void deleteBookThread(Long id)
    {
        repository.deleteById(id);
    }

    @Async
    public Book putBookThread(Long id, Book newBook)
    {
        return repository.findById(id)
                .map(book -> {
                    book.setTitel(newBook.getTitel());
                    book.setUitgever(newBook.getUitgever());
                    return repository.save(book);
                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return repository.save(newBook);
                });
    }
}

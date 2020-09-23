package com.duo.book;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class BookRepositoryTest {

    @Autowired

    private BookRepository repository;

    @Test
    public int checkSize()
    {
        return repository.findAll().size();
    }
    @Test
    public void addBook()
    {
        int sizeBefore = checkSize();
        repository.save(new Book("Bob", "uitgever"));
        assertThat(sizeBefore + 1).isEqualTo(checkSize());
    }

    @Test
    public void removeBook()
    {
        int sizeBefore = checkSize();
        long id = repository.findAll().get(sizeBefore-1).getId();

        repository.deleteById(id);
        assertThat(sizeBefore - 1).isEqualTo(checkSize());
        assertThat(repository.findAll().get(checkSize()-1).getId()).isLessThan(id);
    }

    @Test
    public void selectBook()
    {
        repository.save(new Book("Jarno", "Hilverts"));
        long id = repository.findAll().get(checkSize()).getId();
        repository.findById(id);


    }


}



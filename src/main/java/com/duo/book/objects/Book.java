package com.duo.book.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titel;
    private String uitgever;

    public Book() {

    }

    public Book(String titel, String uitgever) {
        this.titel = titel;
        this.uitgever = uitgever;
    }

    public Long getId() {
        return id;
    }

    public String getTitel() {
        return titel;
    }

    public String getUitgever() {
        return uitgever;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public void setUitgever(String uitgever) {
        this.uitgever = uitgever;
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + this.id + ", titel='" + this.titel + '\'' + ", uitgever='" + this.uitgever + '\'' + '}';
    }
}

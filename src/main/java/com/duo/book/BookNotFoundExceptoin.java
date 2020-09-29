package com.duo.book;

public class BookNotFoundExceptoin extends RuntimeException {
    public BookNotFoundExceptoin(Long id) {
        super("Could not found book " + id);
    }
}

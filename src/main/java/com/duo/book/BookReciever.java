package com.duo.book;

import com.duo.book.objects.Book;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class BookReciever {

    @JmsListener(destination= "kast", containerFactory= "myFactory")
    public void recieveMessage(Book book)
    {
        System.out.println("Received <" + book.toString() + ">");
    }
}

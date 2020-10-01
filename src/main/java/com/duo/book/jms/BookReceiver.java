package com.duo.book.jms;

import com.duo.book.objects.Book;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class BookReceiver {

    @JmsListener(destination= "kast", containerFactory= "myFactory")
    public void recieveMessage(Book book)
    {
        System.out.println("Received <" + book.toString() + ">");
    }
}

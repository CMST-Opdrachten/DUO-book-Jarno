package com.duo.book;

import com.duo.book.objects.Book;
import com.duo.book.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;
import java.util.List;

@SpringBootApplication
@EnableJms
public class RestBookApplication
{

    private static BookRepository repository;

    RestBookApplication(BookRepository repository)
    {
        this.repository = repository;
    }

    public static void main(String[] args)
    {
        ConfigurableApplicationContext context = (SpringApplication.run(RestBookApplication.class, args));
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        System.out.println("Sending Books");
        for(int i = 0; i <= getBooks().size()-1 ; i++)
        {
            jmsTemplate.convertAndSend("kast", getBooks().get(i));
        }
    }

    @Bean
    public JmsListenerContainerFactory<?> myFactory(@Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer)
    {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter()
    {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    public static List<Book> getBooks()
    {
        return repository.findAll();
    }


}

package com.duo.book;

import com.duo.book.objects.Book;
import com.duo.book.repositories.BookRepository;
import com.duo.book.service.BookService;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration-test")
class EndPointsTest {

    @Autowired
    private BookService service;

    private static final String URL = "http://localhost:8080/api";

    @BeforeEach
    void addTestData()
    {
        service.deleteAllThread();
        service.addBookThread(new Book("test1", "uitgever"));
        service.addBookThread(new Book("test2", "uitgever"));
    }

    @Test
    void getResponses() {

        Long bookId1 = service.findAllThread().get(service.findAllThread().size()- 2).getId();
        Long bookId2 = service.findAllThread().get(service.findAllThread().size()- 1).getId();

        get(URL + "/boeken")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", is(service.findAllThread().size()));

        get(URL+ "/boeken/" + bookId1)
                .then()
                .assertThat()
                .statusCode(200)
                .body("titel", Matchers.equalTo("test1"))
                .body("uitgever", Matchers.equalTo("uitgever"));

        get(URL+ "/boeken/" + bookId2)
                .then()
                .assertThat()
                .statusCode(200)
                .body("titel", Matchers.equalTo("test2"))
                .body("uitgever", Matchers.equalTo("uitgever"));

    }

    @Test
    void postResponses()
    {
        String json = "{ \"titel\": \"ik ben een titel\", \"uitgever\": \"Ik ben de uitgever\" }";


        given()
                .contentType(ContentType.JSON)
                .body(json)
                .post(URL+ "/boeken")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        Long bookId = service.findAllThread().get(service.findAllThread().size() - 1).getId();

        get(URL + "/boeken/" + bookId)
                .then()
                .assertThat()
                .statusCode(200)
                .body("titel", Matchers.equalTo("ik ben een titel"))
                .body("uitgever", Matchers.equalTo("Ik ben de uitgever"));

    }
    @Test
    void updateResponse()
    {
        String json = "{ \"titel\": \"ik ben een geupdate titel\", \"uitgever\": \"Ik ben de geupdate uitgever\" }";

        Long bookId = service.findAllThread().get(service.findAllThread().size() - 1).getId();
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .put(URL + "/boeken/"+ bookId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        get(URL + "/boeken/" + bookId)
                .then()
                .assertThat()
                .statusCode(200)
                .body("titel", Matchers.equalTo("ik ben een geupdate titel"))
                .body("uitgever", Matchers.equalTo("Ik ben de geupdate uitgever"));
    }

    @Test
    void deleteRessponse()
    {
        Long bookId = service.findAllThread().get(service.findAllThread().size() - 1).getId();
        delete(URL + "/boeken/" + bookId)
                .then()
                .assertThat()
                .statusCode(200);

    }
}

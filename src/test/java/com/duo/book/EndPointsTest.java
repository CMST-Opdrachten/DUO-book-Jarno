package com.duo.book;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ActiveProfiles("integration-test")
public class EndPointsTest {

    @Autowired
    private BookRepository repository;

    @Test
    public void addTestData()
    {

        repository.save(new Book("test1", "uitgever"));
        repository.save(new Book("test2", "uitgever"));
    }

    @Test
    public void getResponses() {

        get("http://localhost:8080/api/boeken")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", is(repository.findAll().size()));

        get("http://localhost:8080/api/boeken/1")
                .then()
                .assertThat()
                .statusCode(200)
                .body("titel", Matchers.equalTo("test1"))
                .body("uitgever", Matchers.equalTo("uitgever"));

        get("http://localhost:8080/api/boeken/2")
                .then()
                .assertThat()
                .statusCode(200)
                .body("titel", Matchers.equalTo("test2"))
                .body("uitgever", Matchers.equalTo("uitgever"));

    }

    @Test
    public void postResponses()
    {
        String json = "{ \"titel\": \"ik ben een titel\", \"uitgever\": \"Ik ben de uitgever\" }";

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .post("http://localhost:8080/api/boeken")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        get("http://localhost:8080/api/boeken/" + repository.findAll().get(repository.findAll().size() - 1).getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("titel", Matchers.equalTo("ik ben een titel"))
                .body("uitgever", Matchers.equalTo("Ik ben de uitgever"));

    }
    @Test
    public void updateResponse()
    {
        String json = "{ \"titel\": \"ik ben een geupdate titel\", \"uitgever\": \"Ik ben de geupdate uitgever\" }";

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .put("http://localhost:8080/api/boeken/2")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        get("http://localhost:8080/api/boeken/2")
                .then()
                .assertThat()
                .statusCode(200)
                .body("titel", Matchers.equalTo("ik ben een geupdate titel"))
                .body("uitgever", Matchers.equalTo("Ik ben de geupdate uitgever"));
    }

    @Test
    public void deleteRessponse()
    {
        delete("http://localhost:8080/api/boeken/1")
                .then()
                .assertThat()
                .statusCode(200);

        get("http://localhost:8080/api/boeken/1")
                .then()
                .assertThat()
                .statusCode(200)
                .body(Matchers.equalTo("null"));

    }
}

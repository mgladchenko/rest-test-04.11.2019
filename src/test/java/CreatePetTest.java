import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreatePetTest {

    static long petId;

    @Test
    public void test1CreatePet() {

        String body = "{\n" +
                "  \"id\": 0,\n" +
                "  \"category\": {\n" +
                "    \"id\": 0,\n" +
                "    \"name\": \"string\"\n" +
                "  },\n" +
                "  \"name\": \"kitty\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"string\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";

        ValidatableResponse response = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .post("https://petstore.swagger.io/v2/pet")
                .then()
                .statusCode(is(200))
                .body("category.name", is(not("")))
                .log().all();
        petId = response.extract().path("id");
        System.out.println(petId);
    }

    @Test
    public void test2GetPetById() {
        System.out.println(petId);
        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .get("https://petstore.swagger.io/v2/pet/"+petId)
                .then()
                .statusCode(is(200))
                .body("category.name", is(not("")))
                .log().all();
    }

    @Test
    public void test3DeletePetById() {
        System.out.println(petId);
        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .delete("https://petstore.swagger.io/v2/pet/"+petId)
                .then()
                .statusCode(is(200))
                .log().all();
    }

}

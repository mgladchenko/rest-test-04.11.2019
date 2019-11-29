import data.Pet;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;

public class PetTest {

    private PetEndpoint petEndpoint = new PetEndpoint();

    private Pet pet = new Pet(0, "string", "kitty", "booked");

    private static long petId;

    @Before
    public void beforeMethod() {
        ValidatableResponse response = petEndpoint
                .createPet(pet)
                .statusCode(is(200))
                .body("category.name", is(not("")));
        petId = response.extract().path("id");
    }

    @Test
    public void createPet() {
        petEndpoint
                .createPet(pet)
                .statusCode(is(200))
                .body("category.name", is(not("")));
    }

    @Test
    public void getPetById() {
        petEndpoint
                .getPet(petId)
                .statusCode(is(200))
                .body("category.name", is(not("")));
    }

    @Test
    public void deletePetById() {
        petEndpoint
                .deletePet(petId)
                .statusCode(is(200));

        petEndpoint
                .getPet(petId)
                .statusCode(is(404))
                .body("message", is("data.Pet not found"));
    }

    @Test
    public void getPetByStatus() {
        petEndpoint
                .getPetByStatus("booked")
                .statusCode(200)
                .body("status[0]", is("booked")); //ToDo: verify each element status
    }

    @Test
    public void updatePet() {
        String body = "{\n" +
                "  \"id\": "+petId+",\n" +
                "  \"category\": {\n" +
                "    \"id\": 0,\n" +
                "    \"name\": \"pets\"\n" +
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
                "  \"status\": \"booked\"\n" +
                "}";

        petEndpoint
                .updatePet(body)
                .statusCode(200)
                .body("category.name", is("pets"));
    }

    @Test
    public void updatePetById() {
        petEndpoint
                .updatePetById(petId, "kitty cat", "available")
                .statusCode(200);
        petEndpoint
                .getPet(petId)
                .statusCode(200)
                .body("name", is("kitty cat"))
                .body("status", is("available"));
    }

}

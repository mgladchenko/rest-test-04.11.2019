import data.Pet;
import data.Status;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.core.Is.is;

public class PetTest {

    private PetEndpoint petEndpoint = new PetEndpoint();

    private Pet pet = new Pet(0, "string", "kitty", Status.pending);

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
                .body("message", is("Pet not found"));
    }

    @Test
    public void getPetByStatus() {
        petEndpoint
                .getPetByStatus(Status.pending)
                .statusCode(200)
                .body("status", everyItem(is(Status.pending.toString())));
    }

    @Test
    public void updatePet() {
        Pet updatedPet = new Pet(petId, "pets", "kitty", Status.pending);

        petEndpoint
                .updatePet(updatedPet)
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

    @Test
    public void uploadPetImage() {
        petEndpoint
                .uploadPetImage(petId, "kitty_cat.png")
                .statusCode(200);
    }

}

package tests;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.TestInstance;
import tests.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LojaDeLivros {


    private int bookingId;
    public String url = "https://restful-booker.herokuapp.com/booking/";
    private static Map<Integer, Integer> bookingIds = new HashMap<>();


    @BeforeAll
    public void criarLivroAntesDoTeste() throws Exception {
        JsonNode jsonArray = JsonUtils.lerJsonArray("createBooks.json");
        int total = jsonArray.size();

        for (int i = 0; i < total; i++) {
            JsonNode jsonBody = jsonArray.get(i);

            bookingId = given().log().all()
                    .contentType(ContentType.JSON)
                    .body(jsonBody)
                    .when()
                    .post(url)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .extract().jsonPath().getInt("bookingid");

            bookingIds.put(i, bookingId);
        }
    }

    @Test
    public void consultarLivro() {
            int id = bookingIds.get(0);

        given().log().all()
                .when().get(url + id)
                .then()
                .assertThat()
                .statusCode(200)
                .body("firstname", equalTo("Jim"))
                .body("lastname", equalTo("Brown"));
    }

    @Test
    public void consultarLivroDoArquivoJson() throws Exception {
        int id = bookingIds.get(1);

        given().log().all()
                .when().get(url + id)
                .then()
                .assertThat()
                .statusCode(200)
                .body("firstname", equalTo("Milly Bob"))
                .body("lastname", equalTo("Brown"));
    }

    @Test
    public void buscarLivroPorNomeESobrenome() {
        int id = bookingIds.get(2);

        given().log().all()
                .queryParam("firstname", "Anniee Tikka")
                .queryParam("lastname", "Brown")
                .when()
                .get(url)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }
}

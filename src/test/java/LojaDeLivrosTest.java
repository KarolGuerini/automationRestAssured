import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.TestInstance;
import config.Config;
import utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LojaDeLivrosTest {


    private int bookingId;
    public String url = Config.get("api.url");
    private static Map<Integer, Integer> bookingIds = new HashMap<>();


    @BeforeAll
    public void criarLivroAntesDoTeste() throws Exception {
        JsonNode jsonArray = JsonUtils.lerJsonArray("createBooks.json");
        int total = jsonArray.size();

        for (int i = 0; i < total; i++) {
            JsonNode jsonBody = jsonArray.get(i);

            bookingId = given()
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
    public void consultarLivros() {
        String responseBody =
                given()
        .when().get(url)
        .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract().body().asString();

        assertThat(responseBody, not(emptyOrNullString()));
        assertThat(responseBody, containsString("bookingid"));
    }

    @Test
    public void consultarLivroDoArquivoJson() throws Exception {
        int id = bookingIds.get(1);

        given()
        .when().get(url + id)
        .then()
                .assertThat()
                .statusCode(200)
                .body("firstname", equalTo("Millyy Bob"))
                .body("lastname", equalTo("Brown"));
    }

    @Test
    public void buscarLivroPorNomeESobrenome() {
        int id = bookingIds.get(2);

        given()
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

package es.codeurjc.ais.nitflex.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import es.codeurjc.ais.nitflex.utils.UrlUtils;

@DisplayName("Url Utils integration tests")
public class UrlUtilsIntegrationTest {

    UrlUtils urlUtils = new UrlUtils();

    @Test
    @DisplayName("Cuando una URL NO tiene el formato correcto, debemos dar la URL por inválida")
	public void testNotValidUrl_MalformedURL(){
        String invalidUrl = "NOT_URL";
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, ()->{
            urlUtils.checkValidImageURL(invalidUrl);
        });
        assertThat(ex.getMessage())
            .contains("The url format is not valid")
            .contains("400 BAD_REQUEST");
  
    }

}

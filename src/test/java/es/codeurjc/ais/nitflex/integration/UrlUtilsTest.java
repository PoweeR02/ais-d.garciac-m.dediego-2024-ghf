package es.codeurjc.ais.nitflex.integration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import es.codeurjc.ais.nitflex.utils.UrlUtils;

class UrlUtilsTest {

    private UrlUtils urlUtils;

    @BeforeEach
    public void setUp() {
        urlUtils = new UrlUtils();
    }

    @Test
    void givenInvalidURL_whenCheckingURLValidity_thenUrlUtilsShouldReturnInvalidUrl() {

        // Given
        String invalidUrl = "esto-no-es-una-url";

        // When & Then
        assertThatThrownBy(() -> {
            urlUtils.checkValidImageURL(invalidUrl);
        }).isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
                .hasMessageContaining("The url format is not valid");
    }

    @Test
    void givenValidImageURL_whenCheckingURLValidity_thenUrlUtilsShouldReturnInvalidUrl() {

        // Given
        String validImageUrl = "https://www.themoviedb.org/image.png";

        // When & Then
        assertThatThrownBy(() -> {
            urlUtils.checkValidImageURL(validImageUrl);
        }).isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
                .hasMessageContaining("Url resource does not exists");
    }

}

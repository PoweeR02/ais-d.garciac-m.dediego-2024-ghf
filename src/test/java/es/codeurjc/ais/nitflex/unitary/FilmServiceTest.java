package es.codeurjc.ais.nitflex.unitary;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import es.codeurjc.ais.nitflex.film.Film;
import es.codeurjc.ais.nitflex.film.FilmRepository;
import es.codeurjc.ais.nitflex.film.FilmService;
import es.codeurjc.ais.nitflex.notification.NotificationService;
import es.codeurjc.ais.nitflex.utils.UrlUtils;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FilmServiceTest {

        private FilmRepository filmRepository = mock(FilmRepository.class);
        private NotificationService notificationService = mock(NotificationService.class);
        private UrlUtils urlUtils = mock(UrlUtils.class);
        private FilmService filmService;

        @BeforeEach
        public void setUp() {
                filmService = new FilmService(filmRepository, notificationService, urlUtils);
        }

        @Test
        void givenCorrectUrl_whenMovieSaved_thenNotificationTriggered() {

                // Given
                Film filmToSave = new Film("Dune",
                                "En un lejano futuro, la galaxia conocida es gobernada mediante un sistema feudal de casas nobles bajo el mandato del Emperador.",
                                2021,
                                "https://www.themoviedb.org/t/p/w220_and_h330_face/m6XWQpT0biTpe5wBGWd60RXmtEX.jpg");

                // Metodo "save": devuelve un Film
                when(filmRepository.save(filmToSave)).thenReturn(filmToSave);

                // When
                assertDoesNotThrow(() -> filmService.save(filmToSave));

                // Then
                verify(urlUtils, times(1)).checkValidImageURL(filmToSave.getUrl());
                verify(filmRepository, times(1)).save(filmToSave);
                verify(notificationService, times(1))
                                .notify("Film Event: Film with title=" + filmToSave.getTitle() + " was created");
        }

        @Test
        void givenIncorrectUrl_whenMovieNotSaved_thenNoNotificationTriggered() {

                // Given
                Film filmToNotSave = new Film("Dune",
                                "En un lejano futuro, la galaxia conocida es gobernada mediante un sistema feudal de casas nobles bajo el mandato del Emperador.",
                                2021, "url incorrecto");

                /*
                 * Se interpreta que un "URL incorrecto" es aquel que tiene el formato
                 * incorrecto independientemente de que
                 * sea en formato imagen o no
                 */
                doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "The url format is not valid"))
                                .when(urlUtils).checkValidImageURL(filmToNotSave.getUrl());

                // When
                assertThatThrownBy(() -> {
                        filmService.save(filmToNotSave);
                }).isInstanceOf(ResponseStatusException.class)
                                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
                                .hasMessageContaining("The url format is not valid");

                // Then
                /*
                 * Estos dos "verify" se podrian omitir dado que el metodo "save" de la clase
                 * "FilmService" termina con la
                 * excepcion que lanza el metodo "checkValidImageURL" de la clase "UrlUtils" y
                 * por lo tanto esos dos
                 * metodos nunca llegarian a ejecutarse
                 */
                verify(filmRepository, never()).save(filmToNotSave);
                verify(notificationService, never())
                                .notify("Film Event: Film with title=" + filmToNotSave.getTitle() + " was created");
        }

}

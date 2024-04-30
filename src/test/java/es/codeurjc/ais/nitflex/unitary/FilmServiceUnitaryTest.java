package es.codeurjc.ais.nitflex.unitary;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.codeurjc.ais.nitflex.film.Film;
import es.codeurjc.ais.nitflex.film.FilmRepository;
import es.codeurjc.ais.nitflex.film.FilmService;
import es.codeurjc.ais.nitflex.notification.NotificationService;
import es.codeurjc.ais.nitflex.utils.UrlUtils;

@DisplayName("FilmService Unitary tests")
public class FilmServiceUnitaryTest {

    private FilmService filmService;
    private NotificationService notificationService;
    private FilmRepository repository;
    private UrlUtils urlUtils;
    
    @Test
    @DisplayName("Cuando se guarda una película (con una URL correcta) utilizando FilmService, se guarda en el repositorio y se lanza una notificación")
    public void createFilm() {

        repository = mock(FilmRepository.class);
        notificationService = mock(NotificationService.class);
        urlUtils = mock(UrlUtils.class);
        filmService = new FilmService(repository, notificationService, urlUtils);

        Film book = new Film("FAKE FILM", "FAKE DESCRIPTION", 1900, "FAKE URL");

        // Given
        when(repository.save(book)).thenReturn(book);

        // When
        filmService.save(book);

        // Then
        verify(repository, times(1)).save(book);
        verify(urlUtils, times(1)).checkValidImageURL("FAKE URL");
        verify(notificationService).notify("Film Event: Film with title=" + book.getTitle() + " was created");
    }

}

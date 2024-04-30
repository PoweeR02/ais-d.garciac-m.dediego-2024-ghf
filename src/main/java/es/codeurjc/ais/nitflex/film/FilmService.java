package es.codeurjc.ais.nitflex.film;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.codeurjc.ais.nitflex.notification.NotificationService;
import es.codeurjc.ais.nitflex.utils.UrlUtils;

/* Este servicio se usará para incluir la funcionalidad que sea 
 * usada desde el FilmRestController y el FilmWebController
 */
@Service
public class FilmService {

	private FilmRepository repository;
	private NotificationService notificationService;
	private UrlUtils urlUtils;

	public FilmService(FilmRepository repository, NotificationService notificationService, UrlUtils urlUtils){
		this.repository = repository;
		this.notificationService = notificationService;
		this.urlUtils = urlUtils;
	}

	public Optional<Film> findOne(long id) {
		return repository.findById(id);
	}
	
	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<Film> findAll() {
		return repository.findAll();
	}

	public Film save(Film film) {
		if (film.getTitle() == "") {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title is empty");
		}
		urlUtils.checkValidImageURL(film.getUrl());
		Film newFilm = repository.save(film);
		notificationService.notify("Film Event: Film with title="+newFilm.getTitle()+" was created");
		return newFilm;
	}

	public void delete(long id) {
		repository.deleteById(id);
		notificationService.notify("Film Event: Film with id="+id+" was deleted");
	}
}

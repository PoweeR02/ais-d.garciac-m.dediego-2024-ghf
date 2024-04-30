package es.codeurjc.ais.nitflex.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import es.codeurjc.ais.nitflex.film.Film;
import es.codeurjc.ais.nitflex.film.FilmService;

@Controller
public class FilmWebController {

	@Autowired
	private FilmService filmService;
	
	@GetMapping("/")
	public String showFilms(Model model) {

		model.addAttribute("films", filmService.findAll());
		
		return "films";
	}
	
	@GetMapping("/films/{id}")
	public String showFilm(Model model, @PathVariable long id) {
		
		Optional<Film> op = filmService.findOne(id);
		if(op.isPresent()) {
			Film film = op.get();
			model.addAttribute("film", film);
			return "film";
		}else {
			return "films";
		}
		
	}
	
	@GetMapping("/removefilm/{id}")
	public String removeFilm(Model model, @PathVariable long id) {
		
		Optional<Film> op = filmService.findOne(id);
		if(op.isPresent()) {
			filmService.delete(id);
			Film removedFilm = op.get();
			model.addAttribute("error", false);
			model.addAttribute("message", "Film '"+removedFilm.getTitle()+"' deleted");
			return "message";
		}else {
			return "redirect:/";
		}
		
	}
	
	@GetMapping("/newfilm")
	public String newFilm(Model model) {
		return "newFilmPage";
	}
	
	@PostMapping("/createfilm")
	public String newBookProcess(Film film) {
		
		Film newFilm = filmService.save(film);
		
		return "redirect:/films/" + newFilm.getId();
	}
	
	@GetMapping("/editfilm/{id}")
	public String editBook(Model model, @PathVariable long id) {
		
		Optional<Film> op = filmService.findOne(id);
		if(op.isPresent()) {
			Film film = op.get();
			model.addAttribute("film", film);
			return "editFilmPage";
		}else {
			return "films";
		}
		
	}
	
	@PostMapping("/editfilm")
	public String editBookProcess(Model model, Film film) {
		
		filmService.save(film);

		model.addAttribute("film", film);
		
		return "redirect:/films/" + film.getId();
	}

	// Cuando se produce una excepción 'ResponseStatusException' se ejecuta este método
	// -> Devuelve una vista con un mensaje 
	@ExceptionHandler({ResponseStatusException.class, BindException.class})
    public ModelAndView handleException(Exception ex){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("message");
		modelAndView.addObject("error", true);

		if(ex instanceof ResponseStatusException){
			ResponseStatusException resExp = (ResponseStatusException) ex;
			modelAndView.addObject("message", resExp.getReason());
		}else if(ex instanceof BindException){
			modelAndView.addObject("message", "Field 'year' must be a number");
		}else{
			modelAndView.addObject("message", ex.getMessage());
		}

        return modelAndView;
    }


}

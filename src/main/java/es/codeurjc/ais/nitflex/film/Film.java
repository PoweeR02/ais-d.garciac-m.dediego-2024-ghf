package es.codeurjc.ais.nitflex.film;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Film {
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id = null;

    private String title;
	
	@Column(length = 50000)
	private String synopsis;

    private int releaseYear;

    private String url;

    public Film() {}

    public Film(String title, String synopsis, int year, String url) {
        this.title = title;
        this.synopsis = synopsis;
        this.releaseYear = year;
        this.url = url;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int year) {
        this.releaseYear = year;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString(){
        return "Film [title="+this.title+", releaseYear="+this.releaseYear+"]";
    }

}

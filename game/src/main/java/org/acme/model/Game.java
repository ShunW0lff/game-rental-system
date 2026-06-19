package org.acme.model;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Game extends PanacheEntity{
    private String name;
    private String year;
    private String genre;

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
    
}

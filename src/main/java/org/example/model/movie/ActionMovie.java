package org.example.model.movie;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("action")

public class ActionMovie extends Movie {
    public ActionMovie(String title, java.util.Date startShowDate, java.time.Duration timeDuration, String category, double basicPrice, org.example.model.persons.Director director) {
        super(title, startShowDate, timeDuration, category, basicPrice, director);
    }

    public ActionMovie() {
        super();
    }
}

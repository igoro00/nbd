package org.example.model.movie;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("sci_fi")

public class SciFi extends Movie {
    public SciFi(String title, java.util.Date startShowDate, java.time.Duration timeDuration, String category, double basicPrice, org.example.model.persons.Director director) {
        super(title, startShowDate, timeDuration, category, basicPrice, director);
    }

    public SciFi() {
        super();
    }
}

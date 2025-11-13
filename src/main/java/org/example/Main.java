package org.example;

import org.example.model.*;
import org.example.repositories.ClientRepository;
import org.example.repositories.HallRepository;
import org.example.repositories.MovieRepository;
import org.example.repositories.ScreeningRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try (
                ClientRepository clientRepo = new ClientRepository();
                MovieRepository movieRepo = new MovieRepository();
                HallRepository hallRepo = new HallRepository();
                ScreeningRepository screeningRepo = new ScreeningRepository();
        ){
            Client c = new Client(
                    "John",
                    "Doe",
                    "agnieszka.duraj@p.lodz.pl",
                    sdf.parse("15-04-1974"),
                    new Address("Lodz", "90-001", "Bałtycka", "44")
            );
            clientRepo.add(c);
            Movie movie = new Movie(
                    "Inception",
                    Duration.ofMinutes(148),
                    "Sci-Fi",
                    12.5,
                    new Director("Christopher", "Nolan")
            );
            movieRepo.add(movie);
            Hall hall = new Hall("Hall 1", 100, 100);
            hallRepo.add(hall);
            Screening screening = new Screening(
                    movie,
                    hall,
                    new GregorianCalendar(2025, Calendar.MARCH, 1, 15, 30).getTime()
            );
            screeningRepo.add(screening);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
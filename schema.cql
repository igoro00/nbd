CREATE KEYSPACE IF NOT EXISTS absolute_cinema
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE absolute_cinema;

-- Table: clients
CREATE TABLE IF NOT EXISTS clients (
    client_id uuid,
    first_name text,
    last_name text,
    date_of_birth timestamp,
    email text,
    PRIMARY KEY (client_id)
);

-- Table: movies
CREATE TABLE IF NOT EXISTS movies (
    movie_id uuid,
    title text,
    duration duration,
    category text,
    price double,
    director_name text,
    PRIMARY KEY (movie_id)
);

-- Table: screenings_by_movie
-- Optimized for: "Give me all screenings for a specific movie"
CREATE TABLE IF NOT EXISTS screenings_by_movie (
    movie_id uuid,
    screening_id uuid,
    movie_name text,
    hall_name text,
    start_date timestamp,
    movie_duration duration,
    PRIMARY KEY (movie_id, screening_id)
) WITH CLUSTERING ORDER BY (screening_id ASC);

-- Table: tickets_by_screening
-- Optimized for: "Give me all tickets/seats for a specific screening"
-- Clustering columns ensure seats are ordered logically.
CREATE TABLE IF NOT EXISTS tickets_by_screening (
    screening_id uuid,
    seat_column int,
    seat_row int,
    client_id uuid,
    PRIMARY KEY (screening_id, seat_column, seat_row)
) WITH CLUSTERING ORDER BY (seat_column ASC, seat_row ASC);
package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Repository<T> {

    private List<T> elements;

    // Konstruktor inicjalizujący pustą listę
    public Repository() {
        this.elements = new ArrayList<>();
    }

    // Pobranie elementu po indeksie (id)
    public T get(int id) {
        if (id >= 0 && id < elements.size()) {
            return elements.get(id);
        }
        return null; // Zwraca null, jeśli id jest poza zakresem
    }

    // Dodanie elementu do repozytorium
    public boolean add(T element) {
        return elements.add(element); // Zwraca true, jeśli dodanie się powiodło
    }

    // Wyszukiwanie elementu na podstawie predykatu
    public T find(Predicate<T> predicate) {
        for (T element : elements) {
            if (predicate.test(element)) {
                return element; // Zwraca pierwszy pasujący element
            }
        }
        return null; // Zwraca null, jeśli żaden element nie spełnia warunku
    }

    // Wyszukiwanie wszystkich elementów pasujących do predykatu
    public List<T> findAll(Predicate<T> predicate) {
        List<T> foundElements = new ArrayList<>();
        for (T element : elements) {
            if (predicate.test(element)) {
                foundElements.add(element);
            }
        }
        return foundElements;
    }

    // Raportowanie wszystkich elementów w repozytorium (jako string)
    public String report() {
        StringBuilder report = new StringBuilder("Repository Report:\n");
        for (int i = 0; i < elements.size(); i++) {
            report.append(i).append(": ").append(elements.get(i).toString()).append("\n");
        }
        return report.toString();
    }

    // Zwracanie rozmiaru repozytorium
    public int size() {
        return elements.size();
    }

    // Usuwanie elementu z repozytorium
    public boolean remove(T element) {
        return elements.remove(element); // Zwraca true, jeśli usunięcie się powiodło
    }
}

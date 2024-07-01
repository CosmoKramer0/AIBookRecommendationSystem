import java.io.*;
import java.util.*;
import javax.swing.*;

public class phase2codeWithGUI {

    static class Book {
        String title;
        String author;
        String genre;
        double rating;

        public Book(String title, String author, String genre, double rating) {
            this.title = title;
            this.author = author;
            this.genre = genre;
            this.rating = rating;
        }
    }

    static List<Book> readBooksFromFile(String filename) throws IOException {
        List<Book> books = new ArrayList<>();
        int lineNumber = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    System.err.println("Error reading line " + lineNumber + ": Invalid format");
                    continue;
                }
                String title = parts[0].trim();
                String author = parts[1].trim();
                String genre = parts[2].trim();
                double rating = Double.parseDouble(parts[3].trim());
                books.add(new Book(title, author, genre, rating));
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing rating in line " + lineNumber + ": " + e.getMessage());
            throw e;
        }
        return books;
    }

    static double heuristicFunction(Book book, String userPreference) {
        double genreMatch = book.genre.equalsIgnoreCase(userPreference) ? 1.0 : 0.0;
        double ratingScore = book.rating;
        return genreMatch * ratingScore;
    }

    static Set<Book> recommendBooks(Book[] books, String userPreference) {
        PriorityQueue<Book> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(book -> -heuristicFunction(book, userPreference)));
        Set<Book> recommendedBooks = new LinkedHashSet<>();

        priorityQueue.addAll(Arrays.asList(books));

        while (!priorityQueue.isEmpty() && recommendedBooks.size() < 3) {
            Book currentBook = priorityQueue.poll();
            if (heuristicFunction(currentBook, userPreference) > 0) {
                recommendedBooks.add(currentBook);
            }
        }

        return recommendedBooks;
    }

    public static void main(String[] args) {
        List<Book> books = new ArrayList<>();
        try {
            books = readBooksFromFile("C:Book Sample.txt");
    
            final List<Book> finalBooks = books; // Declare books as final
    
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new SmartBooksGUI(finalBooks); // Use finalBooks inside the inner class
                }
            });
    
        } catch (IOException e) {
            System.err.println("Error reading book dataset: " + e.getMessage());
        }
    }
}


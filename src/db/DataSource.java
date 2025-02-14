package db;

import entity.*;
import entity.enums.Role;
import entity.enums.SectionState;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class DataSource {

    public static void refreshScanner(){
        Scanner strScanner = new Scanner(System.in);
        Scanner intScanner = new Scanner(System.in);
        Scanner doubleScanner = new Scanner(System.in);
    }
    public static Scanner strScanner = new Scanner(System.in);
    public static Scanner intScanner = new Scanner(System.in);
    public static Scanner doubleScanner = new Scanner(System.in);

    public static ArrayList<Section> sections = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();

    public static User currentUser;
    public static User getCurrentUser(){return currentUser;}
    public static void setCurrentUser(User user){currentUser = user;}

    public static Section currentSection;
    public static Book currentBook;

    static {
        users.add(new User(Role.ADMIN, "admin", "admin", "admin", "admin", new ArrayList<History>(), new ArrayList<Borrow>(), 500000.0 ));
        users.add(new User(Role.USER, "a", "a", "a", "a", new ArrayList<History>(), new ArrayList<Borrow>(), 100000.0 ));
        users.add(new User(Role.USER, "b", "b", "b", "b", new ArrayList<History>(), new ArrayList<Borrow>(), 200000.0 ));
    }

    static {
        ArrayList<Book> books1 = new ArrayList<>();
        Section novel = new Section(UUID.randomUUID().toString(), "novel", books1, SectionState.ENABLED);
        Book book1 = new Book("War and Peace", "Anna Karenina", novel, 10);
        Book book2 = new Book("War and Peace 2", "Anna Karenina", novel, 10);
        book1.setAvailableBook(10);
        book2.setAvailableBook(5);

        books1.add(book1);
        books1.add(book2);

        ArrayList<Book> books2 = new ArrayList<>();
        Section fantasy = new Section(UUID.randomUUID().toString(), "fantasy", books2, SectionState.ENABLED);
        Book book3 = new Book("A Game of Thrones", "George M", fantasy, 10);
        Book book4 = new Book("Tigana", "Gavriel K", fantasy, 5);
        book3.setAvailableBook(10);
        book4.setAvailableBook(5);

        books2.add(book3);
        books2.add(book4);

        sections.add(novel);
        sections.add(fantasy);

    }
}

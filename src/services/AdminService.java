package services;

import entity.*;
import entity.enums.Role;
import entity.enums.SectionState;

import java.util.List;
import java.util.Objects;

import static db.DataSource.*;

public class AdminService {
    public static void service() {
        while (true) {
            System.out.println("""
                    0 exit
                    1 add section
                    2 show sections
                    3 show section
                    4 section state
                    5 add book
                    6 remove book
                    7 book state
                    8 show users
                    9 show user
                    """);
            switch (intScanner.nextInt()) {
                case 0 -> {
                    return;
                }
                case 1 -> addSection();
                case 2 -> showSections();
                case 3 -> showSection();
                case 4 -> sectionState();
                case 5 -> addBook();
                case 6 -> removeBook();
                case 7 -> bookState();
                case 8 -> showUsers();
                case 9 -> showUser();
                default -> System.out.println("invalid option has chosen!");
            }
        }
    }

    private static void addSection() {
        System.out.println("Enter new section name");
        String sectionName = strScanner.nextLine();
        for (Section section : sections) {
            if (Objects.equals(section.getName(), sectionName)) {
                System.out.println("Such kinda section name already exists!");
                return;
            }
        }
        Section section = new Section();
        section.setName(sectionName);
        section.setStatus(SectionState.ENABLED);
        sections.add(section);
        System.out.println("Section added successfully!");
    }

    private static void showSections() {
        showingSections();
    }

    private static void showSection() {
        showingSections();
        System.out.println("-----------------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println("Enter name of the section");
        String nameOfSection = strScanner.nextLine();

        for (Section section : sections) {
            if (Objects.equals(section.getName(), nameOfSection)) {
                System.out.println("Current section : " + section.getName());

                List<Book> currentSectionBooks = section.getBooks();
                if (currentSectionBooks == null || currentSectionBooks.isEmpty()) {
                    System.out.println("no books available in the section");
                    return;
                } else {
                    for (Book book : currentSectionBooks) {
                        if (book.getSection().getName().equals(nameOfSection)) {
                            System.out.println(book);
                        }
                    }
                }
                return;
            }
        }
    }

    private static void sectionState() {
        Section section = findSection();
        System.out.println("""
                Choose Section State :
                0 exit
                1 Enabled
                2 Disabled
                """);
        switch (intScanner.nextInt()) {
            case 0 -> {
                return;
            }
            case 1 -> {
                if(section == null){
                    System.out.println("section not found!");
                    return;
                }
                section.setStatus(SectionState.ENABLED);
                System.out.println("Section state successfully changed to enable!");
            }
            case 2 -> {
                if(section == null){
                    System.out.println("section not found!");
                    return;
                }
                section.setStatus(SectionState.DISABLED);
                System.out.println("Section state successfully changed to disable!");
            }
            default -> System.out.println("invalid option!");
        }

    }

    private static void addBook() {
        System.out.println("Enter section name");
        String sectionName = strScanner.nextLine();
        for (Section section : sections) {
            if (Objects.equals(section.getName(), sectionName)) {
                currentSection = section;
                break;
            }
        }
        if(currentSection == null){
            System.out.println("invalid section name");
        }else {
             Book book = new Book();
            System.out.println("Enter new book title");
            book.setTitle(strScanner.nextLine());
            System.out.println("Enter new book author");
            book.setAuthor(strScanner.nextLine());
            System.out.println("Enter total book");
            int totalBook = intScanner.nextInt();
            book.setTotalBook(totalBook);
            book.setAvailableBook(totalBook);
            book.setSection(currentSection);
            currentSection.getBooks().add(book);
            System.out.println("Book added successfully!");
        }



    }

    private static void removeBook() {
        Section section = findSection();
        if(section == null){
            System.out.println("section not found!");
            return;
        }
        List<Book> booksList = section.getBooks();
        System.out.println("Enter book id to remove");
        String bookId = strScanner.nextLine();
        boolean bookFound = false;
        for (Book book : booksList) {
            if (Objects.equals(book.getId(), bookId)) {
                bookFound = true;
                booksList.remove(book);
                System.out.println("book successfully removed!");
                break;
            }
        }
        if (!bookFound) {
            System.out.println("book not found!");
        }
    }

    private static void bookState() {
        Section section = findSection();
        if(section == null){
            System.out.println("section not found!");
            return;
        }
        List<Book> booksList = section.getBooks();
        System.out.println("Enter book id to see the state");
        String bookId = strScanner.nextLine();
        boolean bookFound = false;
        for (Book book : booksList) {
            if (Objects.equals(book.getId(), bookId)) {
                bookFound = true;
                System.out.println(book);
            }
        }
        if (!bookFound) {
            System.out.println("book not found!");
        }

    }

    private static void showUsers() {
        for (User user : users) {
            if (user.getRole().equals(Role.USER)) {
                System.out.println(user);
            }
        }
    }

    private static void showUser() {
        System.out.println("Enter user id");
        String userId = strScanner.nextLine();
        User user = null;
        for (User user1 : users) {
            if(Objects.equals(user1.getId(), userId)){
                System.out.println(user1.toString1());
                user = user1;
                break;
            }
        }
        if(user == null) {
            System.out.println("user not found");
            return;
        }
        System.out.println("Borrow list :");
        if(user.getBorrowList().isEmpty()){
            System.out.println("borrow list is empty!");
            return;
        }

        for (Borrow borrow : user.getBorrowList()) {
            System.out.println(borrow);
        }

        System.out.println("History list :");
        if(user.getHistories().isEmpty()){
            System.out.println("history list is empty!");
            return;
        }

        for (History history : user.getHistories()) {
            System.out.println(history);
        }
    }

    private static void showingSections() {
        for (Section section : sections) {
            System.out.println(section);
        }
    }

    private static Section findSection() {
        System.out.println("Enter section name");
        String sectionName = strScanner.nextLine();
        for (Section section : sections) {
            if (Objects.equals(section.getName(), sectionName)) {
                return section;
            }
        }
        return null;

    }

}

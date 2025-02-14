package services;

import entity.Book;
import entity.Borrow;
import entity.History;
import entity.Section;
import entity.enums.BorrowState;
import entity.enums.SectionState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static db.DataSource.*;

public class UserService {
    public static void service() {

        String message = """
                
                0: back to menu
                1: show sections
                2: show section
                3: borrow books(up to 5)
                4: return book
                5: current borrowed books
                6: history
                
                Choose!
                """;
        while (true) {
            System.out.println(message);
            int choice = intScanner.nextInt();
            switch (choice) {
                case 0 -> {
                    System.out.println("see you never");
                    return;
                }
                case 1 -> {
                    showSections();
                }
                case 2 -> {
                    showSection();
                }
                case 3 -> {
                    borrowBooks();
                }
                case 4 -> {
                    returnBook();
                }
                case 5 -> {
                    currentBorrowedBooks();
                }
                case 6 -> {
                    history();
                }
                default -> System.out.println("invalid choice!");
            }
        }
    }

    private static void history() {
        if (getCurrentUser().getHistories().isEmpty()) {
            System.out.println("You have not borrowed any books yet");
            return;
        }
        int j = 0;
        for (History history : getCurrentUser().getHistories()) {
            System.out.println((++j) + ": " + history);
        }
    }

    private static void currentBorrowedBooks() {
        boolean has = false;
        for (Borrow borrow : getCurrentUser().getBorrowList()) {
            if (Objects.equals(borrow.getBorrowState(), BorrowState.BORROWED)) {
                System.out.println(borrow);
                has = true;
            }
        }
        if (!has) {
            System.out.println("books not borrowed yet");
        }
    }

    private static void returnBook() {
        if (getCurrentUser().getBorrowList().isEmpty()) {
            System.out.println("books hasn't borrowed yet");
            return;
        }

        int k = 0;
        List<Borrow> borrowedBooks = new ArrayList<>();

        boolean has = false;
        for (Borrow borrow : getCurrentUser().getBorrowList()) {
            if (Objects.equals(borrow.getBorrowState(), BorrowState.BORROWED)) {
                borrowedBooks.add(borrow);
                System.out.println((++k) + ": " + borrow.getBook().toString(getCurrentUser()));
                has = true;
            }
        }
        if (!has) {
            System.out.println("books hasn't borrowed yet");
            return;
        }
        System.out.println("choose one of them");
        int choice = intScanner.nextInt();


        if (choice < 1 || choice > borrowedBooks.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Borrow selectedBorrow = borrowedBooks.get(choice - 1);

        selectedBorrow.getBook().setAvailableBook(selectedBorrow.getBook().getAvailableBook() + 1);
        selectedBorrow.setBorrowState(BorrowState.RETURNED);

        getCurrentUser().getBorrowList().remove(selectedBorrow);
        getCurrentUser().setCountBook(getCurrentUser().getCountBook() - 1);

        System.out.println("Book returned successfully.");
    }

    private static void borrowBooks() {
        System.out.println("enter section name: ");
        String sectionName = strScanner.nextLine();

        Section s1 = null;
        for (Section section : sections) {
            if (Objects.equals(section.getStatus(), SectionState.ENABLED)
                    && Objects.equals(section.getName(), sectionName)) {
                s1 = section;
                break;
            }
        }
        if (s1 == null) {
            System.out.println("section not found");
            return;
        }

        for (Book book : s1.getBooks()) {
            System.out.println("Name =>\t " + book.getTitle() + "\nId:\t" + book.getId() +
                    "\nAvailable: " + book.getAvailableBook());
            System.out.println("\n");
        }

        System.out.println("How many books do you want to rent?");
        int n = intScanner.nextInt();
        intScanner.nextLine();
        if (n < 1 || n > 5) {
            System.out.println("you can rent a maximum of 5 books");
            return;
        }

        List<Book> borrowedBooks = new ArrayList<>();
        Map<String, LocalDateTime> borrowedTime = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < n; i++) {
            System.out.println("enter id of book " + (i + 1) + ": ");
            String id = strScanner.nextLine();

            Book selectedBook = null;
            for (Book book : s1.getBooks()) {
                if (Objects.equals(book.getId(), id)) {
                    selectedBook = book;
                    break;
                }
            }
            if (selectedBook == null) {
                System.out.println("invalid id");
                i--;
                continue;
            }
            if (selectedBook.getAvailableBook() == 0) {
                System.out.println("all books in rent");
                i--;
                continue;
            }
            selectedBook.setAvailableBook(selectedBook.getAvailableBook() - 1);
            borrowedBooks.add(selectedBook);

            LocalDateTime time = LocalDateTime.now();
            borrowedTime.put(selectedBook.getId(), time);

            System.out.println("successfully taken " + selectedBook.getTitle() + " at " + time.format(formatter));

            Borrow borrow = new Borrow(UUID.randomUUID().toString(), getCurrentUser(), selectedBook, BorrowState.BORROWED, LocalDateTime.now(), null);
            getCurrentUser().getHistories().add(new History(UUID.randomUUID().toString(), selectedBook, time, null));
            getCurrentUser().getBorrowList().add(borrow);
            getCurrentUser().setCountBook(getCurrentUser().getCountBook() + 1);
        }
        System.out.println("\nyou have rented these books");
        for (int i = 0; i < borrowedBooks.size(); i++) {
            Book book = borrowedBooks.get(i);
            System.out.println((i + 1) + ": " + book.getTitle() + " => borrowed at => "
                    + borrowedTime.get(book.getId()).format(formatter));
        }
    }

    private static void showSection() {
        System.out.println("enter name of section");
        String name = strScanner.nextLine();

        Section section1 = null;
        for (Section section : sections) {
            if (Objects.equals(section.getName(), name) && Objects.equals(section.getStatus(), SectionState.ENABLED)) {
                section1 = section;
                break;
            }
        }
        if (section1 != null) {
            for (Book book : section1.getBooks()) {
                System.out.println(book);
            }
        }
    }

    private static void showSections() {
        boolean has = false;
        for (Section section : sections) {
            if (section.getStatus().equals(SectionState.ENABLED)) {
                has = true;
                System.out.println(section);
            }
        }
        if (!has) {
            System.out.println("this section not exists yet");
        }

    }
}

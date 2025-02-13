package services;

import db.DataSource;
import entity.Borrow;
import entity.History;
import entity.User;
import entity.enums.Role;

import java.util.ArrayList;
import java.util.Objects;

import static db.DataSource.*;

public class AuthService {
    public static void service() {
        System.out.println("Welcome to Library");
        while (true) {
            System.out.println("""
                    0 exit
                    1 sign up
                    2 sign in
                    """);
            switch (intScanner.nextInt()) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    signUp();
                }
                case 2 -> {
                    signIn();
                }
                default -> System.out.println("incorrect option!");
            }
        }


    }

    private static void signUp() {

        System.out.println("Enter name");
        String name = strScanner.nextLine();
        System.out.println("Enter surname");
        String surname = strScanner.nextLine();
        System.out.println("Enter email");
        String email = strScanner.nextLine();
        System.out.println("Enter password");
        String password = strScanner.nextLine();

        for (User user : users) {
            if (Objects.equals(user.getEmail(), email) && Objects.equals(user.getPassword(), password)) {
                System.out.println("This email is already exist!");
                return;
            }
        }

        System.out.println("Enter balance");
        Double balance = doubleScanner.nextDouble();
        User user = new User(Role.USER, name, surname, email, password, new ArrayList<History>(), new ArrayList<Borrow>(), balance);
        users.add(user);
        System.out.println("User successfully added!");
    }

    private static void signIn() {
        System.out.println("Enter email");
        String email = strScanner.nextLine();
        System.out.println("Enter password");
        String password = strScanner.nextLine();

        for (User user : users) {
            if (Objects.equals(user.getEmail(), email) && Objects.equals(user.getPassword(), password)) {
                if (Objects.equals(user.getRole(), Role.ADMIN)) {
                    System.out.println("Admin service");
                    AdminService.service();
                }else if(Objects.equals(user.getRole(), Role.USER)){
                    System.out.println("User service");
                    setCurrentUser(user);
                    UserService.service();
                }
            }
        }

    }

}

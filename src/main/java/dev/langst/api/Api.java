package dev.langst.api;

import dev.langst.entities.Account;
import dev.langst.entities.AccountDAOPostgres;
import dev.langst.entities.Customer;
import dev.langst.entities.CustomerDAOPostgres;
import dev.langst.services.AccountService;
import dev.langst.services.AccountServiceImpl;
import dev.langst.services.CustomerService;
import dev.langst.services.CustomerServiceImpl;
import dev.langst.utilities.List;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Api {
    static Scanner scanner = new Scanner(System.in);
    static CustomerService customerService = new CustomerServiceImpl(new CustomerDAOPostgres());
    static AccountService accountService = new AccountServiceImpl(new AccountDAOPostgres());

    public static void main(String[] args) {

        mainMenu();
    }

    static void mainMenu(){
        String choice;
        boolean showOptions = true;

        System.out.println("Welcome to First World Bank! Please choose an option");
        while(showOptions){
            System.out.println("[1] - Login");
            System.out.println("[2] - Register");
            System.out.println("[3] - Exit");
            choice = scanner.nextLine();

            switch (choice) {
                case "1": {
                    loginMenu();
                    showOptions = false;
                    break;
                }
                case "2": {
                    registerMenu();
                    showOptions = false;
                    break;
                }
                case "3": {
                    showOptions = false;
                    break;
                }
                default: {
                    System.out.println("Invalid input. Please try again.");
                }
            }
        }
    }

    static void loginMenu(){
        boolean showOptions = true;
        String username;
        String password;
        Customer user;

        while(showOptions) {
            System.out.println("Please input your username.");
            username = scanner.nextLine();
            System.out.println("Please input your password.");
            password = scanner.nextLine();

            user = customerService.loginUser(username, password);

            if(user != null){
                userMenu(user);
                showOptions = false;
            }
            else{
                System.out.println("Invalid Credentials!");
                System.out.println("Would you like to exit the program? [Y/N]");

                if(scanner.nextLine().compareToIgnoreCase("Y") == 0){
                    showOptions = false;
                }
            }
        }
    }

    static void registerMenu(){
        String firstName;
        String lastName;
        String username;
        String password;
        boolean invalidPassword = true;

        System.out.println("Thank you for choosing to register for out bank.");

        System.out.println("What is your first name?");
        firstName = scanner.nextLine();

        System.out.println("What is your last name?");
        lastName = scanner.nextLine();

        System.out.println("Please provide a username to login with. (case-sensitive)");
        username = scanner.nextLine();
        while(!customerService.isUniqueUsername(username)) {
            System.out.println("Sorry, that username is already taken. Please try again. (case-sensitive)");
            username = scanner.nextLine();
        }

        do {
            System.out.println("Please enter a password. (case-sensitive)");
            password = scanner.nextLine();
            System.out.println("To validate your password please type it again.");
            if (!password.equals(scanner.nextLine())) {
                System.out.println("That did not match your original password. Please try again.");
            }
            else {
                invalidPassword = false;
            }
        }
        while(invalidPassword);

        System.out.println("\nPlease look over the information you provided.");
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Username: " + username);
        System.out.println("Is this correct? (Y/N)");
    }



    static void userMenu(Customer user){
        String choice;
        boolean showOptions = true;

        System.out.println("Welcome " + user.getFirstName() + "!\n");
        while(showOptions) {
            System.out.println("Please choose an option.");
            System.out.println("[1] View account(s)");
            System.out.println("[2] Open a new account");
            System.out.println("[3] Delete an existing account");
            System.out.println("[4] Exit the program");
            choice = scanner.nextLine();

            switch (choice) {
                case "1": {
                    accountView(user);
                    break;
                }
                case "2": {
                    createAccount(user);
                    break;
                }
                case "3": {
                    deleteAccount(user);
                    break;
                }
                case "4": {
                    showOptions = false;
                    break;
                }
                default: {
                    System.out.println("Invalid input. Please try again.\n");
                }
            }
        }
    }

    static void accountView(Customer user){
        List<Account> accounts;
        boolean showOptions = true;
        String accountChoice;

        accounts = accountService.retrieveAccounts(user.getId());

        if(accounts.size() == 0){
            System.out.println("You currently have no accounts. Please open a checking or savings account.");
            return;
        }

        System.out.println("Here are you current accounts:");
        for(int i = 0; i < accounts.size(); i++){
            Account currentAccount = accounts.get(i);
            System.out.println("[" + currentAccount.getAccountId() + "] " +
                    currentAccount.getAccountType() + " " + currentAccount.getBalance());
        }

        while(showOptions){
            System.out.println("Please choose an account by typing in the account's ID");
            accountChoice = scanner.nextLine();
        }
    }

    static void createAccount(Customer user){

    }

    static void deleteAccount(Customer user){

    }
}
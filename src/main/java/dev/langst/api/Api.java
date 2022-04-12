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
                    break;
                }
                case "2": {
                    registerMenu();
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
                System.out.println("Would you like to return to the previous menu? [Y/N]");

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
        boolean showOptions = true;
        String choice;

        System.out.println("Thank you for choosing to register for out bank.");

        System.out.println("What is your first name?");
        firstName = scanner.nextLine();

        System.out.println("What is your last name?");
        lastName = scanner.nextLine();

        System.out.println("Please provide a username to login with. (case-sensitive)");
        username = scanner.nextLine();
        while (!customerService.isUniqueUsername(username)) {
            System.out.println("Sorry, that username is already taken. Please try again. (case-sensitive)");
            username = scanner.nextLine();
        }

        do {
            System.out.println("Please enter a password. (case-sensitive)");
            password = scanner.nextLine();
            System.out.println("To validate your password please type it again.");
            if (!password.equals(scanner.nextLine())) {
                System.out.println("That did not match your original password. Please try again.");
            } else {
                invalidPassword = false;
            }
        }
        while (invalidPassword);

        System.out.println("\nPlease look over the information you provided.");
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Username: " + username);
        System.out.println("Is this correct? (Y/N)");
        choice = scanner.nextLine();

        if (choice.compareToIgnoreCase("Y") == 0) {
            Customer customer = new Customer(0, firstName, lastName, username, password);

            customerService.registerUser(customer);

            System.out.println("You are now able to login to your user account.\n");
        }
        else {
            System.out.println("Please try making your account again.\n");
        }
    }



    static void userMenu(Customer user){
        String choice;
        boolean showOptions = true;

        System.out.println("Welcome " + user.getFirstName() + "!\n");
        while(showOptions) {
            System.out.println("Please choose an option.");
            System.out.println("[1] View account(s)");
            System.out.println("[2] Open a new account");
            System.out.println("[3] Delete User Account");
            System.out.println("[4] Logout");
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
                    Boolean deleted = deleteUser(user);
                    if(deleted){
                        showOptions = false;
                    }
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
        Account chosenAccount = null;
        boolean showOptions = true;
        String accountChoice;

        accounts = accountService.retrieveAccounts(user.getId());

        if(accounts.size() == 0){
            System.out.println("You currently have no accounts. Please open a checking or savings account.\n");
            return;
        }

        while(showOptions){
            System.out.println("Here are you current accounts:");

            for(int i = 0; i < accounts.size(); i++){
                Account currentAccount = accounts.get(i);
                System.out.println(currentAccount);
            }

            System.out.println("Please choose an account by typing in the account's ID");
            accountChoice = scanner.nextLine();

            for(int i = 0; i < accounts.size(); i++){
                try {
                    if (Integer.parseInt(accountChoice) == accounts.get(i).getAccountId()) {
                        chosenAccount = accounts.get(i);
                        break;
                    }
                }
                catch (NumberFormatException e){
                    break;
                }
            }

            if(chosenAccount == null){
                System.out.println("Invalid account choice.\n");
            }
            else{
                accountOptions(chosenAccount);
                showOptions = false;
            }
        }
    }

    static void createAccount(Customer user) {
        String choice;

        System.out.println("What kind of account would you like to create? Choose which option");
        System.out.println("[1] Checking");
        System.out.println("[2] Savings");
        System.out.println("[3] Cancel");
        choice = scanner.nextLine();

        switch (choice) {
            case "1": {
                Account newAccount = accountService.openAccount(new Account(0,
                        user.getId(), "Checking", 0.00));
                System.out.println("A checking account has successfully been added.");
                break;
            }
            case "2": {
                Account newAccount = accountService.openAccount(new Account(0,
                        user.getId(), "Savings", 0.00));
                System.out.println("A savings account has successfully been added.");
                break;
            }
            case "3": {
                return;
            }
            default: {
                System.out.println("Invalid input. Please try again.\n");
            }
        }
    }

    static boolean deleteUser(Customer user){
        double totalBalance = 0;
        boolean deleted;
        String choice;

        System.out.println("Are you sure you want to delete your user account and" +
                " close your current bank accounts? (Y/N)");
        choice = scanner.nextLine();
        if(choice.compareToIgnoreCase("Y") != 0){
            return false;
        }

        System.out.println("One moment while we close your bank accounts before deleting your user accounts.");

        List<Account> accounts = accountService.retrieveAccounts(user.getId());
        for(int i = 0; i < accounts.size(); i++){
            totalBalance = accounts.get(i).getBalance();
            accountService.closeAccount(accounts.get(i));
        }

        deleted = customerService.deleteUser(user);
        if(deleted){
            System.out.println("The amount of: " + totalBalance + " will be mailed to your home address.");
            System.out.println("You account has successfully been deleted.\n");
            return true;
        }
        else{
            System.out.println("There was an error in deleting your account. Please try again.");
            return false;
        }
    }

    static void accountOptions(Account account){
        String choice;
        Boolean showOptions = true;
        Double difference;

        while(showOptions) {
            System.out.println(account);
            System.out.println("What would you like to do with this account?");
            System.out.println("[1] Withdraw");
            System.out.println("[2] Deposit");
            System.out.println("[3] View Transaction History");
            System.out.println("[4] Delete Account");
            System.out.println("[5] Exit");
            choice = scanner.nextLine();

            switch (choice) {
                //Withdraw Option
                case "1": {
                    Boolean validInput = false;

                    while(!validInput) {
                        System.out.println(account);
                        System.out.println("How much would you like to withdraw?");
                        String amount = scanner.nextLine();

                        try{
                            difference = Double.parseDouble(amount);

                            if(difference > account.getBalance()){
                                System.out.println("You may not overdraft your account. Please try again.\n");
                                break;
                            }
                            else if(difference >= 0.00){
                                accountService.adjustBalance(account, -difference);
                                validInput = true;
                            }
                            else{
                                System.out.println("Not a valid input. Please enter " +
                                        "a non-negative number in \"xx.xx\" format.\n");
                            }
                        }
                        catch (NumberFormatException e){

                        }
                    }

                    break;
                }

                //Deposit Option
                case "2": {
                    Boolean validInput = false;

                    while(!validInput) {
                        System.out.println(account);
                        System.out.println("How much would you like to deposit?");
                        String amount = scanner.nextLine();

                        try{
                            difference = Double.parseDouble(amount);

                            if(difference >= 0.00){
                                accountService.adjustBalance(account, difference);
                                validInput = true;
                            }
                            else{
                                System.out.println("Not a valid input. Please enter " +
                                        "a non-negative number in \"xx.xx\" format.\n");
                            }
                        }
                        catch (NumberFormatException e){
                            System.out.println("Not a valid input. Please enter " +
                                    "a non-negative number in \"xx.xx\" format.\n");
                        }
                    }
                    break;
                }

                case "3": {
                    System.out.println("Here is a history of this account's previous transactions.");
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
}
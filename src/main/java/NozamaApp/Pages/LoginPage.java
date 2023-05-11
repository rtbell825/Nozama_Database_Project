package NozamaApp.Pages;

import Controller.ClientController;
import Controller.UserController;
import Entity.Client;
import Entity.Supplier;
import Entity.User;

import java.sql.SQLException;
import java.util.Scanner;

public class LoginPage extends Page{
    @Override
    public void printPage(String username) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        printHeader();
        System.out.println("1: Login");
        System.out.println("2: Register");
        boolean isCorrect = false;
        while(!isCorrect){
            System.out.print("Enter input: ");
            String loginInput = scanner.nextLine();
            if(loginInput.equals("1")){
                login();
                isCorrect = true;
            }
            else if (loginInput.equals("2")) {
                register();
                isCorrect = true;
            }
            else {
                printHeader();
                System.out.println("Wrong input. Try again");
                isCorrect = false;
            }
        }
    }
    public void login() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String username;
        boolean isCorrect;
        User user;
        do{
            printHeader();
            System.out.println("Login Page");
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            user = UserController.getUser(username);
            if(user == null){
                isCorrect = false;
                System.out.println("User does not exist, try again.");
            }
            else{
                isCorrect = true;
            }
        }while (!isCorrect);
        do{
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            if(!user.getPassword().equals(password)){
                isCorrect = false;
                System.out.println("Incorrect password, try again.");
            }
            else{
                isCorrect = true;
            }
        }while (!isCorrect);
        ProductPage p = new ProductPage();
        p.printPage(username);
    }
    public void register() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean isAvailable;
        User user;
        String username;
        do{
            printHeader();
            System.out.println("Registration Page");

            System.out.print("Enter username: ");
            username = scanner.nextLine();
            user = UserController.getUser(username);
            if(user != null){
                isAvailable = false;
                System.out.println("Username already exists, try again.");
            }
            else{
                isAvailable = true;
            }
        }while (!isAvailable);
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter bank name: ");
        String bankName = scanner.nextLine();
        System.out.print("Enter bank number: ");
        String bankNumber = scanner.nextLine();
        System.out.print("Would you like to be a supplier?(Y/N): ");
        String isSupplierStr = scanner.nextLine();
        boolean isSupplier = false;
        if(isSupplierStr.equals("Y")){
            isSupplier = true;
        }
        if(isSupplier){
            Supplier supplier = new Supplier(lastName, firstName, username, password, bankName, bankNumber, 0.0);
            UserController.addUser(supplier);
        }
        else{
            Client client = new Client(lastName, firstName, username,password, bankName, bankNumber, false);
            UserController.addUser(client);
        }
        ProductPage p = new ProductPage();
        p.printPage(username);
    }
}

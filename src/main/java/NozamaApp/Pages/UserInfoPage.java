package NozamaApp.Pages;

import Controller.ClientController;
import Controller.UserController;
import Entity.Client;
import Entity.User;

import java.sql.SQLException;
import java.util.Scanner;

public class UserInfoPage extends Page{

    private boolean isAdmin = false;
    private String adminUser = "";

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
        isAdmin=true;
    }

    public void printPage(String username) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean hasExited = false;
        String command;
        do{
            printHeader();
            User user = UserController.getUser(username);
            System.out.println(user);
            System.out.print("\nEnter command('help' for command list): ");
            command = scanner.nextLine();
            hasExited = executeCommand(command, user);
        }while (!hasExited);
    }
    public boolean executeCommand(String command, User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        String[] parts = command.split(" ");
        if(parts[0].equals("navigate")){
            if(parts[1].equals("1") && !isAdmin){
                ProductPage p = new ProductPage();
                p.printPage(user.getUsername());
                return true;
            } else if(parts[1].equals("2") && !isAdmin){
                ShoppingCartPage p = new ShoppingCartPage();
                p.printPage(user.getUsername());
                return true;
            } else if(parts[1].equals("3") && !isAdmin){
                PreviousPurchasePage p = new PreviousPurchasePage();
                p.printPage(user.getUsername());
                return true;
            } else if(parts[1].equals("4") && !isAdmin){
                UserInfoPage p = new UserInfoPage();
                p.printPage(user.getUsername());
                return true;
            }  else if(parts[1].equals("0") && isAdmin){
                AdminPage p = new AdminPage();
                p.printPage(adminUser);
                return true;
            } else{
                System.out.println("Invalid input. Try again");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, user);
            }
        } else if (parts[0].equals("help")) {
            printCommands();
            scanner.nextLine();
            return false;
        }  else if (parts[0].equals("changepass")) {
            System.out.print("Change Password\nEnter old password: ");
            String enteredPass = scanner.nextLine();
            while(!enteredPass.equals(user.getPassword())){
                System.out.print("Wrong password. Try again\nEnter old password: ");
                enteredPass = scanner.nextLine();
            }
            System.out.print("Enter new password: ");
            enteredPass = scanner.nextLine();
            UserController.changePass(user.getUsername(), enteredPass);
            System.out.println("Password updated");
            return false;
        } else if (parts[0].equals("changename")) {
            System.out.print("Change Name\nEnter password: ");
            String enteredPass = scanner.nextLine();
            while (!enteredPass.equals(user.getPassword())) {
                System.out.print("Wrong password. Try again\nEnter password: ");
                enteredPass = scanner.nextLine();
            }
            System.out.print("Enter new First Name: ");
            String enteredFirst = scanner.nextLine();
            System.out.print("Enter new Last Name: ");
            String enteredLast = scanner.nextLine();
            UserController.changeName(user.getUsername(), enteredFirst, enteredLast);
            System.out.println("Name updated");
            return false;
        } else if (parts[0].equals("changebankinfo")) {
            System.out.print("Change Bank Information\nEnter password: ");
            String enteredPass = scanner.nextLine();
            while (!enteredPass.equals(user.getPassword())) {
                System.out.print("Wrong password. Try again\nEnter password: ");
                enteredPass = scanner.nextLine();
            }
            System.out.print("Enter new Bank Name: ");
            String enteredName = scanner.nextLine();
            System.out.print("Enter new Bank Account Number: ");
            String enteredNumber = scanner.nextLine();
            ClientController.changeBankInfo(user.getUsername(), enteredName, enteredNumber);
            System.out.println("Bank Information updated");
            return false;
        } else if(parts[0].equals("makesupplier") && isAdmin){
            if (user.isAdmin()){
                System.out.println("User is an administrator");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, user);
            }
            Client client = ClientController.getClient(user.getUsername());
            if(client.isSupplier()){
                System.out.println("User is already a supplier");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, user);
            }
            ClientController.makeSupplier(client);
            System.out.println("User is now a supplier");
            return false;
        }else{
            System.out.println("Invalid input. Try again");
            System.out.print("Enter command('help' for command list): ");
            command = scanner.nextLine();
            return executeCommand(command, user);
        }
    }

    @Override
    public void printHeader() {
        super.printHeader();
        System.out.println("User Info Page\n");
    }
    public void printCommands(){
        printHeader();
        System.out.println("Command list\n");
        System.out.println("- 'navigate <page_num>'                             Navigates to page page_num.");
        System.out.println("    Page numbers:");
        if(isAdmin) {
            System.out.println("        0: Back to Admin Page");
        }
        else {
            System.out.println("        1: Main Product Page");
            System.out.println("        2: Shopping Cart Page");
            System.out.println("        3: Previous Purchases Page");
            System.out.println("        4: User Info Page");
        }
        System.out.println("- 'changepass'                                      Change your password.");
        System.out.println("- 'changename'                                      Change your first and/or last name.");
        System.out.println("- 'changebankinfo'                                  Change your bank information.");
        if (isAdmin){
            System.out.println("- 'makesupplier'                                    Make this user a Supplier.");
        }
        System.out.println("Press enter to quit.");
    }
}

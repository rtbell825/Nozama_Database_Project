package NozamaApp.Pages;

import Controller.*;
import Entity.Admin;
import Entity.Client;
import Entity.User;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AdminPage extends Page{
    public void printPage(String username) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean hasExited = false;
        String command;
        do{
            Admin user = AdminController.getAdmin(username);
            printCommands();
            System.out.print("\nEnter command('help' for command list): ");
            command = scanner.nextLine();
            hasExited = executeCommand(command, user);
        }while (!hasExited);
    }
    public boolean executeCommand(String command, User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        String[] parts = command.split(" ");
        if(parts[0].equals("navigate")){
            if(parts[1].equals("1")){
                ProductPage p = new ProductPage();
                p.printPage(user.getUsername());
                return true;
            } else if(parts[1].equals("2")){
                ShoppingCartPage p = new ShoppingCartPage();
                p.printPage(user.getUsername());
                return true;
            } else if(parts[1].equals("3")){
                PreviousPurchasePage p = new PreviousPurchasePage();
                p.printPage(user.getUsername());
                return true;
            } else if(parts[1].equals("4")){
                UserInfoPage p = new UserInfoPage();
                p.printPage(user.getUsername());
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
        }  else if (parts[0].equals("systeminfo")) {
            double totalCount = ProductController.getTotalProductCount();
            double totalSold = PurchaseController.getTotalProductsSoldCount();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            LocalDateTime date1 = LocalDateTime.parse("03-04-2020 00:00", dtf);
            LocalDateTime date2 = LocalDateTime.now();
            long daysPassed = Duration.between(date1, date2).toDays();
            long monthsPassed = (Duration.between(date1, date2).toDays())/30;
            long yearsPassed = (Duration.between(date1, date2).toDays())/365;
            System.out.println("Number of clients: "+ClientController.getClientCount());
            System.out.println("Number of suppliers: "+ SupplierController.getSupplierCount());
            System.out.println("Number of unique products: "+ ProductController.getProductCount());
            System.out.println("Number of total products: "+ (int)totalCount);
            System.out.println("Average number of products sold per day since 04/03/2020: "+ Math.round(totalSold/daysPassed));
            System.out.println("Average number of products sold per month since 04/03/2020: "+ Math.round(totalSold/monthsPassed));
            System.out.println("Average number of products sold per year since 04/03/2020: "+ Math.round(totalSold/yearsPassed));
            System.out.println("Average number of products added per day since 04/03/2020: "+ Math.round(totalCount/daysPassed));
            System.out.println("Average number of products added per month since 04/03/2020: "+ Math.round(totalCount/monthsPassed));
            System.out.println("Average number of products added per year since 04/03/2020: "+ Math.round(totalCount/yearsPassed));
            System.out.println("Press enter to continue.");
            scanner.nextLine();
            return false;
        }  else if (parts[0].equals("viewsupplier")) {
            User u = UserController.getUser(parts[1]);
            if (u==null){
                System.out.println("User not found. Try again");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, user);
            }
            if (u.isAdmin()){
                System.out.println("User is an administrator");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, user);
            }
            Client client = ClientController.getClient(u.getUsername());
            if(!client.isSupplier()){
                System.out.println("User is not a supplier");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, user);
            }
            SupplierPage p = new SupplierPage();
            p.setAdminUser(user.getUsername());
            p.printPage(u.getUsername());
            return true;
        }  else if (parts[0].equals("userinfo")) {
            User u = UserController.getUser(parts[1]);
            if (u==null){
                System.out.println("User not found. Try again");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, user);
            }
            UserInfoPage p = new UserInfoPage();
            p.setAdminUser(user.getUsername());
            p.printPage(u.getUsername());
            return true;
        }  else if (parts[0].equals("viewcart")) {
            User u = UserController.getUser(parts[1]);
            if (u==null){
                System.out.println("User not found. Try again");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, user);
            }
            ShoppingCartPage p = new ShoppingCartPage();
            p.setAdminUser(user.getUsername());
            p.printPage(u.getUsername());
            return true;
        } else{
            System.out.println("Invalid input. Try again");
            System.out.print("Enter command('help' for command list): ");
            command = scanner.nextLine();
            return executeCommand(command, user);
        }
    }

    @Override
    public void printHeader() {
        super.printHeader();
        System.out.println("Admin Page\n");
    }
    public void printCommands(){
        printHeader();
        System.out.println("Command list\n");
        System.out.println("- 'navigate <page_num>'                             Navigates to page page_num.");
        System.out.println("    Page numbers:");
        System.out.println("        1: Main Product Page");
        System.out.println("        2: Shopping Cart Page");
        System.out.println("        3: Previous Purchases Page");
        System.out.println("        4: User Info Page");
        System.out.println("- 'systeminfo'                                      Show system information.");
        System.out.println("- 'viewsupplier <username>'                         Open that user's supplier page.");
        System.out.println("- 'userinfo <username>'                             Open that user's information page.");
        System.out.println("- 'viewcart <username>'                             Open that user's shopping cart.");
    }
}

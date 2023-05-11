package NozamaApp.Pages;

import Controller.ProductController;
import Controller.PurchaseController;
import Controller.SupplierController;
import Controller.TransactionController;
import Entity.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class SupplierPage extends Page{
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
            Supplier user = SupplierController.getSupplier(username);
            List<Product> productList = ProductController.getSupplierProducts(username);
            if(productList.size()==0){
                System.out.println("There are no products");
            }
            else{
                for (Product p:productList) {
                    System.out.println(p+"Quantity:"+p.getQuantity()+"\n");
                }
            }
            System.out.print("\nEnter command('help' for command list): ");
            command = scanner.nextLine();
            hasExited = executeCommand(command, user, productList);
        }while (!hasExited);
    }
    public boolean executeCommand(String command, User user, List<Product> productList) throws SQLException { //ACA
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
                return executeCommand(command, user, productList);
            }
        } else if (parts[0].equals("help")) {
            printCommands();
            scanner.nextLine();
            return false;
        }  else if (parts[0].equals("view")) {
            if(productList.size()==0){
                System.out.println("There are no products. Try again");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, user, productList);
            }
            for (Product product:productList) {
                if(product.getPageId() == Integer.parseInt(parts[1])){
                    List<Purchase> purchaseList = PurchaseController.getProductPurchases(product.getProductId());
                    if(purchaseList.size()==0){
                        System.out.println("No purchases");
                    }
                    for (Purchase purchase:purchaseList) {
                        Transaction transaction = TransactionController.getTransaction(purchase.getTransactionId());
                        System.out.println(transaction.supplierToString()+"\nQuantity: "+purchase.getQuantity()+"\n");
                    }
                    System.out.println("Press enter to quit.");
                    scanner.nextLine();
                    return false;
                }
            }
            System.out.println("Incorrect product id. Try again");
            System.out.print("Enter command('help' for command list): ");
            command = scanner.nextLine();
            return executeCommand(command, user, productList);
        } else if (parts[0].equals("add")) {
            System.out.print("Enter name: ");
            String description = scanner.nextLine();
            System.out.print("Enter price: ");
            String priceStr = scanner.nextLine();
            double price = Double.valueOf(priceStr);
            System.out.print("Enter stock quantity: ");
            String quantityStr = scanner.nextLine();
            int quantity = Integer.parseInt(quantityStr);
            Product product = new Product(user.getUsername(), price, quantity, 0.0, description, -1);
            ProductController.addProduct(product);
            return false;
        } else{
            System.out.println("Invalid input. Try again");
            System.out.print("Enter command('help' for command list): ");
            command = scanner.nextLine();
            return executeCommand(command, user, productList);
        }
    }

    @Override
    public void printHeader() {
        super.printHeader();
        System.out.println("Supplier Page\n");
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
        System.out.println("- 'view <product_id>'                               Open that product's purchase history");
        System.out.println("- 'add'                                             Add a new product.");
        System.out.println("Press enter to quit.");
    }
}

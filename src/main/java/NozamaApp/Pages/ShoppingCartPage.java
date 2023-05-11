package NozamaApp.Pages;

import Controller.ClientController;
import Controller.ProductController;
import Controller.PurchaseController;
import Controller.TransactionController;
import Entity.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ShoppingCartPage extends Page{
    private boolean isEmpty = true;
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
            Transaction transaction = TransactionController.getShoppingCart(username);
            List<Purchase> purchaseList = null;
            if(transaction != null){
                isEmpty=false;
                purchaseList = PurchaseController.getPurchases(username, transaction.getTransactionId());
                for (int i = 1; i< purchaseList.size()+1; i++) {
                    Purchase purchase = purchaseList.get(i-1);
                    Product product = ProductController.getProduct(purchase.getProductId());
                    product.setPageId(i);
                    System.out.println(i+".");
                    System.out.print(purchase.getQuantity()+" ");
                    System.out.println(product.cartToString()+"\n");
                }
            } else{
                isEmpty=true;
                System.out.println("Shopping Cart is empty");
            }
            System.out.print("\nEnter command('help' for command list): ");
            command = scanner.nextLine();
            hasExited = executeCommand(command, username, purchaseList);
        }while (!hasExited);
    }
    public boolean executeCommand(String command, String username, List<Purchase> purchaseList) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        String[] parts = command.split(" ");
        if(parts[0].equals("navigate")){
            if(parts[1].equals("1") && !isAdmin){
                ProductPage p = new ProductPage();
                p.printPage(username);
                return true;
            } else if(parts[1].equals("2") && !isAdmin){
                ShoppingCartPage p = new ShoppingCartPage();
                p.printPage(username);
                return true;
            } else if(parts[1].equals("3") && !isAdmin){
                PreviousPurchasePage p = new PreviousPurchasePage();
                p.printPage(username);
                return true;
            } else if(parts[1].equals("4") && !isAdmin){
                UserInfoPage p = new UserInfoPage();
                p.printPage(username);
                return true;
            }  else if(parts[1].equals("0") && isAdmin){
                AdminPage p = new AdminPage();
                p.printPage(adminUser);
                return true;
            } else{
                System.out.println("Invalid input. Try again");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, username, purchaseList);
            }
        } else if (parts[0].equals("help")) {
            printCommands();
            scanner.nextLine();
            return false;
        } else if (parts[0].equals("remove")) {
            if(!isEmpty){
                int purchaseId = Integer.parseInt(parts[1]);
                if(purchaseId>purchaseList.size()){
                    System.out.println("Incorrect product id. Try again");
                    System.out.print("Enter command('help' for command list): ");
                    command = scanner.nextLine();
                    return executeCommand(command, username, purchaseList);
                } else{
                    if(purchaseList.size()==1){
                        TransactionController.deleteTransaction(purchaseList.get(purchaseId-1).getTransactionId());
                    }
                    else{
                        PurchaseController.deletePurchase(purchaseList.get(purchaseId-1));
                    }
                }
            }
            else{
                System.out.println("Shopping cart already empty");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, username, purchaseList);
            }
            return false;
        } else if (parts[0].equals("quantity")) {
            if(!isEmpty) {
                int purchaseId = Integer.parseInt(parts[1]);
                int newQuantity = Integer.parseInt(parts[2]);
                if (purchaseId > purchaseList.size()) {
                    System.out.println("Incorrect product id. Try again");
                    System.out.print("Enter command('help' for command list): ");
                    command = scanner.nextLine();
                    return executeCommand(command, username, purchaseList);
                } else {
                    if (newQuantity == 0) {
                        System.out.println("Please use remove command instead");
                        System.out.print("Enter command('help' for command list): ");
                        command = scanner.nextLine();
                        return executeCommand(command, username, purchaseList);
                    } else {
                        purchaseList.get(purchaseId - 1).setQuantity(newQuantity);
                        PurchaseController.changeQuantity(purchaseList.get(purchaseId - 1));
                    }
                }
                return false;
            }
            else{
                System.out.println("Shopping cart is empty");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, username, purchaseList);
            }
        } else if (parts[0].equals("checkout")) {
            if(!isEmpty){
                int i = 1;
                for (Purchase purchase: purchaseList) {
                    Product product = ProductController.getProduct(purchase.getProductId());
                    if(product.getQuantity()< purchase.getQuantity()){
                        System.out.println("Product "+i+" has fewer than "+purchase.getQuantity()+" item(s) in stock. Try again");
                        System.out.print("Enter command('help' for command list): ");
                        command = scanner.nextLine();
                        return executeCommand(command, username, purchaseList);
                    }
                    i++;
                }
                System.out.print("Bank account registered. Bank: ");
                Client user = ClientController.getClient(username);
                System.out.println(user.getBankName() + ". Account number: "+user.getBankNumber());
                System.out.println("Would you like to use this account?(Y/N): ");
                String useAcc = scanner.nextLine();
                while(!useAcc.equals("Y") && !useAcc.equals("N")){
                    System.out.print("Incorrect input. Try again: ");
                    useAcc = scanner.nextLine();
                }
                InvoicePage p = new InvoicePage();
                CreditCard creditCard = null;
                if(useAcc.equals("N")){
                    System.out.print("Enter name on card: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter card number: ");
                    String number = scanner.nextLine();
                    System.out.print("Enter expiration date(YYYY-MM): ");
                    String expDateString = scanner.nextLine();
                    Date expDate;
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                    while(true) {
                        try{
                            expDate = formatter.parse(expDateString+"-01");
                            break;
                        }
                        catch(ParseException e){
                            System.out.println("Not a valid input. Try again");
                            System.out.print("Enter expiration date(YYYY-MM): ");
                            expDateString = scanner.nextLine();
                        }
                    }
                    System.out.print("Enter security code: ");
                    String securityCodeString = scanner.nextLine();
                    while(securityCodeString.length()!=3){
                        System.out.println("Not a valid input. Try again");
                        securityCodeString = scanner.nextLine();
                    }
                    int securityCode = Integer.parseInt(securityCodeString);
                    creditCard = new CreditCard(name, number, expDate, securityCode);
                }
                System.out.print("Enter shipping address: ");
                String address = scanner.nextLine();
                System.out.print("Enter city: ");
                String city = scanner.nextLine();
                System.out.print("Enter state: ");
                String state = scanner.nextLine();
                System.out.print("Enter zip: ");
                String zip = scanner.nextLine();
                System.out.print("Enter any specific instructions for delivery: ");
                String deliveryInstructions = scanner.nextLine();
                Transaction transaction = TransactionController.getShoppingCart(username);
                Transaction completedTransaction = new Transaction(transaction.getTransactionId(), Transaction.Status.ORDER_SUBMITTED, Date.from(Instant.now()), transaction.getTotal(), transaction.getSubTotal(), transaction.getTax(), address, city, state, zip, deliveryInstructions);
                TransactionController.updateTransaction(completedTransaction);
                for (Purchase purchase:purchaseList) {
                    ProductController.buyProduct(purchase.getProductId(), purchase.getQuantity());
                }
                p.setCard(creditCard);
                p.setAccount(user.getBankNumber());
                p.setTransactionId(transaction.getTransactionId());
                p.printPage(username);
                return true;
            }
            else{
                System.out.println("Cannot checkout with an empty cart");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, username, purchaseList);
            }
        } else if (parts[0].equals("clear")) {
            if(!isEmpty){
                TransactionController.deleteTransaction(purchaseList.get(0).getTransactionId());
            }
            else{
                System.out.println("Shopping cart already empty");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, username, purchaseList);
            }
            return false;
        } else{
            System.out.println("Invalid input. Try again");
            System.out.print("Enter command('help' for command list): ");
            command = scanner.nextLine();
            return executeCommand(command, username, purchaseList);
        }
    }

    @Override
    public void printHeader() {
        super.printHeader();
        System.out.println("Shopping Cart Page\n");
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
        System.out.println("- 'remove <product_id>'                             Removes that product from the cart");
        System.out.println("- 'quantity <product_id> x'                         Changes that product's quantity to x");
        System.out.println("- 'checkout'                                        Proceed to payment");
        System.out.println("- 'clear'                                           Clears the cart");
        System.out.println("Press enter to quit.");
    }
}

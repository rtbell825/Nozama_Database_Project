package NozamaApp.Pages;

import Controller.ProductController;
import Controller.PurchaseController;
import Controller.TransactionController;
import Entity.CreditCard;
import Entity.Product;
import Entity.Purchase;
import Entity.Transaction;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class InvoicePage extends Page{
    private CreditCard card = null;
    private String account = "";
    private int transactionId;

    public void printPage(String username) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean hasExited = false;
        String command;
        do{
            printHeader();
            Transaction transaction = TransactionController.getTransaction(transactionId);
            System.out.println(transaction);
            System.out.println("Payment method: ");
            if(card!=null){
                System.out.println(card);
            }
            else {
                System.out.println("Bank account ending in "+account.substring(15));
            }
            List<Purchase> purchaseList = PurchaseController.getPurchases(username, transactionId);
            for (Purchase purchase: purchaseList) {
                Product product = ProductController.getProduct(purchase.getProductId());
                System.out.print(purchase);
                System.out.println(product.purchasedToString());
            }
            System.out.print("\nEnter command('help' for command list): ");
            command = scanner.nextLine();
            hasExited = executeCommand(command, username);
        }while (!hasExited);
    }
    public boolean executeCommand(String command, String username) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        String[] parts = command.split(" ");
        if(parts[0].equals("navigate")){
            if(parts[1].equals("1")){
                ProductPage p = new ProductPage();
                p.printPage(username);
                return true;
            } else if(parts[1].equals("2")){
                ShoppingCartPage p = new ShoppingCartPage();
                p.printPage(username);
                return true;
            } else if(parts[1].equals("3")){
                PreviousPurchasePage p = new PreviousPurchasePage();
                p.printPage(username);
                return true;
            } else if(parts[1].equals("4")){
                UserInfoPage p = new UserInfoPage();
                p.printPage(username);
                return true;
            } else{
                System.out.println("Invalid input. Try again");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, username);
            }
        } else if (parts[0].equals("help")) {
            printCommands();
            scanner.nextLine();
            return false;
        } else{
            System.out.println("Invalid input. Try again");
            System.out.print("Enter command('help' for command list): ");
            command = scanner.nextLine();
            return executeCommand(command, username);
        }
    }

    @Override
    public void printHeader() {
        super.printHeader();
        System.out.println("Invoice Page\n");
    }
    public void printCommands(){
        printHeader();
        System.out.println("Command list\n");
        System.out.println("- 'navigate <page_num>'                             Navigates to page page_num.");
        System.out.println("    Page numbers:");
        System.out.println("        1: Main Product Page");
        System.out.println("        2: Shopping Cart Page(now empty)");
        System.out.println("        3: Previous Purchases Page");
        System.out.println("        4: User Info Page");
        System.out.println("Press enter to quit.");
    }

    public CreditCard getCard() {
        return card;
    }

    public void setCard(CreditCard card) {
        this.card = card;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}

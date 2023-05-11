package NozamaApp.Pages;

import Controller.*;
import Entity.*;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductPage extends Page{
    private List<Product> productList;
    private Sort order;
    public enum Sort{
        lowprices, highprices, lowratings, highratings, alpha, clear
    }
    private double priceGreater= -1;
    private double priceLower= -1;
    private double ratingsGreater= -1;
    private double ratingsLower= -1;
    private int curPageNum = 1;
    private boolean isSupplier = false;
    private boolean isAdmin = false;

    @Override
    public void printPage(String username) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean hasExited = false;
        String command;
        User user = UserController.getUser(username);
        if (user.isAdmin()){
            isAdmin = user.isAdmin();
        }
        else{
            Client client = ClientController.getClient(username);
            isSupplier=client.isSupplier();
        }
        do{
            printHeader();
            productList = ProductController.getProducts(priceGreater, priceLower, ratingsGreater, ratingsLower, order, curPageNum);
            if(productList.size() == 0){
                curPageNum--;
                productList = ProductController.getProducts(priceGreater, priceLower, ratingsGreater, ratingsLower, order, curPageNum);
            }
            for (Product p:productList) {
                System.out.println(p.toString());
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
                System.out.println("Already on Main Product Page");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, username);
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
            } else if(parts[1].equals("5") && isSupplier){
                SupplierPage p = new SupplierPage();
                p.printPage(username);
                return true;
            } else if(parts[1].equals("0") && isAdmin){
                AdminPage p = new AdminPage();
                p.printPage(username);
                return true;
            }else{
                System.out.println("Invalid input. Try again");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, username);
            }
        } else if (parts[0].equals("next")) {
            curPageNum++;
            return false;
        } else if (parts[0].equals("previous")) {
            if(curPageNum > 1){
                curPageNum--;
                return false;
            }
            else{
                System.out.println("You are already at the first page. Try again");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, username);
            }
        } else if (parts[0].equals("view")) {
            for (Product p:productList) {
                if(String.valueOf(p.getPageId()).equals(parts[1])){
                    System.out.print(p.viewToString());
                    Supplier supplier = SupplierController.getSupplier(p.getSellerUsername());
                    System.out.println("\tRating:"+supplier.getRating());
                    System.out.println("Press enter to quit.");
                    command = scanner.nextLine();
                    return false;
                }
            }
            return false;
        } else if (parts[0].equals("checkout")) {
            for (Product p:productList) {
                if(String.valueOf(p.getPageId()).equals(parts[1])){
                    int quantity = Integer.parseInt(parts[2]);
                    if(quantity>p.getQuantity()){
                        System.out.println("Product "+p.getPageId()+" has fewer than "+quantity+" item(s) in stock. Try again");
                        System.out.print("Enter command('help' for command list): ");
                        command = scanner.nextLine();
                        return executeCommand(command, username);
                    }
                    Transaction transaction = TransactionController.getShoppingCart(username);
                    if(transaction==null){
                        transaction = new Transaction(Transaction.Status.IN_CART, Date.from(Instant.now()), 0);
                        transaction = TransactionController.addTransaction(transaction);
                    }
                    List<Purchase> purchaseList = PurchaseController.getPurchases(username, transaction.getTransactionId());
                    for (Purchase purchase:purchaseList) {
                        if(purchase.getProductId()==p.getProductId()){
                            purchase.setQuantity(purchase.getQuantity()+quantity);
                            PurchaseController.changeQuantity(purchase);
                            System.out.println("Item "+p.getPageId()+" added to shopping cart");
                            return false;
                        }
                    }
                    Purchase purchase = new Purchase(transaction.getTransactionId(), p.getProductId(), username, quantity);
                    PurchaseController.addPurchase(purchase);
                    System.out.println("Item "+p.getPageId()+" added to shopping cart");
                    return false;
                }
            }
            return false;
        } else if (parts[0].equals("sortby")) {
            try {
                order = Sort.valueOf(parts[1]);
                curPageNum = 1;
                return false;
            }
            catch(Exception e) {
                System.out.println("Invalid input. Try again");
                System.out.print("Enter command('help' for command list): ");
                command = scanner.nextLine();
                return executeCommand(command, username);
            }
        } else if (parts[0].equals("filterby")) {
            for (int i = 1; i<parts.length; i++) {
                Pattern priceGreaterPattern = Pattern.compile("price>[0-9]*\\.[0-9]+");
                Pattern priceLowerPattern = Pattern.compile("price<[0-9]*\\.[0-9]+");
                Pattern ratingGreaterPattern = Pattern.compile("rating>[0-9]*\\.[0-9]+");
                Pattern ratingLowerPattern = Pattern.compile("rating<[0-9]*\\.[0-9]+");
                Matcher matcher = priceGreaterPattern.matcher(parts[i]);
                boolean isPriceGreater = matcher.find();
                matcher = priceLowerPattern.matcher(parts[i]);
                boolean isPriceLower = matcher.find();
                matcher = ratingGreaterPattern.matcher(parts[i]);
                boolean isRatingGreater = matcher.find();
                matcher = ratingLowerPattern.matcher(parts[i]);
                boolean isRatingLower = matcher.find();
                if(isPriceGreater){
                    priceGreater = Double.parseDouble(parts[i].substring(parts[i].indexOf('>')+1));
                    curPageNum = 1;
                } else if (isPriceLower) {
                    priceLower = Double.parseDouble(parts[i].substring(parts[i].indexOf('<')+1));
                    curPageNum = 1;
                } else if (isRatingGreater) {
                    ratingsGreater= Double.parseDouble(parts[i].substring(parts[i].indexOf('>')+1));
                    curPageNum = 1;
                } else if (isRatingLower) {
                    ratingsLower = Double.parseDouble(parts[i].substring(parts[i].indexOf('<')+1));
                    curPageNum = 1;
                } else if(parts[1].equals("clear")){
                    priceGreater = -1;
                    priceLower = -1;
                    ratingsGreater = -1;
                    ratingsLower = -1;
                    curPageNum = 1;
                }else{
                    System.out.println("Invalid input. " + "Try again."+
                            "\nMake sure numbers are in decimal format and there are no spaces after or before comparison symbols. ");
                    System.out.print("Enter command('help' for command list): ");
                    command = scanner.nextLine();
                    return executeCommand(command, username);
                }
            }
            return false;
        } else if(parts[0].equals("help")){
            printCommands();
            scanner.nextLine();
            return false;
        }
        else{
            System.out.println("Invalid input. Try again");
            System.out.print("Enter command('help' for command list): ");
            command = scanner.nextLine();
            return executeCommand(command, username);
        }
    }

    @Override
    public void printHeader() {
        super.printHeader();
        System.out.println("Main Product Page");
        System.out.print("Current filters: ");
        boolean firstFilter = true;
        if(priceGreater > 0){
            if(priceLower > 0){
                System.out.print("Prices between "+priceGreater+" and "+priceLower);

            } else{
                System.out.print("Prices greater than "+priceGreater);
            }
            firstFilter = false;
        }
        else if(priceLower > 0){
            System.out.print("Prices lower than "+priceLower);
            firstFilter = false;
        }
        if(ratingsGreater > 0){
            if(!firstFilter){
                System.out.print(", ");
            }
            if(ratingsLower > 0){
                System.out.print("Ratings between "+ratingsGreater+" and "+ratingsLower);

            } else{
                System.out.print("Ratings greater than "+ratingsGreater);
            }
        }
        else if(ratingsLower > 0){
            if(!firstFilter){
                System.out.print(", ");
            }
            System.out.print("Ratings lower than "+ratingsLower);
        }
        if(priceGreater < 0 && priceLower < 0 && ratingsGreater < 0 && ratingsLower < 0){
            System.out.print("None");
        }
        if(order == ProductPage.Sort.highratings){
            System.out.println("\nSorted by high to low ratings\n");
        } else if (order == ProductPage.Sort.lowratings) {
            System.out.println("\nSorted by low to high ratings\n");
        } else if (order == ProductPage.Sort.highprices) {
            System.out.println("\nSorted by high to low prices\n");
        } else if (order == ProductPage.Sort.lowprices) {
            System.out.println("\nSorted by low to high prices\n");
        } else if (order == ProductPage.Sort.alpha) {
            System.out.println("\nSorted alphabetically\n");
        } else{
            System.out.println("\nNo sorting applied\n");
        }
    }

    public void printCommands(){
        printHeader();
        System.out.println("Command list\n");
        System.out.println("- 'navigate <page_num>'                             Navigates to page page_num.");
        System.out.println("    Page numbers:");
        if(isAdmin){
            System.out.println("        0: Admin Page");
        }
        System.out.println("        1: Main Product Page");
        System.out.println("        2: Shopping Cart Page");
        System.out.println("        3: Previous Purchases Page");
        System.out.println("        4: User Info Page");
        if(isSupplier){
            System.out.println("        5: Supplier Page");
        }
        System.out.println("- 'next'                                            Navigates to the next page of listings.");
        System.out.println("- 'previous'                                        Navigates to the previous page of listings.");
        System.out.println("- 'view <product_id>'                               Opens that product.");
        System.out.println("- 'checkout <product_id> x'                         Add x quantity of that product to the shopping cart.");
        System.out.println("- 'sortby <order>'                                  Sorts products.");
        System.out.println("    Order options:");
        System.out.println("        lowprices       Low to high prices");
        System.out.println("        highprices       High to low prices");
        System.out.println("        lowratings      Low to high ratings");
        System.out.println("        highratings     High to low ratings");
        System.out.println("        alpha           A-Z alphabetical");
        System.out.println("        clear           Clears order choice");
        System.out.println("- 'filterby <filter1> <filter2>...'                 Filters products by 1 or more filters.");
        System.out.println("    Filter options:");
        System.out.println("        (ALL values of x must be decimal, so 4 would have to be 4.0)");
        System.out.println("        price>x         Price greater than x");
        System.out.println("        price<x         Price less than x");
        System.out.println("        rating>x        Rating greater than x");
        System.out.println("        rating<x        Rating less than x");
        System.out.println("        clear           Clears filters\n");
        System.out.println("Press enter to quit.");
    }
}

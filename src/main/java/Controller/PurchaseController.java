package Controller;

import Entity.Product;
import Entity.Purchase;
import Entity.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseController {
    public static List<Purchase> getPurchases(String username, int transactionId) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("SELECT * FROM purchase WHERE Transaction_id=? AND Username=?");
        stmt.setInt(1, transactionId);
        stmt.setString(2, username);
        ResultSet result = stmt.executeQuery();

        List<Purchase> purchaseList = new ArrayList<Purchase>();
        while(result.next()){
            int productId = result.getInt(2);
            int quantity = result.getInt(4);
            Purchase p = new Purchase(transactionId, productId, username, quantity);
            purchaseList.add(p);
        }
        con.close();
        return purchaseList;
    }
    public static List<Purchase> getProductPurchases(int productId) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("SELECT * FROM purchase WHERE Product_id=?");
        stmt.setInt(1, productId);
        ResultSet result = stmt.executeQuery();

        List<Purchase> purchaseList = new ArrayList<Purchase>();
        while(result.next()){
            int transactionId = result.getInt(1);
            String username = result.getString(3);
            int quantity = result.getInt(4);
            Purchase p = new Purchase(transactionId, productId, username, quantity);
            purchaseList.add(p);
        }
        con.close();
        return purchaseList;
    }
    public static Purchase getQuantity(int transactionId, int productId, String username) throws SQLException{
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("SELECT Quantity FROM purchase WHERE Transaction_id=? AND Product_id=? AND Username=?");
        stmt.setInt(1, transactionId);
        stmt.setInt(2, productId);
        stmt.setString(3, username);
        ResultSet result = stmt.executeQuery();
        if(result.next()){
            int quantity = result.getInt(1);
            Purchase p = new Purchase(transactionId, productId, username, quantity);
            con.close();
            return p;
        }
        con.close();
        return null;
    }
    public static void deletePurchase(Purchase purchase) throws SQLException {
        purchase.setQuantity(0);
        updateTransaction(purchase);
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("DELETE FROM purchase WHERE Transaction_id=? AND Product_id=?");
        stmt.setInt(1, purchase.getTransactionId());
        stmt.setInt(2, purchase.getProductId());
        stmt.execute();
        con.close();
    }

    public static void changeQuantity(Purchase purchase) throws SQLException {
        updateTransaction(purchase);
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("UPDATE purchase SET Quantity=? WHERE Transaction_id=? AND Product_id=?");
        stmt.setInt(1, purchase.getQuantity());
        stmt.setInt(2, purchase.getTransactionId());
        stmt.setInt(3, purchase.getProductId());
        stmt.execute();
        con.close();
    }

    public static void deleteAllPurchases(int transactionId) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("DELETE FROM purchase WHERE Transaction_id=?");
        stmt.setInt(1, transactionId);
        stmt.execute();
        con.close();
    }

    public static Purchase addPurchase(Purchase purchase) throws SQLException {
        updateTransaction(purchase);
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("INSERT INTO purchase VALUES (?, ?, ?, ?)");
        stmt.setInt(1, purchase.getTransactionId());
        stmt.setInt(2, purchase.getProductId());
        stmt.setString(3, purchase.getBuyerUsername());
        stmt.setInt(4, purchase.getQuantity());
        stmt.execute();
        con.close();
        return purchase;
    }
    private static void updateTransaction(Purchase purchase) throws  SQLException{
        Transaction transaction = TransactionController.getTransaction(purchase.getTransactionId());
        Purchase originalPurchase = PurchaseController.getQuantity(purchase.getTransactionId(), purchase.getProductId(), purchase.getBuyerUsername());
        int oldQ = 0;
        if(originalPurchase!=null){
            oldQ = originalPurchase.getQuantity();
        }
        Product product = ProductController.getProduct(purchase.getProductId());
        transaction.setSubTotal(transaction.getSubTotal() + (product.getPrice()*(purchase.getQuantity()-oldQ)));

        TransactionController.updateTransaction(transaction);
    }

    public static int getTotalProductsSoldCount() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("SELECT SUM(Quantity) FROM purchase");
        ResultSet result = stmt.executeQuery();
        if(result.next()){
            return result.getInt(1);
        }
        return -1;
    }
}

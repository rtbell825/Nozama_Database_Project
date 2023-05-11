package Controller;

import Entity.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TransactionController {
    public static List<Transaction> getSortedCompletedTransactions(String username) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement(
                "SELECT DISTINCT transaction.* FROM purchase JOIN transaction " +
                        "ON purchase.Transaction_id = transaction.Transaction_id WHERE purchase.Username=? AND transaction.Status!='IN_CART' ORDER BY transaction.Date_time DESC");
        stmt.setString(1, username);
        ResultSet result = stmt.executeQuery();

        List<Transaction> transactionList = new ArrayList<Transaction>();
        while(result.next()){
            int transactionId = result.getInt(1);
            Transaction.Status status = Transaction.Status.valueOf(result.getString(2));
            Date date = result.getDate(3);
            double total = result.getDouble(4);
            double subtotal = result.getDouble(5);
            double tax = result.getDouble(6);
            String streetAddress = result.getString(7);
            String city = result.getString(8);
            String state = result.getString(9);
            String zip = result.getString(10);
            String deliveryInstructions = result.getString(11);
            Transaction t = new Transaction(transactionId, status, date, total, subtotal, tax, streetAddress, city, state, zip, deliveryInstructions);
            transactionList.add(t);
        }
        con.close();
        return transactionList;
    }
    public static Transaction getShoppingCart(String username) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement(
                "SELECT transaction.* FROM purchase JOIN transaction " +
                        "WHERE purchase.Transaction_id = transaction.Transaction_id AND purchase.Username=? AND transaction.Status='IN_CART'");
        stmt.setString(1, username);
        ResultSet result = stmt.executeQuery();

        if(result.next()){
            int transactionId = result.getInt(1);
            Transaction.Status status = Transaction.Status.valueOf(result.getString(2));
            Date date = result.getDate(3);
            double total = result.getDouble(4);
            double subtotal = result.getDouble(5);
            double tax = result.getDouble(6);
            String streetAddress = result.getString(7);
            String city = result.getString(8);
            String state = result.getString(9);
            String zip = result.getString(10);
            String deliveryInstructions = result.getString(11);
            Transaction t = new Transaction(transactionId, status, date, total, subtotal, tax, streetAddress, city, state, zip, deliveryInstructions);
            con.close();
            return t;
        }
        con.close();
        return null;
    }
    public static Transaction getTransaction(int transactionId) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM transaction WHERE Transaction_id=?");
        stmt.setInt(1, transactionId);
        ResultSet result = stmt.executeQuery();

        if(result.next()){
            Transaction.Status status = Transaction.Status.valueOf(result.getString(2));
            Date date = result.getDate(3);
            double total = result.getDouble(4);
            double subtotal = result.getDouble(5);
            double tax = result.getDouble(6);
            String streetAddress = result.getString(7);
            String city = result.getString(8);
            String state = result.getString(9);
            String zip = result.getString(10);
            String deliveryInstructions = result.getString(11);
            Transaction t = new Transaction(transactionId, status, date, total, subtotal, tax, streetAddress, city, state, zip, deliveryInstructions);
            con.close();
            return t;
        }
        con.close();
        return null;
    }

    public static void deleteTransaction(int transactionId) throws SQLException {
        PurchaseController.deleteAllPurchases(transactionId);
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("DELETE FROM transaction WHERE Transaction_id=?");
        stmt.setInt(1, transactionId);
        stmt.execute();
        con.close();
    }

    public static void updateTransaction(Transaction completedTransaction) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("UPDATE transaction SET Status=?, Date_time=?, Total=?, Subtotal=?, Tax=?, Street_address=?, City=?, State=?, Zip=?, Delivery_instructions=? WHERE Transaction_id=?");
        stmt.setString(1, completedTransaction.getCurStatus().name());
        stmt.setDate(2, new java.sql.Date(completedTransaction.getDateTime().getTime()));
        stmt.setDouble(3, completedTransaction.getTotal());
        stmt.setDouble(4, completedTransaction.getSubTotal());
        stmt.setDouble(5, completedTransaction.getTax());
        stmt.setString(6, completedTransaction.getStreetAddress());
        stmt.setString(7, completedTransaction.getCity());
        stmt.setString(8, completedTransaction.getState());
        stmt.setString(9, completedTransaction.getZip());
        stmt.setString(10, completedTransaction.getDeliveryInstructions());
        stmt.setInt(11, completedTransaction.getTransactionId());
        stmt.execute();
        con.close();
    }

    public static Transaction addTransaction(Transaction transaction) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("INSERT INTO transaction VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        stmt.setInt(1, transaction.getTransactionId());
        stmt.setString(2, transaction.getCurStatus().name());
        stmt.setDate(3, new java.sql.Date(transaction.getDateTime().getTime()));
        stmt.setDouble(4, transaction.getTotal());
        stmt.setDouble(5, transaction.getSubTotal());
        stmt.setDouble(6, transaction.getTax());
        stmt.setString(7, transaction.getStreetAddress());
        stmt.setString(8, transaction.getCity());
        stmt.setString(9, transaction.getState());
        stmt.setString(10, transaction.getZip());
        stmt.setString(11, transaction.getDeliveryInstructions());
        while (true) {
            try{
                stmt.execute();
                break;
            } catch (Exception e){
                Random rand = new Random();
                transaction.setTransactionId(rand.nextInt(Integer.MAX_VALUE));
                stmt.setInt(1, transaction.getTransactionId());
            }
        }
        con.close();
        return transaction;
    }
}

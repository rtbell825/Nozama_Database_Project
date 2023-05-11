package Controller;

import Entity.Client;
import Entity.Supplier;

import java.sql.*;

public class ClientController {
    public static Client getClient(String username) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("SELECT * FROM client WHERE Username = ?");
        stmt.setString(1, username);
        ResultSet result = stmt.executeQuery();
        if(result.next()) {
            String bankName = result.getString(2);
            String bankNumber = result.getString(3);
            boolean isSupplier = result.getBoolean(4);
            con.close();
            Client client = new Client(username, bankName, bankNumber, isSupplier);
            if (isSupplier) {
                Supplier supplier = new Supplier(client, SupplierController.getSupplier(username).getRating());
                return supplier;
            } else {
                return client;
            }
        }
        else{
            con.close();
            return null;
        }
    }
    public static int getClientCount() throws SQLException{
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM client");
        ResultSet result = stmt.executeQuery();
        if(result.next()){
            return result.getInt(1);
        }
        return -1;
    }

    public static Client addClient(Client client) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("INSERT INTO client VALUES (?, ?, ?, ?)");
        stmt.setString(1, client.getUsername());
        stmt.setString(2, client.getBankName());
        stmt.setString(3, client.getBankNumber());
        stmt.setBoolean(4, client.isSupplier());
        stmt.execute();
        con.close();
        if(client.isSupplier()){
            Supplier supplier = new Supplier(client, SupplierController.addSupplier((Supplier)client).getRating());
            return supplier;
        }
        return client;
    }
    public static void changeBankInfo(String username, String bankName, String bankNumber) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("UPDATE client SET Bank_name = ?, Bank_number = ? WHERE Username = ?");
        stmt.setString(1, bankName);
        stmt.setString(2, bankNumber);
        stmt.setString(3, username);
        stmt.execute();
        con.close();
    }

    public static void makeSupplier(Client client) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("UPDATE client SET isSupplier = true WHERE Username = ?");
        stmt.setString(1, client.getUsername());
        stmt.execute();
        Supplier supplier = new Supplier(client, 0.0);
        SupplierController.addSupplier(supplier);
        con.close();
    }
}

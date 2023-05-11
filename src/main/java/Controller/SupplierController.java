package Controller;

import Entity.Supplier;

import java.sql.*;

public class SupplierController {
    public static Supplier getSupplier(String username) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM supplier WHERE Username = ?");
        stmt.setString(1, username);
        ResultSet result = stmt.executeQuery();
        if(result.next()) {
            double rating = result.getDouble(2);
            con.close();
            Supplier supplier = new Supplier(username, rating);
            return supplier;
        }
        else{
            con.close();
            return null;
        }
    }
    public static Supplier addSupplier(Supplier supplier) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("INSERT INTO supplier VALUES (?, ?)");
        stmt.setString(1, supplier.getUsername());
        stmt.setDouble(2, supplier.getRating());
        stmt.execute();
        con.close();
        return supplier;
    }

    public static int getSupplierCount() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM supplier");
        ResultSet result = stmt.executeQuery();
        if(result.next()){
            return result.getInt(1);
        }
        return -1;
    }
}

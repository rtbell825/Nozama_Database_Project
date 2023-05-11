package Controller;

import Entity.Admin;
import Entity.Client;
import Entity.Supplier;
import Entity.User;

import java.sql.*;

public class UserController {
    public static User getUser(String username) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("SELECT * FROM user WHERE Username = ?");
        stmt.setString(1, username);
        ResultSet result = stmt.executeQuery();

        if(result.next()){
            String password = result.getString(2);
            String firstName = result.getString(3);
            String lastName = result.getString(4);
            boolean isAdmin = result.getBoolean(5);
            con.close();
            User user = new User(lastName, firstName, username, password, isAdmin);
            if (!isAdmin) {
                Client clientReturned = ClientController.getClient(username);
                Client client;
                if(!clientReturned.isSupplier()){
                    client = new Client(user, clientReturned);
                }
                else{
                    client = new Supplier(user, clientReturned, ((Supplier)clientReturned).getRating());
                }
                return client;
            } else {
                Admin admin = new Admin(user, AdminController.getAdmin(username).getLevel());
                return admin;
            }
        }
        else{
            con.close();
            return null;
        }
    }

    public static User addUser(User user) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("INSERT INTO user VALUES (?, ?, ?, ?, ?)");
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getFirstName());
        stmt.setString(4, user.getLastName());
        stmt.setBoolean(5, user.isAdmin());
        stmt.execute();
        con.close();
        if(user.isAdmin()){
            Admin admin = new Admin(user, AdminController.addAdmin((Admin)user).getLevel());
            return admin;
        }
        else{
            Client client = new Client(user, ClientController.addClient((Client)user));
            return client;
        }
    }
     public static void changeName(String username, String firstName, String lastName) throws SQLException {
         Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

         PreparedStatement stmt = con.prepareStatement("UPDATE user SET First_name = ?, Last_name = ? WHERE Username = ?");
         stmt.setString(1, firstName);
         stmt.setString(2, lastName);
         stmt.setString(3, username);
         stmt.execute();
         con.close();
     }
    public static void changePass(String username, String password) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("UPDATE user SET Password = ? WHERE Username = ?");
        stmt.setString(1, password);
        stmt.setString(2, username);
        stmt.execute();
        con.close();
    }
}

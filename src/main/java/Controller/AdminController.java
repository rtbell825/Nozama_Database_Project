package Controller;

import Entity.Admin;
import Entity.Supplier;

import java.sql.*;

public class AdminController {
    public static Admin getAdmin(String username) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM admin WHERE Username = ?");
        stmt.setString(1, username);
        ResultSet result = stmt.executeQuery();
        if(result.next()) {
            Admin.PrivilegeLevel privilegeLevel = privilegeToEnum(result.getString(2));
            con.close();
            Admin admin = new Admin(username, privilegeLevel);
            return admin;
        }
        else{
            con.close();
            return null;
        }
    }
    public static Admin addAdmin(Admin admin) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("INSERT INTO admin VALUES (?, ?)");
        stmt.setString(1, admin.getUsername());
        stmt.setString(2, privilegeToString(admin.getLevel()));
        stmt.execute();
        con.close();
        return admin;
    }
    public static String privilegeToString(Admin.PrivilegeLevel level){
        String levelStr = "-1";
        switch(level) {
            case ONE:
                levelStr = "1";
                break;
            case TWO:
                levelStr = "2";
                break;
            case THREE:
                levelStr = "3";
                break;
            case FOUR:
                levelStr = "4";
                break;
            case FIVE:
                levelStr = "5";
                break;
        }
        return levelStr;
    }
    public static Admin.PrivilegeLevel privilegeToEnum(String levelStr){
        Admin.PrivilegeLevel level;
        switch(levelStr) {
            case "1":
                level = Admin.PrivilegeLevel.ONE;
                break;
            case "2":
                level = Admin.PrivilegeLevel.TWO;
                break;
            case "3":
                level = Admin.PrivilegeLevel.THREE;
                break;
            case "4":
                level = Admin.PrivilegeLevel.FOUR;
                break;
            case "5":
                level = Admin.PrivilegeLevel.FIVE;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + levelStr);
        }
        return level;
    }
}

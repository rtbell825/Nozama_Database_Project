package NozamaApp;
import NozamaApp.Pages.LoginPage;

import java.sql.SQLException;

public class Nozama {
    public static void main(String[] args) throws SQLException {
        LoginPage p = new LoginPage();
        p.printPage("");
    }
}

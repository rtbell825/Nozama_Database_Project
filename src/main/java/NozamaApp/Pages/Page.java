package NozamaApp.Pages;

import java.sql.SQLException;

public abstract class Page {
    public abstract void printPage(String username) throws SQLException;
    public void printHeader(){
        System.out.println("*******************************************************************************************************************************************************************************************************");
        System.out.println("                                                                                  Nozama");
    }
}

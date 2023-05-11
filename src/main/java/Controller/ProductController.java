package Controller;
import Entity.Product;
import Entity.Purchase;
import Entity.Transaction;
import NozamaApp.Pages.ProductPage;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductController {
    public static List<Product> getSupplierProducts(String username) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("SELECT * FROM product WHERE Username=?");
        stmt.setString(1, username);
        ResultSet result = stmt.executeQuery();

        List<Product> productList = new ArrayList<Product>();
        int row = 1;
        while(result.next()){
            int productId = result.getInt(1);
            double price = result.getDouble(3);
            int quantity = result.getInt(4);
            double rating = result.getDouble(5);
            String description = result.getString(6);
            Product p = new Product(productId, username, price, quantity, rating, description, row);
            productList.add(p);
            row++;
        }
        con.close();
        return productList;
    }
    public static Product getProduct(int productId) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("SELECT * FROM product WHERE Product_id=?");
        stmt.setInt(1, productId);
        ResultSet result = stmt.executeQuery();
        if(result.next()){
            String username = result.getString(2);
            double price = result.getDouble(3);
            int quantity = result.getInt(4);
            double rating = result.getDouble(5);
            String description = result.getString(6);
            Product p = new Product(productId, username, price, quantity, rating, description, -1);
            return p;
        }
        return null;
    }
    public static List<Product> getProducts(
            double priceGreater, double priceLower, double ratingsGreater, double ratingsLower, ProductPage.Sort order, int page) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement(buildSelectQuery(priceGreater, priceLower, ratingsGreater, ratingsLower, order));
        ResultSet result = stmt.executeQuery();

        List<Product> productList = new ArrayList<Product>();
        int row = 1;
        while(result.next()){
            if((page*10) - row < 10 && (page*10) - row >= 0){
                int id = result.getInt(1);
                String username = result.getString(2);
                double price = result.getDouble(3);
                int quantity = result.getInt(4);
                double rating = result.getDouble(5);
                String description = result.getString(6);
                Product p = new Product(id, username, price, quantity, rating, description, row);
                productList.add(p);
            } else if ((page*10) - row < 0) {
                break;
            }
            row++;
        }
        con.close();
        return productList;
    }

    public static void buyProduct(int productId, int quantity) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("UPDATE product SET Quantity = Quantity - ? WHERE Product_id=?");
        stmt.setInt(1, quantity);
        stmt.setInt(2, productId);
        stmt.execute();
        con.close();
    }
    public static void updateProduct(Product product) throws SQLException {
        Product oldProduct = ProductController.getProduct(product.getProductId());
        List<Purchase> purchaseList = PurchaseController.getProductPurchases(product.getProductId());
        for (Purchase purchase:purchaseList) {
            Transaction transaction = TransactionController.getTransaction(purchase.getTransactionId());
            transaction.setSubTotal(transaction.getSubTotal() + ((product.getPrice()- oldProduct.getPrice()) * purchase.getQuantity()));
        }
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("UPDATE product SET Price = ?, Quantity = ?, Rating=?, Description=? WHERE Product_id=?");
        stmt.setDouble(1, product.getPrice());
        stmt.setInt(2, product.getQuantity());
        stmt.setDouble(3, product.getRating());
        stmt.setString(4, product.getDescription());
        stmt.setInt(5, product.getProductId());
        stmt.execute();
        con.close();
    }
    public static Product addProduct(Product product) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("INSERT INTO product VALUES (?, ?, ?, ?, ?, ?)");
        stmt.setInt(1, product.getProductId());
        stmt.setString(2, product.getSellerUsername());
        stmt.setDouble(3, product.getPrice());
        stmt.setInt(4, product.getQuantity());
        stmt.setDouble(5, product.getRating());
        stmt.setString(6, product.getDescription());
        while (true) {
            try{
                stmt.execute();
                break;
            } catch (Exception e){
                Random rand = new Random();
                product.setProductId(rand.nextInt(Integer.MAX_VALUE));
                stmt.setInt(1, product.getProductId());
            }
        }
        con.close();
        return product;
    }
    public static String buildSelectQuery(double priceGreater, double priceLower, double ratingsGreater, double ratingsLower, ProductPage.Sort order){
        boolean whereAdded = false;
        String query = "SELECT * FROM product";
        if(priceGreater > 0){
            if(!whereAdded){
                query += " WHERE Price > "+priceGreater;
                whereAdded = true;
            }
            else{
                query += " AND Price > "+priceGreater;
            }
        }
        if(priceLower > 0){
            if(!whereAdded){
                query += " WHERE Price < "+priceLower;
                whereAdded = true;
            }
            else{
                query += " AND Price < "+priceLower;
            }
        }
        if(ratingsGreater > 0){
            if(!whereAdded){
                query += " WHERE Rating > "+ratingsGreater;
                whereAdded = true;
            }
            else{
                query += " AND Rating > "+ratingsGreater;
            }
        }
        if(ratingsLower > 0){
            if(!whereAdded){
                query += " WHERE Rating < "+ratingsLower;
                whereAdded = true;
            }
            else{
                query += " AND Rating < "+ratingsLower;
            }
        }
        if(order == ProductPage.Sort.highratings){
            query += " ORDER BY Rating DESC";
        } else if (order == ProductPage.Sort.lowratings) {
            query += " ORDER BY Rating ASC";
        } else if (order == ProductPage.Sort.highprices) {
            query += " ORDER BY Price DESC";
        } else if (order == ProductPage.Sort.lowprices) {
            query += " ORDER BY Price ASC";
        } else if (order == ProductPage.Sort.alpha) {
            query += " ORDER BY Description";
        }
        return query;
    }

    public static int getProductCount() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM product");
        ResultSet result = stmt.executeQuery();
        if(result.next()){
            return result.getInt(1);
        }
        return -1;
    }

    public static int getTotalProductCount() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nozamadb", "root", "root");

        PreparedStatement stmt = con.prepareStatement("SELECT SUM(Quantity) FROM product");
        ResultSet result = stmt.executeQuery();
        if(result.next()){
            return result.getInt(1);
        }
        return -1;
    }
}

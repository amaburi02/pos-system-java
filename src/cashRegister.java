import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class cashRegister {
    final private Connection connection;
    public cashRegister(Connection connection) {
        this.connection = connection;
    }
    Scanner scanner;
    public void addItem(long upc, int quantity, String transactionNum) throws Exception {
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM items WHERE upc = " + upc;
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            sql = "INSERT INTO transaction_items (transaction_num, upc, quantity) VALUES (" + transactionNum + ", " + upc + ", " + quantity + ")";
            statement.executeUpdate(sql);
        }
        else {
            System.out.println("Item not found. Would you like to add a new item to the database? (Y/N)");
            String dbAdd = scanner.nextLine();
            if (Objects.equals(dbAdd, "Y")) {
                System.out.println("Enter name (12 characters max):");
                String itemName = scanner.nextLine();
                System.out.println("Enter price: ");
                String strPrice = scanner.nextLine();
                double price = Double.parseDouble(strPrice);
                sql = "INSERT INTO items (upc, item_name, price) VALUES (" + upc + ", " + itemName + ", " + price + ")";
                statement.executeUpdate(sql);
                sql = "INSERT INTO transaction_items (transaction_num, upc, quantity) VALUES (" + transactionNum + ", " + upc + ", " + quantity + ")";
                statement.executeUpdate(sql);
            }
            else if (Objects.equals(dbAdd, "N")) {
                System.out.println("Cancelled item add.");
            }
            else {
                System.out.println("Invalid Input, please try adding the item again.");
            }
        }
    }
    public void removeItem(long upc, int quantity, String transactionNum) throws Exception {
        Statement statement = connection.createStatement();
        String sql = "SELECT quantity FROM transaction_items WHERE transaction_num = " + transactionNum;
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            int svQuantity = rs.getInt(quantity);
            if (quantity == svQuantity) {
                sql = "DELETE FROM transaction_items WHERE (transaction_num = '" + transactionNum + "' AND upc = " + upc + ")";
            }
            else if (quantity < svQuantity) {
                svQuantity = svQuantity - quantity;
                sql = "UPDATE transaction_items SET quantity = " + svQuantity + ", WHERE transaction_num = " + transactionNum;
            }
        }
        statement.executeUpdate(sql);
    }
}
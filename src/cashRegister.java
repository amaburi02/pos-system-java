import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class cashRegister {
    private Connection connection;
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
            System.out.println("Would you like to add a new item to the database? (Y/N)");
            String dbAdd = scanner.nextLine();
            if (dbAdd == "Y") {

            }
            else if (dbAdd == "N") {
                
            }
            else {
                System.out.println("Invalid Input");
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
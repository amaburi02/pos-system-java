import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Scanner;

public class cashRegister {
    final private Connection connection;
    public cashRegister(Connection connection) {
        this.connection = connection;
    }
    Scanner scanner = new Scanner(System.in);
    public void addItem(long upc, int quantity, String transactionNum) throws Exception {
        String sql = "SELECT * FROM items WHERE upc = ?";
        PreparedStatement checkStmt = connection.prepareStatement(sql);
        checkStmt.setLong(1, upc);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
            sql = "INSERT INTO transaction_items (transaction_num, upc, quantity) VALUES (?, ?, ?)";
            PreparedStatement addStmt = connection.prepareStatement(sql);
            addStmt.setLong(1, Long.parseLong(transactionNum));
            addStmt.setLong(2, upc);
            addStmt.setInt(3, quantity);
            addStmt.executeUpdate();
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
                sql = "INSERT INTO items (upc, item_name, price) VALUES (?, ?, ?)";
                PreparedStatement itemAddStmt = connection.prepareStatement(sql);
                itemAddStmt.setLong(1, upc);
                itemAddStmt.setString(2, itemName);
                itemAddStmt.setDouble(3, price);
                itemAddStmt.executeUpdate();
                sql = "INSERT INTO transaction_items (transaction_num, upc, quantity) VALUES (?, ?, ?)";
                PreparedStatement TIAddStmt = connection.prepareStatement(sql);
                TIAddStmt.setLong(1, Long.parseLong(transactionNum));
                TIAddStmt.setLong(2, upc);
                TIAddStmt.setInt(3, quantity);
                TIAddStmt.executeUpdate();
                System.out.println("Item added.");
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
        long transactionNumParse = Long.parseLong(transactionNum);
        String sql = "SELECT * FROM items WHERE upc = ?";
        PreparedStatement checkStmt = connection.prepareStatement(sql);
        checkStmt.setLong(1, upc);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
            sql = "SELECT quantity FROM transaction_items WHERE transaction_num = ? AND upc = ?";
            PreparedStatement quantCheck = connection.prepareStatement(sql);
            quantCheck.setLong(1, transactionNumParse);
            quantCheck.setLong(2, upc);
            System.out.println("SQL Query: " + quantCheck);
            ResultSet quantRs = quantCheck.executeQuery();
            if (quantRs.next()) {
                //to do: fix logic "Column 'quantity' not found
                int dbQuantity = rs.getInt("quantity");
                if (quantity > dbQuantity) {
                    System.out.println("Invalid quantity entered.");
                }
                else {
                    sql = "INSERT INTO transaction_items (transaction_num, upc, quantity) VALUES (?, ?, ?)";
                    PreparedStatement addStmt = connection.prepareStatement(sql);
                    addStmt.setLong(1, transactionNumParse);
                    addStmt.setLong(2, upc);
                    addStmt.setInt(3, -quantity);
                    addStmt.executeUpdate();
                    System.out.println("Item voided.");
                }
            }
        }
        else {
            System.out.println("Invalid input, please try again.");
        }
        /*
        String sql = "SELECT quantity FROM transaction_items WHERE transaction_num = ?";
        PreparedStatement checkStmt = connection.prepareStatement(sql);
        checkStmt.setLong(1, Long.parseLong(transactionNum));
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
            int svQuantity = rs.getInt(quantity);
            if (quantity == svQuantity) {
                sql = "DELETE FROM transaction_items WHERE transaction_num = ? AND upc = ?";
                PreparedStatement delStmt = connection.prepareStatement(sql);
                delStmt.setLong(1, Long.parseLong(transactionNum));
                delStmt.setLong(2, upc);
                delStmt.executeUpdate();
            }
            else if (quantity < svQuantity) {
                svQuantity = svQuantity - quantity;
                sql = "UPDATE transaction_items SET quantity = " + svQuantity + ", WHERE transaction_num = " + transactionNum;
                PreparedStatement delStmt = connection.prepareStatement(sql);
                delStmt.setInt(1, svQuantity);
                delStmt.setLong(2, Long.parseLong(transactionNum));
                delStmt.executeUpdate();
            }
        } */
    }
}
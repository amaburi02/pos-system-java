import java.sql.*;
import java.util.Random;
import javax.swing.SwingUtilities;
import java.util.Scanner;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainWindow main = new mainWindow();
                main.show();
            }
        });
        // Database URL
        String jdbcUrl = "jdbc:mysql://localhost:3306/retail";
        String username = "root";
        String password = "root";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        boolean signedIn = false;
        boolean quit = false;

        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            statement = connection.createStatement();
            cashRegister CashRegister = new cashRegister(connection);

            Scanner scanner = new Scanner(System.in);
            String sql;
            PreparedStatement stmt;
            String enteredId = "";
            String string_upc;
            String string_quantity;
            int quantity;

            while(!signedIn) {
                System.out.println("Operator ID:");
                enteredId = scanner.nextLine();
                System.out.println("Password:");
                String opPassword = scanner.nextLine();
                sql = "SELECT * FROM operators WHERE id = ? AND pin = ?";

                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(enteredId));
                stmt.setInt(2, Integer.parseInt(opPassword));

                resultSet = stmt.executeQuery();

                if (resultSet.next()) {
                    System.out.println("Login successful!");
                    signedIn = true;
                }
                else {
                    System.out.println("Error: Operator ID or Password is incorrect/invalid");
                }
            }

            while(!quit) {
                System.out.println("Enter command, or 'q' to sign out");
                String command = scanner.nextLine();
                switch(command) {
                    case "q":
                        quit = true;
                        break;
                    case "begin":
                        Random rand = new Random();
                        long transactionNumFirst = rand.nextInt(99999999);
                        long transactionNumSecond = rand.nextInt(999999999);

                        String transactionNum = String.format("%08d%08d",transactionNumFirst, transactionNumSecond);
                        sql = "INSERT INTO transactions (transaction_num, op_num) VALUES (" + transactionNum + ", " + enteredId + ")";
                        statement.executeUpdate(sql);

                        String commandReg = scanner.nextLine();
                        switch(commandReg) {
                            case "add":
                                System.out.println("Enter UPC:");
                                string_upc = scanner.nextLine();
                                long upc = Long.parseLong(string_upc);

                                System.out.println("Enter quantity:");
                                string_quantity = scanner.nextLine();
                                quantity = Integer.parseInt(string_quantity);

                                CashRegister.addItem(upc, quantity, transactionNum);
                            case "void":
                                System.out.print("Enter UPC:");
                                string_upc = scanner.nextLine();
                                upc = Long.parseLong(string_upc);

                                System.out.println("Enter quantity to remove:");
                                string_quantity = scanner.nextLine();
                                quantity = Integer.parseInt(string_quantity);

                                CashRegister.removeItem(upc, quantity, transactionNum);
                            case "end":
                                System.out.println("Enter payment method:");
                                String paymentMethod = scanner.nextLine();

                                LocalDateTime currDateTime = LocalDateTime.now();
                                sql = "INSERT INTO transactions (transaction_num, op_num, date, payment_method) VALUES (?, ?, ?, ?)";
                                stmt = connection.prepareStatement(sql);
                                stmt.setInt(1, Integer.parseInt(transactionNum));
                                stmt.setInt(2, Integer.parseInt(enteredId));
                                stmt.setObject(3, currDateTime);
                                stmt.setString(4, paymentMethod);

                                stmt.executeUpdate();
                                break;
                        }
                    default:
                        System.out.print("Invalid command");
                }
            }
            System.out.println("Signed off.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

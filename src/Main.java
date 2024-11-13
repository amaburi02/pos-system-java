import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
            String enteredId = "";
            String string_upc;
            String string_quantity;
            int quantity;

            while(!signedIn) {
                System.out.println("Operator ID:");
                enteredId = scanner.nextLine();
                System.out.println("Password:");
                String opPassword = scanner.nextLine();
                sql = "SELECT * FROM operators WHERE id = '" + enteredId + "' AND pin = '" + opPassword + "'";

                resultSet = statement.executeQuery(sql);

                if (resultSet.next()) {
                    System.out.println("Login successful!");
                    signedIn = true;
                }
                else {
                    System.out.println("Error: Operator ID or Password is incorrect/invalid");
                }
            }

            Random rand = new Random();
            long transactionNumFirst = rand.nextInt(99999999);
            long transactionNumSecond = rand.nextInt(999999999);

            String transactionNum = String.format("%08d%08d",transactionNumFirst, transactionNumSecond);
            sql = "INSERT INTO transactions (transaction_num, op_num) VALUES (" + transactionNum + ", " + enteredId + ")";
            statement.executeUpdate(sql);

            while(!quit) {
                System.out.println("Enter command, or 'q' to sign out");
                String command = scanner.nextLine();
                switch(command) {
                    case "q":
                        quit = true;
                    case "begin":
                        String commandReg = scanner.nextLine();
                        switch(commandReg) {
                            case "add":
                                System.out.print("Enter UPC:");
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

                                System.out.println("Enter quantity:");
                                string_quantity = scanner.nextLine();
                                quantity = Integer.parseInt(string_quantity);

                                CashRegister.removeItem(upc, quantity, transactionNum);
                            case "end":
                                break;
                        }
                    default:
                        System.out.print("Invalid command");
                }
            }
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

package it2e.bdr.mangila;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class RECORDS {

    public config conf;

    public RECORDS() {
        conf = new config();
    }

    public void recordsMenu() {
        Scanner sc = new Scanner(System.in);
        String response;

        do {
            System.out.println("\n------------------------");
            System.out.println("Welcome to Records Panel");
            System.out.println("------------------------");
            System.out.println("1. Generate Specific Report");
            System.out.println("2. Generate General Report");
            System.out.println("3. Exit");

            System.out.print("Enter Selection: ");
            int choice = sc.nextInt();
            RECORDS rs = new RECORDS();

            switch (choice) {
                case 1:
                    rs.generateSpecificRecord();
                    break;
                case 2:
                    rs.generateGeneralRecord();
                    break;
                case 3:
                    System.out.println("Exiting Records Panel...");
                    return; // Exit the menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.print("Do you want to continue? (Yes/No): ");
            response = sc.next();
        } while (response.equalsIgnoreCase("Yes"));
    }

    public void generateSpecificRecord() {
    Scanner sc = new Scanner(System.in);
    config conf = new config();

    System.out.print("Enter Customer ID: ");
    int cid = sc.nextInt();

    // Check if the Customer ID exists
    while (conf.getSingleValue("SELECT c_id FROM Citizen WHERE c_id = ?", cid) == 0) {
        System.out.print("ID doesn't exist, try again: ");
        cid = sc.nextInt();
    }

    // SQL query to fetch specific citizen's details and their requested documents
    String specificQuery = "SELECT c.c_id, c.a_fname, c.a_lname, c.a_email, " +
                           "d.d_id, d.d_type, d.d_price, dr.dr_date, dr.dr_status, " +
                           "dr.dr_due, dr.dr_cash " + // Added space before FROM
                           "FROM Citizen c " +
                           "JOIN DocumentRequested dr ON c.c_id = dr.c_id " +
                           "JOIN Documents d ON dr.d_id = d.d_id " +
                           "WHERE c.c_id = ?";

    try (Connection conn = conf.connectDB();
         PreparedStatement findRow = conn.prepareStatement(specificQuery)) {
        findRow.setInt(1, cid);

        try (ResultSet result = findRow.executeQuery()) {
            // Print the header
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-15s | %-30s | %-25s | %-15s | %-10s |\n",
                              "Customer ID", "Name", "Document Type", "Request Date", "Status");
            System.out.println("---------------------------------------------------------------------------------------------------------------");

            // Variables to hold totals
            double totalDueSum = 0;
            double cashReceivedSum = 0;
            boolean hasRecords = false;

            // Check if there are results and process them
            while (result.next()) {
                hasRecords = true;
                int customerID = result.getInt("c_id");
                String customerName = result.getString("a_lname") + ", " + result.getString("a_fname"); // Combine first and last name
                String documentType = result.getString("d_type");
                String requestDate = result.getString("dr_date");
                String status = result.getString("dr_status");
                double totalDue = result.getDouble("dr_due");
                double cashReceived = result.getDouble("dr_cash");

                // Accumulate totals
                totalDueSum += totalDue;
                cashReceivedSum += cashReceived;

                System.out.printf("| %-15d | %-30s | %-25s | %-15s | %-10s |\n",
                                  customerID, customerName, documentType, requestDate, status);
            }

            if (!hasRecords) {
                System.out.println("| No records found for the given Customer ID.");
            }
            System.out.println("---------------------------------------------------------------------------------------------------------------");

            System.out.println("\n------------------");

            // Print the total due, cash, and change
            if (hasRecords) {
                double change = cashReceivedSum - totalDueSum; // Calculate change
                System.out.println("Total Due: " + totalDueSum);
                System.out.println("Cash: " + cashReceivedSum);
                System.out.println("Change: " + change);
            }

            System.out.println("-------------------");
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
    private void generateGeneralRecord() {
        // SQL query to fetch all citizens and their requested documents
        String generalQuery = "SELECT c.c_id, c.a_fname, c.a_lname, c.a_email, d.d_id, d.d_type, d.d_price, dr.dr_status " +
                              "FROM Citizen c " +
                              "JOIN DocumentRequested dr ON c.c_id = dr.c_id " +
                              "JOIN Documents d ON dr.d_id = d.d_id";

        String[] generalHeaders = {"Citizen ID", "First Name", "Last Name", "Email", "Document ID", "Document Type", "Document Price", "Status"};
        String[] generalColumns = {"c_id", "a_fname", "a_lname", "a_email", "d_id", "d_type", "d_price", "dr_status"};

        conf.viewRecords(generalQuery, generalHeaders, generalColumns);
    }
}

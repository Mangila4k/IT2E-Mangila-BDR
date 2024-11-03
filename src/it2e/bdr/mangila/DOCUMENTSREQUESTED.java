package it2e.bdr.mangila;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DOCUMENTSREQUESTED {
    public void documentsrequestedMenu() {
        Scanner sc = new Scanner(System.in);
        String response;    
        do {
            System.out.println("\n----------------------------------------------");
            System.out.println("Welcome to Documents Requested Panel");
            System.out.println("----------------------------------------------");
            System.out.println("1. Add Document Requested");
            System.out.println("2. View Document Requested");
            System.out.println("3. Update Document Requested");
            System.out.println("4. Delete Document Requested");
            System.out.println("5. Exit");
            
            System.out.print("Enter Selection: ");
            int choice = sc.nextInt();
            DOCUMENTSREQUESTED drs = new DOCUMENTSREQUESTED();
            
            switch (choice) {
                case 1:
                    drs.addRequestedDocument();
                    drs.viewRequestedDocument();
                    break;
                case 2:
                    drs.viewRequestedDocument();
                    break;
                case 3:
                    drs.viewRequestedDocument();
                    drs.updateRequestedDocument();
                    drs.viewRequestedDocument();
                    break;
                case 4:
                    drs.viewRequestedDocument();
                    drs.deleteRequestedDocument();
                    drs.viewRequestedDocument();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
                    break;
            }
            System.out.print("Do you want to continue? (Yes/No): ");
            response = sc.next();
        } while (response.equalsIgnoreCase("Yes"));
    }

    private void addRequestedDocument() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        CITIZEN cs = new CITIZEN();
        cs.viewCitizen();

        System.out.print("Enter ID of the Citizen: ");
        int cid = sc.nextInt();

        String csql = "SELECT c_id FROM Citizen WHERE c_id = ?";
        while (conf.getSingleValue(csql, cid) == 0) {
            System.out.print("Citizen doesn't exist, Select again: ");
            cid = sc.nextInt();
        }

        DOCUMENTS dr = new DOCUMENTS();
        dr.viewDocument();

        System.out.print("Enter ID of the Document: ");
        int did = sc.nextInt();

        String dsql = "SELECT d_id FROM Documents WHERE d_id = ?";
        while (conf.getSingleValue(dsql, did) == 0) {
            System.out.print("Document doesn't exist, Select again: ");
            did = sc.nextInt();
        }

        System.out.print("Enter Quantity: ");
        double quantity = sc.nextDouble();

        String priceqry = "SELECT d_price FROM Documents WHERE d_id = ?";
        double price = conf.getSingleValue(priceqry, did);
        double due = price * quantity;

        System.out.println("\n------------------------");
        System.out.print("Total due: " + due);
        System.out.println("\n------------------------");

        System.out.print("Enter Cash Received: ");
        double rcash = sc.nextDouble();

        // Ensure that cash received is valid
        while (rcash < due) {
            System.out.println("Invalid amount, cash received cannot be less than total due. Try Again!: ");
            rcash = sc.nextDouble();
        }

        double change = rcash - due;  // Calculate change
        System.out.println("Change to be returned: " + change);

        LocalDate currdate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String date = currdate.format(format);

        // Determine the status based on cash received
        String status = (rcash >= due) ? "Approved" : "Pending"; // Set status to Approved if cash covers the due

        String orderqry = "INSERT INTO DocumentRequested (c_id, d_id, dr_quantity, dr_due, dr_cash, dr_change, dr_date, dr_status) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        // Pass the necessary details including status
        conf.addRecord(orderqry, cid, did, quantity, due, rcash, change, date, status);
    }

    public void viewRequestedDocument() {
        String DocumentRequestedQuery = "SELECT dr_id, a_lname, d_type, dr_due, dr_cash, dr_change, dr_date, dr_status FROM DocumentRequested " 
                + "LEFT JOIN Citizen ON Citizen.c_id = DocumentRequested.c_id " 
                + "LEFT JOIN Documents ON Documents.d_id = DocumentRequested.d_id";
        String[] DocumentRequestedHeaders = {"DRID", "Citizen", "Document Type", "Due", "Cash", "Change", "Date", "Status"};
        String[] DocumentRequestedColumns = {"dr_id", "a_lname", "d_type", "dr_due", "dr_cash", "dr_change", "dr_date", "dr_status"};
        config conf = new config();
        conf.viewRecords(DocumentRequestedQuery, DocumentRequestedHeaders, DocumentRequestedColumns);
    }

    public void updateRequestedDocument() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter Document Request ID to Update: ");
        int drId = sc.nextInt();
        
        String checkQuery = "SELECT dr_id FROM DocumentRequested WHERE dr_id = ?";
        while (conf.getSingleValue(checkQuery, drId) == 0) {
            System.out.print("Document Request doesn't exist. Try again: ");
            drId = sc.nextInt();
        }

        System.out.println("\nUpdate Options:");
        System.out.println("1. Update Quantity");
        System.out.println("2. Update Status");
        System.out.print("Select an option to update: ");
        int option = sc.nextInt();
        
        switch (option) {
            case 1:
                System.out.print("Enter New Quantity: ");
                double newQuantity = sc.nextDouble();
                
                String priceQuery = "SELECT d_price FROM Documents WHERE d_id = (SELECT d_id FROM DocumentRequested WHERE dr_id = ?)";
                double price = conf.getSingleValue(priceQuery, drId);
                double newDue = price * newQuantity;

                String updateQuantityQuery = "UPDATE DocumentRequested SET dr_quantity = ?, dr_due = ? WHERE dr_id = ?";
                conf.updateRecord(updateQuantityQuery, newQuantity, newDue, drId);
                
                System.out.println("Quantity and Due updated successfully.");
                break;

            case 2:
                System.out.println("\nStatus Options:");
                System.out.println("1. Approved");
                System.out.println("2. Denied");
                System.out.print("Select status: ");
                int statusChoice = sc.nextInt();

                String newStatus;
                switch (statusChoice) {
                    case 1:
                        newStatus = "Approved";
                        break;
                    case 2:
                        newStatus = "Denied";
                        break;
                    default:
                        System.out.println("Invalid status selection.");
                        return; // Exit the method if the choice is invalid
                }

                String updateStatusQuery = "UPDATE DocumentRequested SET dr_status = ? WHERE dr_id = ?";
                conf.updateRecord(updateStatusQuery, newStatus, drId);
                
                System.out.println("Status updated successfully to " + newStatus + ".");
                break;

            default:
                System.out.println("Invalid option selected.");
                break;
        }
    }

    public void deleteRequestedDocument() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter Document Request ID to Delete: ");
        int drId = sc.nextInt();
        
        String checkQuery = "SELECT dr_id FROM DocumentRequested WHERE dr_id = ?";
        while (conf.getSingleValue(checkQuery, drId) == 0) {
            System.out.print("Document Request doesn't exist. Try again: ");
            drId = sc.nextInt();
        }
        
        System.out.print("Are you sure you want to delete this request? (Yes/No): ");
        String confirmation = sc.next();
        
        if (confirmation.equalsIgnoreCase("Yes")) {
            String deleteQuery = "DELETE FROM DocumentRequested WHERE dr_id = ?";
            conf.deleteRecord(deleteQuery, drId);
            System.out.println("Document Request deleted successfully.");
        } else {
            System.out.println("Deletion canceled.");
        }
    }
}

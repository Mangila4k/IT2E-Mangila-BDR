package it2e.bdr.mangila;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DOCUMENTSREQUESTED {
    public void documentsrequestedMenu() {
        Scanner sc = new Scanner(System.in);
        String response;    
        do {
            System.out.println("\n-----------------------------------");
            System.out.println("Welcome to Documents Requested Panel");
            System.out.println("------------------------------------");
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

    int cid;
    while (true) {
        System.out.print("Enter ID of the Citizen: ");
        if (sc.hasNextInt()) {
            cid = sc.nextInt();
            if (conf.getSingleValue("SELECT c_id FROM Citizen WHERE c_id = ?", cid) != 0) {
                break;
            } else {
                System.out.println("Citizen doesn't exist, please select again.");
            }
        } else {
            System.out.println("Invalid input. Please enter a numeric ID.");
            sc.next();
        }
    }

    DOCUMENTS dr = new DOCUMENTS();
    dr.viewDocument();

    int did;
    while (true) {
        System.out.print("Enter ID of the Document: ");
        if (sc.hasNextInt()) {
            did = sc.nextInt();
            if (conf.getSingleValue("SELECT d_id FROM Documents WHERE d_id = ?", did) != 0) {
                break;
            } else {
                System.out.println("Document doesn't exist, please select again.");
            }
        } else {
            System.out.println("Invalid input. Please enter a numeric ID.");
            sc.next();
        }
    }

    double quantity = 0;
    while (true) {
        System.out.print("Enter Quantity: ");
        try {
            quantity = sc.nextDouble();
            if (quantity > 0) {
                break;
            } else {
                System.out.println("Quantity must be a positive number.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric value for quantity.");
            sc.next();
        }
    }

    double price = conf.getSingleValue("SELECT d_price FROM Documents WHERE d_id = ?", did);
    double due = price * quantity;

    System.out.println("\n------------------------");
    System.out.print("Total due: " + due);
    System.out.println("\n------------------------");

    double rcash = 0;
    while (true) {
        System.out.print("Enter Cash Received: ");
        try {
            rcash = sc.nextDouble();
            if (rcash >= due) {
                break;
            } else {
                System.out.println("Cash received cannot be less than the total due. Try again!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid cash amount.");
            sc.next();
        }
    }

    double change = rcash - due;
    System.out.println("Change to be returned: " + change);

    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    String status = "Approved";

    String orderqry = "INSERT INTO DocumentRequested (c_id, d_id, dr_quantity, dr_due, dr_cash, dr_change, dr_date, dr_status) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    conf.addRecord(orderqry, cid, did, quantity, due, rcash, change, date, status);
    System.out.println("Requested document added successfully.");
}

public void viewRequestedDocument() {
    String DocumentRequestedQuery = "SELECT dr_id, a_lname, d_type, dr_due, dr_cash, dr_change, dr_date, dr_status FROM DocumentRequested " +
                                    "LEFT JOIN Citizen ON Citizen.c_id = DocumentRequested.c_id " +
                                    "LEFT JOIN Documents ON Documents.d_id = DocumentRequested.d_id";
    String[] DocumentRequestedHeaders = {"DRID", "Citizen", "Document Type", "Due", "Cash", "Change", "Date", "Status"};
    String[] DocumentRequestedColumns = {"dr_id", "a_lname", "d_type", "dr_due", "dr_cash", "dr_change", "dr_date", "dr_status"};
    config conf = new config();
    conf.viewRecords(DocumentRequestedQuery, DocumentRequestedHeaders, DocumentRequestedColumns);
}

public void updateRequestedDocument() {
    Scanner sc = new Scanner(System.in);
    config conf = new config();
    
    int drId;
    while (true) {
        System.out.print("Enter Document Request ID to Update: ");
        if (sc.hasNextInt()) {
            drId = sc.nextInt();
            if (conf.getSingleValue("SELECT dr_id FROM DocumentRequested WHERE dr_id = ?", drId) != 0) {
                break;
            } else {
                System.out.println("Document Request doesn't exist. Try again.");
            }
        } else {
            System.out.println("Invalid input. Please enter a numeric ID.");
            sc.next();
        }
    }

    System.out.println("\nUpdate Options:");
    System.out.println("1. Update Quantity");
    System.out.println("2. Update Status");
    System.out.print("Select an option to update: ");
    
    int option = sc.nextInt();
    switch (option) {
        case 1:
            double newQuantity = 0;
            while (true) {
                System.out.print("Enter New Quantity: ");
                try {
                    newQuantity = sc.nextDouble();
                    if (newQuantity > 0) {
                        break;
                    } else {
                        System.out.println("Quantity must be positive.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a numeric quantity.");
                    sc.next();
                }
            }

            double price = conf.getSingleValue("SELECT d_price FROM Documents WHERE d_id = (SELECT d_id FROM DocumentRequested WHERE dr_id = ?)", drId);
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
                        return;
            };

            if (newStatus != null) {
                String updateStatusQuery = "UPDATE DocumentRequested SET dr_status = ? WHERE dr_id = ?";
                conf.updateRecord(updateStatusQuery, newStatus, drId);
                System.out.println("Status updated successfully to " + newStatus + ".");
            }
            break;

        default:
            System.out.println("Invalid option selected.");
            break;
    }
}

public void deleteRequestedDocument() {
    Scanner sc = new Scanner(System.in);
    config conf = new config();
    
    int drId;
    while (true) {
        System.out.print("Enter Document Request ID to Delete: ");
        if (sc.hasNextInt()) {
            drId = sc.nextInt();
            if (conf.getSingleValue("SELECT dr_id FROM DocumentRequested WHERE dr_id = ?", drId) != 0) {
                break;
            } else {
                System.out.println("Document Request doesn't exist. Try again.");
            }
        } else {
            System.out.println("Invalid input. Please enter a numeric ID.");
            sc.next();
        }
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
package it2e.bdr.mangila;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DOCUMENTS {
    
    public void documentMenu(){
    
    Scanner sc = new Scanner(System.in);
    String response;    
    do{
        System.out.println("\n--------------------------");
        System.out.println("Welcome to Documents Panel");
        System.out.println("---------------------------");
        System.out.println("1. Add Document");
        System.out.println("2. View Document");
        System.out.println("3. Update Document");
        System.out.println("4. Delete Document");
        System.out.println("5. Exit");
        
        System.out.print("Enter Selection: ");
        int choice = sc.nextInt();
        DOCUMENTS dr = new DOCUMENTS();
        
        switch(choice){
            case 1:
                dr.addDocument();
                dr.viewDocument();
                break;
            case 2:
                dr.viewDocument();
                break;
            case 3:
                dr.viewDocument();
                dr.updateDocument();
                dr.viewDocument();
                break;
            case 4:
                dr.viewDocument();
                dr.deleteDocument();
                dr.viewDocument();
                break;
            case 5:
                break;
        }
        System.out.println("Do you want to continue? (Yes/No): ");
        response = sc.next();
       }while(response.equalsIgnoreCase("Yes"));
}
    public void addDocument() {
    Scanner sc = new Scanner(System.in);

    // Display document type options
    System.out.println("Select Document Type:");
    System.out.println("1. CIDULA");
    System.out.println("2. BARANGAY CLEARANCE");
    System.out.println("3. BUSINESS PERMIT");

    int docChoice = 0;
    while (true) {
        try {
            System.out.print("Enter your choice (1-3): ");
            docChoice = sc.nextInt();
            if (docChoice < 1 || docChoice > 3) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
            } else {
                break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            sc.next(); // Clear invalid input
        }
    }

    String dtype;
        switch (docChoice) {
            case 1:
                dtype = "CIDULA";
                break;
            case 2:
                dtype = "BARANGAY CLEARANCE";
                break;
            case 3:
                dtype = "BUSINESS PERMIT";
                break;
            default:
                dtype = "";
    }

    double dprice = 0;
    while (true) {
        try {
            System.out.print("Document Price: ");
            dprice = sc.nextDouble();
            if (dprice <= 0) {
                System.out.println("Price must be a positive number. Please enter again.");
            } else {
                break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid price.");
            sc.next(); // Clear invalid input
        }
    }

    String qry = "INSERT INTO Documents (d_type, d_price) VALUES (?, ?)";
    config conf = new config();
    conf.addRecord(qry, dtype, dprice);
    System.out.println("Document added successfully!");
}

public void viewDocument() {
    String DocumentQuery = "SELECT * FROM Documents";
    String[] DocumentHeaders = {"ID", "Document Type", "Price"};
    String[] DocumentColumns = {"d_id", "d_type", "d_price"};
    config conf = new config();
    conf.viewRecords(DocumentQuery, DocumentHeaders, DocumentColumns);
}

public void updateDocument() {
    Scanner sc = new Scanner(System.in);
    config conf = new config();
    int id;

    // Validate document ID
    while (true) {
        System.out.print("Enter ID to update: ");
        if (sc.hasNextInt()) {
            id = sc.nextInt();
            if (conf.getSingleValue("SELECT d_id FROM Documents WHERE d_id=?", id) != 0) {
                break;
            } else {
                System.out.println("Selected ID doesn't exist! Please enter a valid ID.");
            }
        } else {
            System.out.println("Invalid input. Please enter a numeric ID.");
            sc.next(); // Clear invalid input
        }
    }

    // Select new document type
    System.out.println("Select New Document Type:");
    System.out.println("1. CIDULA");
    System.out.println("2. BARANGAY CLEARANCE");
    System.out.println("3. BUSINESS PERMIT");

    int docChoice = 0;
    while (true) {
        try {
            System.out.print("Enter your choice (1-3): ");
            docChoice = sc.nextInt();
            if (docChoice < 1 || docChoice > 3) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
            } else {
                break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            sc.next(); // Clear invalid input
        }
    }

   String dtype;
        switch (docChoice) {
            case 1:
                dtype = "CIDULA";
                break;
            case 2:
                dtype = "BARANGAY CLEARANCE";
                break;
            case 3:
                dtype = "BUSINESS PERMIT";
                break;
            default:
                dtype = "";
    }

    double dprice = 0;
    while (true) {
        try {
            System.out.print("New Document Price: ");
            dprice = sc.nextDouble();
            if (dprice <= 0) {
                System.out.println("Price must be a positive number. Please enter again.");
            } else {
                break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid price.");
            sc.next(); // Clear invalid input
        }
    }

    String qry = "Update Documents SET d_type = ?, d_price = ? WHERE d_id = ?";
    conf.updateRecord(qry, dtype, dprice, id);
    System.out.println("Document updated successfully!");
}

public void deleteDocument() {
    Scanner sc = new Scanner(System.in);
    config conf = new config();
    int id;

    // Validate document ID
    while (true) {
        System.out.print("Enter ID to delete: ");
        if (sc.hasNextInt()) {
            id = sc.nextInt();
            if (conf.getSingleValue("SELECT d_id FROM Documents WHERE d_id=?", id) != 0) {
                break;
            } else {
                System.out.println("Selected ID doesn't exist! Please enter a valid ID.");
            }
        } else {
            System.out.println("Invalid input. Please enter a numeric ID.");
            sc.next(); // Clear invalid input
        }
    }

    String qry = "DELETE FROM Documents WHERE d_id=?";
    conf.deleteRecord(qry, id);
    System.out.println("Document deleted successfully!");
}
}
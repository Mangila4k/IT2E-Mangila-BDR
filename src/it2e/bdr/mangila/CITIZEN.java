package it2e.bdr.mangila;

import java.util.Scanner;

public class CITIZEN {

    public void citizenMenu(){
        
    Scanner sc = new Scanner(System.in);
    String response;    
    do{
        System.out.println("\n----------------------------------------------");
        System.out.println("Welcome to Citizen Panel");
        System.out.println("----------------------------------------------");
        System.out.println("1. Add Citizen");
        System.out.println("2. View Citizen");
        System.out.println("3. Update Citizen");
        System.out.println("4. Delete Citizen");
        System.out.println("5. Exit");
        
        System.out.print("Enter Selection: ");
        int choice = sc.nextInt();
        CITIZEN cs = new CITIZEN();
        
        switch(choice){
            case 1:
                cs.addCitizen();
                cs.viewCitizen();
                break;
            case 2:
                cs.viewCitizen();
                break;
            case 3:
                cs.viewCitizen();
                cs.updateCitizen();
                cs.viewCitizen();
                break;
            case 4:
                cs.viewCitizen();
                cs.deleteCitizen();
                cs.viewCitizen();
                break;
            case 5:
                break;
        }
        System.out.print("Do you want to continue? (Yes/No): ");
        response = sc.next();
       }while(response.equalsIgnoreCase("Yes"));
    }
    
    public void addCitizen() {
    Scanner sc = new Scanner(System.in);
    String fname, lname, cemail;

    while (true) {
        System.out.print("Citizen First Name: ");
        fname = sc.nextLine();
        if (fname.matches("[A-Za-z ]+")) {
            break;
        } else {
            System.out.println("Invalid first name. Please enter only letters.");
        }
    }

    while (true) {
        System.out.print("Citizen Last Name: ");
        lname = sc.next();
        if (lname.matches("[A-Za-z]+")) {
            break; 
        } else {
            System.out.println("Invalid last name. Please enter only letters.");
        }
    }
  
    while (true) {
        System.out.print("Citizen Email: ");
        cemail = sc.next();
        if (cemail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            break;
        } else {
            System.out.println("Invalid email format. Please enter a valid email.");
        }
    }

    String qry = "INSERT INTO Citizen (a_fname, a_lname, a_email) VALUES (?, ?, ?)";
    config conf = new config();
    conf.addRecord(qry, fname, lname, cemail);

    System.out.println("Citizen added successfully.");
}

    public void viewCitizen() {
    String CitizenQuery = "SELECT * FROM Citizen";
    String[] CitizenHeaders = {"ID", "Fname", "Lname", "Email"};
    String[] CitizenColumns = {"c_id", "a_fname", "a_lname", "a_email"};
    config conf = new config();
    conf.viewRecords(CitizenQuery, CitizenHeaders, CitizenColumns);
}

public void updateCitizen() {
    Scanner sc = new Scanner(System.in);
    config conf = new config();
    int id;

    while (true) {
        System.out.print("Enter ID to update: ");
        if (sc.hasNextInt()) {
            id = sc.nextInt();
            if (conf.getSingleValue("SELECT c_id FROM Citizen WHERE c_id=?", id) != 0) {
                break;
            } else {
                System.out.println("Selected ID doesn't exist! Please enter a valid ID.");
            }
        } else {
            System.out.println("Invalid input. Please enter a numeric ID.");
            sc.next();
        }
    }

    String fname;
    while (true) {
        System.out.print("New Citizen First Name: ");
        fname = sc.next();
        if (fname.matches("[A-Za-z]+")) {
            break;
        } else {
            System.out.println("Invalid first name. Please enter only letters.");
        }
    }

    String lname;
    while (true) {
        System.out.print("New Citizen Last Name: ");
        lname = sc.next();
        if (lname.matches("[A-Za-z]+")) {
            break;
        } else {
            System.out.println("Invalid last name. Please enter only letters.");
        }
    }

    String cemail;
    while (true) {
        System.out.print("New Citizen Email: ");
        cemail = sc.next();
        if (cemail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            break;
        } else {
            System.out.println("Invalid email format. Please enter a valid email.");
        }
    }

    String qry = "UPDATE Citizen SET a_fname = ?, a_lname = ?, a_email = ? WHERE c_id = ?";
    conf.updateRecord(qry, fname, lname, cemail, id);
    System.out.println("Citizen updated successfully.");
}

public void deleteCitizen() {
    Scanner sc = new Scanner(System.in);
    config conf = new config();
    int id;

    while (true) {
        System.out.print("Enter ID to delete: ");
        if (sc.hasNextInt()) {
            id = sc.nextInt();
            if (conf.getSingleValue("SELECT c_id FROM Citizen WHERE c_id=?", id) != 0) {
                break;
            } else {
                System.out.println("Selected ID doesn't exist! Please enter a valid ID.");
            }
        } else {
            System.out.println("Invalid input. Please enter a numeric ID.");
            sc.next();
        }
    }

    String qry = "DELETE FROM Citizen WHERE c_id=?";
    conf.deleteRecord(qry, id);
    System.out.println("Citizen deleted successfully.");
}
}


   
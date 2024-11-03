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
    
    public void addCitizen(){
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Citizen First Name: ");
        String fname = sc.next();
        System.out.print("Citizen Last Name: ");
        String lname = sc.next();
        System.out.print("Citizen Email: ");
        String cemail = sc.next();
        
        String qry = "INSERT INTO Citizen (a_fname, a_lname, a_email) VALUES (?, ?, ?)";
        config conf = new config();
        
        conf.addRecord(qry, fname, lname, cemail);
        
    }
    public void viewCitizen() {
        String CitizenQuery = "SELECT * FROM Citizen";
        String[] CitizenHeaders = {"ID", "Fname", "Lname", "Email"};
        String[] CitizenColumns = {"c_id", "a_fname", "a_lname", "a_email"};
        config conf = new config();
        conf.viewRecords(CitizenQuery,CitizenHeaders, CitizenColumns);
    }
    public void updateCitizen(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        System.out.print("Enter ID to update: ");
        int id = sc.nextInt();
        
        while(conf.getSingleValue("SELECT c_id FROM Citizen WHERE c_id=?",id)==0){
            System.out.println("Selected ID doesn't exist!");
            System.out.print("Select Citizen ID again: ");
            id = sc.nextInt();
        }
        
        System.out.print("New Citizen First Name: ");
        String fname = sc.next();
        System.out.print("New Citizen Last Name: ");
        String lname = sc.next();
        System.out.print("New Citizen Email: ");
        String cemail = sc.next();
        
        String qry = "Update Citizen SET a_fname = ?, a_lname = ?, a_email = ? WHERE c_id = ?";
        
        
        conf.updateRecord(qry, fname, lname, cemail, id);
    }
    public void deleteCitizen(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        System.out.print("Enter ID to delete: ");
        int id = sc.nextInt();
        
        while(conf.getSingleValue("SELECT c_id FROM Citizen WHERE c_id=?",id)==0){
            System.out.println("Selected ID doesn't exist!");
            System.out.print("Select Citizen ID again: ");
            id = sc.nextInt();
       
    } String qry = "DELETE FROM Citizen WHERE c_id=?";
        conf.deleteRecord(qry, id);
    }
}


   

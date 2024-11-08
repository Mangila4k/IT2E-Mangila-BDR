package it2e.bdr.mangila;

import java.util.Scanner;

public class IT2EBDRMANGILA {

    public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    boolean exit = true; 
    do{
        System.out.println("\n-----------------------------------------");
        System.out.println("WELCOME TO BARANGAY DOCUMENTS REQUEST APP");
        System.out.println("-----------------------------------------");
        System.out.println("1. Citizen");
        System.out.println("2. Documents");
        System.out.println("3. DocumentsRequested");
        System.out.println("4. Records");
        System.out.println("5. Exit");
        
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        
        switch(choice){
            case 1:
                CITIZEN cs = new CITIZEN();
                cs.citizenMenu();
                break;
                
            case 2:
                DOCUMENTS dr = new DOCUMENTS();
                dr.documentMenu();
                break;
                
            case 3:
                DOCUMENTSREQUESTED drs = new DOCUMENTSREQUESTED();
                drs.documentsrequestedMenu();
                break;
                
            case 4:
                RECORDS rs = new RECORDS();
                rs.recordsMenu();
                break;
                
            case 5:
                System.out.print("Exit Selected... Type 'yes' to continue: ");
                String resp = sc.next();
                if(resp.equalsIgnoreCase("Yes")){
                exit = false;
                }
                break;
            
        }
       }while(exit);
    }
}
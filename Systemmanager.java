/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurantsystem1;

import java.util.*;

public class Systemmanager {
    
     private Systemmode mode;

    public Systemmanager(Systemmode mode) {
        this.mode = mode;
    }
    public Systemmode getMode() {
        return mode;
    }

    
      public void selectMode() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Welcome to the Restaurant System!");
        System.out.println("Please select your mode:");
        System.out.println("1. Online Delivery");
        System.out.println("2. Takeaway");
        System.out.println("3. Dine-In");
        
        int choice = sc.nextInt();
        
        switch (choice) {
            case 1:
                directToOnline();
                break;
            case 2:
                directToTakeaway();
                break;
            case 3:
                directToDineIn();
                break;
            default:
                System.out.println("Invalid choice! Defaulting to Dine-In mode.");
                directToDineIn();
        }
    }
   
    public void directToOnline() {
        mode = Systemmode.online_delivery;
        System.out.println("System set to ONLINE DELIVERY mode.\n");
    }
    
    public void directToTakeaway() {
        mode = Systemmode.takeAway;
        System.out.println("System set to TAKEAWAY mode.\n");
    }
    
    public void directToDineIn() {
        mode = Systemmode.walk_in;
        System.out.println("System set to DINE-IN mode.\n");
    }
     
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurantsystem1;

import java.io.Serializable;
import java.util.Scanner;

public class Systemmanager implements Serializable {
    private static final long serialVersionUID = 1L;

    private Systemmode currentMode;

    public Systemmanager(Systemmode mode) {
        this.currentMode = mode;
    }

    public Systemmode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(Systemmode mode) {
        this.currentMode = mode;
        System.out.println("System mode changed to: " + mode);
    }

    // Directly set mode
    public void directToOnline() {
        setCurrentMode(Systemmode.ONLINE_DELIVERY);
        System.out.println("System set to ONLINE DELIVERY mode.\n");
    }

    public void directToTakeaway() {
        setCurrentMode(Systemmode.TAKEAWAY);
        System.out.println("System set to TAKEAWAY mode.\n");
    }

    public void directToDineIn() {
        setCurrentMode(Systemmode.DINE_IN);
        System.out.println("System set to DINE-IN mode.\n");
    }

    // Interactive selection
    public void selectMode() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Restaurant System!");
        System.out.println("Please select your mode:");
        System.out.println("1. Online Delivery");
        System.out.println("2. Takeaway");
        System.out.println("3. Dine-In");

        int choice = sc.nextInt();
        switch (choice) {
            case 1 -> directToOnline();
            case 2 -> directToTakeaway();
            case 3 -> directToDineIn();
            default -> {
                System.out.println("Invalid choice! Defaulting to Dine-In mode.");
                directToDineIn();
            }
        }
    }

    // Mode description
    public String getModeDescription() {
        return switch (currentMode) {
            case ONLINE_DELIVERY -> "Online Delivery - Customers order from home for delivery";
            case TAKEAWAY -> "Takeaway - Customers order for pickup";
            case DINE_IN -> "Dine-In - Customers order and eat in restaurant";
        };
    }

    @Override
    public String toString() {
        return "System Manager [Current Mode: " + currentMode + "]";
    }
}

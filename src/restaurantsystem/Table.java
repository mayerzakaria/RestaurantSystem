package restaurantsystem;

import java.io.Serializable;
import java.util.*;

public class Table implements Serializable{
    private static final long serialVersionUID = 1L;
;
    
    public enum TableStatus {
        AVAILABLE,
        OCCUPIED
    }
    
    private int tableNumber;
    private int capacity;
    private TableStatus status;

    public Table(int tableNumber, int capacity, TableStatus status) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = status;
    }

    // Getters and Setters
    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status == TableStatus.AVAILABLE;
    }

    // ==================== STATIC METHODS ====================
    
    //  Select table with capacity check
    public static Table selectTableForCapacity(ArrayList<Table> tables, Scanner scanner, int numberOfPeople) {
        ArrayList<Table> available = getAvailableTables(tables);
        ArrayList<Table> suitableTables = new ArrayList<>();
        
        for (Table table : available) {
            if (table.getCapacity() >= numberOfPeople) {
                suitableTables.add(table);
            }
        }
        System.out.println("\n" + "=".repeat(60));
        System.out.println("      AVAILABLE TABLES FOR " + numberOfPeople + " PEOPLE");
        System.out.println("=".repeat(60));
        
         
        if (suitableTables.isEmpty()) {
            System.out.println(" No available tables for " + numberOfPeople + " people!");
            
            System.out.println("   Wait for a table to become available");
           
            
            return null;
        }
        
        for (int i = 0; i < suitableTables.size(); i++) {
            Table table = suitableTables.get(i);
            System.out.println((i + 1) + ". " + table.toString() + 
                             " (Fits " + numberOfPeople + " people)");
        }
        
        System.out.print("\nSelect table number: ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice < 1 || choice > suitableTables.size()) {
                System.out.println(" Invalid choice!");
                return null;
            }

            Table selected = suitableTables.get(choice - 1);
            selected.assignTable();
            return selected;
        } catch (InputMismatchException e) {
            System.out.println(" Invalid input!");
            scanner.nextLine();
            return null;
        }
    }

    // Get all available tables
    public static ArrayList<Table> getAvailableTables(ArrayList<Table> tables) {
        ArrayList<Table> available = new ArrayList<>();
        for (Table table : tables) {
            if (table.isAvailable()) {
                available.add(table);
            }
        }
        return available;
    }
    
    // ==================== INSTANCE METHODS ====================

    public boolean assignTable() {
        if (this.isAvailable()) {
            status = TableStatus.OCCUPIED;
            System.out.println(" Table " + tableNumber + " assigned.");
            return true;
        } else {
            System.out.println(" Table " + tableNumber + " is already occupied.");
            return false;
        }
    }

    public void releaseTable() {
        status = TableStatus.AVAILABLE;
        System.out.println(" Table " + tableNumber + " is now available.");
    }

    @Override
    public String toString() {
        return "Table #" + tableNumber +
               " | Capacity: " + capacity +
               " | Status: " + (status == TableStatus.AVAILABLE ? " Available" : " Occupied");
    }
}
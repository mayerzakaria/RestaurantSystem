package restaurantsystem1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

enum TableStatus {
    AVAILABLE,
    OCCUPIED
}

public class Table implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int tableNumber;
    private int capacity;
    private TableStatus status;

    public Table(int tableNumber, int capacity, TableStatus status) {
        setTableNumber(tableNumber);
        setCapacity(capacity);
        setStatus(status);
    }
    
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


    
    
    public boolean isAvailable()
    {
        return status == TableStatus.AVAILABLE;
    }
       
 public static Table selectTable(ArrayList<Table> tables, Scanner scanner) {
        System.out.println("\n========== AVAILABLE TABLES ==========");
        ArrayList<Table> availableTables = new ArrayList<>();

        for (Table table : tables) {
            if (table.isAvailable()) {
                availableTables.add(table);
                System.out.println(availableTables.size() + ". " + table.toString());
            }
        }

        if (availableTables.isEmpty()) {
            System.out.println("No available tables!");
            return null;
        }

        System.out.print("Select table number: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > availableTables.size()) {
            System.out.println("Invalid choice!");
            return null;
        }

        Table selected = availableTables.get(choice - 1);
        selected.assignTable();
        return selected;
    }

   
   
    public boolean assignTable() 
    {
        if (this.isAvailable()) {
            status = TableStatus.OCCUPIED;
            System.out.println("Table " + tableNumber + " has been assigned.");
            return true;
        } else {
            System.out.println("Table " + tableNumber + " is already occupied.");
            return false;
        }
    }

    public void releaseTable() 
    {
        status = TableStatus.AVAILABLE;
        System.out.println("Table " + tableNumber + " is now available.");
    }

    @Override
    public String toString() {
        return "Table #" + tableNumber +
               " | Capacity: " + capacity +
               " | Status: " + (status == TableStatus.AVAILABLE ? "Available" : "Occupied");
    }
}
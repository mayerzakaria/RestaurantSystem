/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurantsystem;

enum TableStatus {
    AVAILABLE,
    OCCUPIED
} 
public class Table 
{
     private int tableNumber,capacity;
    private TableStatus status;
    
    public Table(int tableNumber,int capacity,TableStatus status){
   setTableNumber(tableNumber);
   setCapacity(capacity);
   setStatus (status);
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

    public boolean isAvailable() 
    {
        if(status==TableStatus.AVAILABLE){
            return true;
    }
        else
            return false;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public TableStatus getStatus() {
        return status;
    }
    
    public void assignTable()
    {
        if(this.isAvailable()){
            status=TableStatus.OCCUPIED;
            System.out.println("Table "+tableNumber+" has been Assigned.");
        }
        else {
        System.out.println("Table "+tableNumber+" is already occupied ");
        }
    }
    public void releaseTable()
    {
    status=TableStatus.AVAILABLE;
    System.out.println("Table "+tableNumber+"  is available now.");
    }

    @Override
    public String toString() {
        return "Table{" + "tableNumber=" + tableNumber + ", capacity=" + capacity + ", status=" + status + '}';
    }
    

}

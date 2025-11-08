package restaurantsystem1;

import java.util.*;
import java.io.Serializable;
import java.util.Map;

public class Cashier extends Person implements Serializable {
    private static final long serialVersionUID = 1L;

   // private String employeeId;
    private double salary;
    private String shift;
    private Systemmanager systemManager;
    private Table assignedTable;
    private static int empCounter = 1;

   
   // public Cashier(String name, String email, String phoneNumber, double salary, String shift, Systemmanager systemManager) {
     //   super(name, email, phoneNumber);
    public Cashier(String name, String email, String phoneNumber, String password, double salary, String shift, Systemmanager systemManager) {
        super(name, email, phoneNumber, password);
        this.id = "CH" + String.format("%03d", empCounter++);
        setSalary(salary);
        setShift(shift);
        setSystemManager(systemManager);
    }
        // Getters & Setters
    public String getEmployeeId() { return id; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }
    public Systemmanager getSystemManager() { return systemManager; }
    public void setSystemManager(Systemmanager systemManager) { this.systemManager = systemManager; }
    public Table getAssignedTable() { return assignedTable; }
    public void setAssignedTable(Table assignedTable) { this.assignedTable = assignedTable; }
    

    
   @Override
    public boolean login(String inputId, String inputPassword) {
        if (super.login(inputId, inputPassword)) {
            System.out.println("Cashier ID: " + id);
            System.out.println("Shift: " + shift);
            return true;
        }
        return false;
    }
    

 
 public Order processTakeawayOrder(Customer customer, Map<MenuItem, Integer> items) {
        System.out.println("\n=== PROCESSING TAKEAWAY ORDER ===");
        System.out.println("Cashier: " + getName());
        System.out.println("Customer: " + customer.getName());

        Order order = new Order(customer, items, Systemmode.TAKEAWAY, null);
        order.calculateSubtotal();
        order.applyEliteDiscount();
        order.calculateTotal();
        order.updateStatus(Status.COMPLETE);

        System.out.println("Takeaway order processed successfully!");
       System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Total: EGP " + String.format("%.2f", order.getTotal()));

        return order;
    }
   public Order processWalkInOrder(Customer customer, Map<MenuItem, Integer> items, Table table) {
        System.out.println("\n=== PROCESSING DINE-IN ORDER ===");
        System.out.println("Cashier: " + getName());
        System.out.println("Customer: " + customer.getName());
        System.out.println("Table: #" + table.getTableNumber());

        customer.incrementDineInCount();
        setAssignedTable(table);
        Order order = new Order(customer, items, Systemmode.DINE_IN, table);
        order.calculateSubtotal();
        order.applyEliteDiscount();
        order.calculateTotal();
        order.updateStatus(Status.COMPLETE);

        System.out.println("Dine-in order processed successfully!");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Total: EGP " + String.format("%.2f", order.getTotal()));

        return order;
    }

   
  public boolean acceptPayment(Order order, double paymentAmount, PaymentMethod paymentMethod) {
    if (order == null) {
        System.out.println("Error: No order to process payment for!");
        return false;
    }

    Payment payment = new Payment(paymentAmount,  paymentMethod, order.getOrderId());
    order.setPayment(payment);
    boolean success = payment.processPayment(order.getTotal());

    if (success) {
        order.updateStatus(Status.COMPLETE);
    } else {
        order.updateStatus(Status.FAILED);
    }

    return success;
}



    // Print receipt
    public void printReceipt(Order order) {
        System.out.println("\n===== RECEIPT =====");
        System.out.println("Cashier: " + getName() + " (" + id + ")");
        System.out.println("Customer: " + (order.getCustomer() != null ? order.getCustomer().getName() : "N/A"));
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Items Ordered:");

        for (Map.Entry<MenuItem, Integer> entry : order.getItems().entrySet()) {
            MenuItem item = entry.getKey();
            int qty = entry.getValue();
            System.out.println(" - " + item.getName() + " x" + qty + "  EGP " + (item.getPrice() * qty));
        }

        System.out.println("Subtotal: EGP " + order.getSubtotal());
        System.out.println("Discount: EGP " + order.getDiscountAmount());
        System.out.println("Total: EGP " + order.getTotal());
        System.out.println("Payment Status: " + (order.getPayment() != null ? order.getPayment().getStatus() : "No Payment"));
        System.out.println("====================\n");
    }

   
    public void assignTable(Table table) {
        this.assignedTable = table;
        System.out.println("Cashier " + getName() + " assigned to manage table " + table.getTableNumber());
    }

    
    @Override
    public String getDetails() {
        return "Employee ID: " + id +
               "\nName: " + getName() +
               "\nEmail: " + getEmail() +
               "\nPhone: " + getPhoneNumber() +
               "\nShift: " + shift +
               "\nSalary: EGP " + salary;
    }


}

package restaurantsystem;

import java.util.*;

public class Cashier extends Person {

    private String employeeId;
    private double salary;
    private String shift;
    private Systemmanager systemManager;
    private Table assignedTable;

    private static int empCounter = 1;

    // Constructor
    public Cashier(String name, String email, String phoneNumber, double salary, String shift, Systemmanager systemManager) {
        super(name, email, phoneNumber);
        this.employeeId = "EMP" + String.format("%03d", empCounter++);
        setSalary(salary);
        setShift(shift);
        setSystemManager(systemManager);
    }

    // Process a walk-in order
    public Order processWalkInOrder(Customer customer, Map<MenuItem, Integer> items, Table table) {
        System.out.println("Processing walk-in order for: " + customer.getName());

        Order order = new Order(customer, items, Systemmode.walk_in, table);
        order.calculateSubtotal();
        order.applyEliteDiscount();
        order.calculateTotal();

        System.out.println("Order processed successfully for " + customer.getName());
        return order;
    }

    // Accept payment
 public void acceptPayment(Order order, double paymentAmount,String paymentMethod) {
    if (order.getPayment() == null) {
        // Create new Payment object with correct parameters
        Payment payment = new Payment(paymentAmount, paymentMethod, order);
        order.setPayment(payment);
    }

    if (paymentAmount >= order.getTotal()) {
        order.getPayment().setStatus("Completed");
        System.out.println("Payment of EGP " + paymentAmount + " accepted. Change: EGP " + (paymentAmount - order.getTotal()));
    } else {
        order.getPayment().setStatus("Pending");
        System.out.println("Insufficient payment! EGP " + (order.getTotal() - paymentAmount) + " remaining.");
    }
}


    // Print receipt
    public void printReceipt(Order order) {
        System.out.println("\n===== RECEIPT =====");
        System.out.println("Cashier: " + getName() + " (" + employeeId + ")");
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

    // Assign a table
    public void assignTable(Table table) {
        this.assignedTable = table;
        System.out.println("Cashier " + getName() + " assigned to manage table " + table.getTableNumber());
    }

    // Get cashier details
    @Override
    public String getDetails() {
        return "Employee ID: " + employeeId +
               "\nName: " + getName() +
               "\nEmail: " + getEmail() +
               "\nPhone: " + getPhoneNumber() +
               "\nShift: " + shift +
               "\nSalary: EGP " + salary;
    }

    // Getters & Setters
    public String getEmployeeId() { return employeeId; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }
    public Systemmanager getSystemManager() { return systemManager; }
    public void setSystemManager(Systemmanager systemManager) { this.systemManager = systemManager; }
    public Table getAssignedTable() { return assignedTable; }
    public void setAssignedTable(Table assignedTable) { this.assignedTable = assignedTable; }
}

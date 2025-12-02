package restaurantsystem;

import java.util.*;

public class Cashier extends Person {
    private static int empCounter = 1;
  
    private double salary;
    private String shift;
    private Table assignedTable;

    public Cashier(String name, String email, String phoneNumber, String password,
                   double salary, String shift) {
        super(name, email, phoneNumber, password);
        this.id = "CH" + String.format("%03d", empCounter++);
        this.salary = salary;
        this.shift = shift;
    
    
    }


    
    

    public String getEmployeeId() { return id; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }
    public Table getAssignedTable() { return assignedTable; }
    public void setAssignedTable(Table assignedTable) { this.assignedTable = assignedTable; }
    
    public static Cashier findCashier(String id, ArrayList<Cashier> cashiers) {
        for (Cashier c : cashiers) {
            if (c.getEmployeeId().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }
    
    @Override
    public boolean login(String inputId, String inputPassword) {
        if (super.login(inputId, inputPassword)) {
            System.out.println(" Cashier ID: " + id);
            System.out.println(" Shift: " + shift);
            return true;
        }
        return false;
    }

    public Order processTakeawayOrder(Customer customer, Map<MenuItem, Integer> items) {
        System.out.println("\n=== PROCESSING TAKEAWAY ORDER ===");
        System.out.println(" Cashier: " + getName());
        System.out.println(" Customer: " + customer.getName());

        Order order = new Order(customer.getCustomerId(), items, Systemmode.TAKEAWAY, (Table) null);
        order.calculateSubtotal();
        order.applyEliteDiscount(customer.isEliteCustomer(), customer.isSubscriptionActive());
        order.calculateTotal();

        System.out.println(" Takeaway order processed!");
        order.updateStatus(Status.COMPLETE);
        System.out.println(" Order ID: " + order.getOrderId());
        System.out.println(" Total: EGP " + order.getTotal());
        
        return order;
    }

    public Order processWalkInOrder(Customer customer, Map<MenuItem, Integer> items, 
                               ArrayList<Table> tables, Scanner scanner) {
        System.out.println("\n=== PROCESSING DINE-IN ORDER ===");
        System.out.println(" Cashier: " + getName());
        System.out.println(" Customer: " + customer.getName());
    
        System.out.print("\n Enter number of people: ");
        int numberOfPeople;
    
        try {
            numberOfPeople = scanner.nextInt();
            scanner.nextLine(); 
        
            if (numberOfPeople <= 0) {
                System.out.println(" Invalid number of people!");
                return null;
            }
        } catch (InputMismatchException e) {
            System.out.println(" Invalid input!");
            scanner.nextLine();
            return null;
        }
    
        Table selectedTable = Table.selectTableForCapacity(tables, scanner, numberOfPeople);
    
        if (selectedTable == null) {
            System.out.println(" No suitable table available. Order cancelled.");
            return null;
        }

        System.out.println(" Table #" + selectedTable.getTableNumber() + " assigned");
    
        this.assignedTable = selectedTable; 
        customer.incrementDineInCount();

        Order order = new Order(customer.getCustomerId(), items, Systemmode.DINE_IN, selectedTable);
        order.calculateSubtotal();
        order.applyEliteDiscount(customer.isEliteCustomer(), customer.isSubscriptionActive());
        order.calculateTotal();

        System.out.println(" Dine-in order processed successfully!");
        System.out.println(" Order ID: " + order.getOrderId());
        System.out.println(" Total: EGP " + String.format("%.2f", order.getTotal()));
    
        return order;
    }

    public boolean acceptPayment(Order order, double paymentAmount, Payment.PaymentMethod paymentMethod) {
        if (order == null) {
            System.out.println(" Error: No order!");
            return false;
        }

        Payment payment = new Payment(paymentAmount, paymentMethod, order.getOrderId());
        boolean success = payment.processPayment(order.getTotal());

        if (success) {
            order.setPayment(payment);
            order.updateStatus(Status.COMPLETE);
        } else {
            order.updateStatus(Status.FAILED);
        }

        return success;
    }

    public void printReceipt(Order order) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                     RECEIPT");
        System.out.println("=".repeat(60));
        System.out.println(" Cashier: " + getName() + " (" + id + ")");
        System.out.println(" Order ID: " + order.getOrderId());
        
        if (order.getOrderDate() != null) {
            java.time.format.DateTimeFormatter dateFormatter = 
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm a");
            System.out.println(" Date: " + order.getOrderDate().format(dateFormatter));
        }
        
        System.out.println("-".repeat(60));
        System.out.println("Items Ordered:");

        for (Map.Entry<MenuItem, Integer> entry : order.getItems().entrySet()) {
            MenuItem item = entry.getKey();
            int qty = entry.getValue();
            double itemTotal = item.getPrice() * qty;
            System.out.println(String.format("  - %-30s x%-3d  EGP %7.2f",
                                           item.getName(), qty, itemTotal));
        }

        System.out.println("-".repeat(60));
        System.out.println(String.format("Subtotal:                            EGP %7.2f",
                                        order.getSubtotal()));
        System.out.println(String.format("Discount:                            EGP %7.2f",
                                        order.getDiscountAmount()));
        System.out.println(String.format("TOTAL:                               EGP %7.2f",
                                        order.getTotal()));
        System.out.println("-".repeat(60));
        System.out.println("Payment Status: " +
                         (order.getPayment() != null ? order.getPayment().getStatus() : "Pending"));
        System.out.println("=".repeat(60));
    }

    @Override
    public String getDetails() {
        return "Employee ID: " + id +
               "\n Name: " + getName() +
               "\n Email: " + getEmail() +
               "\n Phone: " + getPhoneNumber() +
               "\n Shift: " + shift +
               "\n Salary: EGP " + salary;
    }
}
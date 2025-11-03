package restaurantsystem;

import java.util.*;

public class Order {
    private static int orderCounter = 1;
    private int orderId;
    private Date orderDate;
    private Map<MenuItem, Integer> items;
    private double subtotal;
    private double discountAmount;
    private double total;
    private String status;
    private Payment payment;
    private Address deliveryAddress;
    private Table table;
    private Systemmode orderType;
    private Customer customer; // ✅ Added link to Customer

    // ✅ Updated constructor to include Customer and items
    public Order(Customer customer, Map<MenuItem, Integer> items, Systemmode orderType, Table table) {
        this.orderId = orderCounter++;
        this.orderDate = new Date();
        this.items = new HashMap<>(items);
        this.customer = customer;
        this.orderType = orderType;
        this.table = table;
        this.status = "Pending";
        calculateSubtotal();
    }

    //  Keep your original constructor too
    public Order(Systemmode orderType) {
        this.orderId = orderCounter++;
        this.orderDate = new Date();
        this.items = new HashMap<>();
        this.status = "Pending";
        setOrderType(orderType);
    }

    // Add item
    public void addItem(MenuItem item, int quantity) {
        if (items.containsKey(item)) {
            items.put(item, items.get(item) + quantity);
        } else {
            items.put(item, quantity);
        }
        calculateSubtotal();
    }

    // Remove item
    public void removeItem(MenuItem item) {
        items.remove(item);
        calculateSubtotal();
    }

    // Update item quantity
    public void updateQuantity(MenuItem item, int newQuantity) {
        if (items.containsKey(item)) {
            items.put(item, newQuantity);
            calculateSubtotal();
        } else {
            System.out.println("Item not found in order.");
        }
    }

    // Calculate subtotal
    public void calculateSubtotal() {
        subtotal = 0;
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            subtotal += entry.getKey().getPrice() * entry.getValue();
        }
    }

    // Apply elite discount
    public void applyEliteDiscount() {
        if (customer != null && customer.isEliteCustomer()) {
            discountAmount = subtotal * 0.10;
        } else {
            discountAmount = 0;
        }
    }

    // Calculate total
    public void calculateTotal() {
        total = subtotal - discountAmount;
    }

    // Update order status
    public void updateStatus(String newStatus) {
        status = newStatus;
        System.out.println("Order status updated to: " + status);
    }

    // Generate summary
    public String getOrderSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("----- ORDER SUMMARY -----\n");
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("Date: ").append(orderDate).append("\n");
        sb.append("Type: ").append(orderType).append("\n");
        sb.append("Customer: ").append(customer != null ? customer.getName() : "N/A").append("\n");
        sb.append("Items:\n");

        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            sb.append("- ").append(entry.getKey().getName())
              .append(" × ").append(entry.getValue())
              .append(" = ").append(entry.getKey().getPrice() * entry.getValue()).append(" EGP\n");
        }

        sb.append("Subtotal: ").append(subtotal).append(" EGP\n");
        sb.append("Discount: ").append(discountAmount).append(" EGP\n");
        sb.append("Total: ").append(total).append(" EGP\n");
        sb.append("Status: ").append(status).append("\n");

        if (deliveryAddress != null) {
            sb.append("Delivery Address: ").append(deliveryAddress.toString()).append("\n");
        }
        if (table != null) {
            sb.append("Table: ").append(table.getTableNumber()).append("\n");
        }
        sb.append("--------------------------");
        return sb.toString();
    }

    // For online orders
    public void deliveryOrder() {
        if (orderType == Systemmode.online_delivery) {
            System.out.println("Delivering order to: " + deliveryAddress);
        } else {
            System.out.println("This is not an online order.");
        }
    }

    // Getters and setters
    public int getOrderId() { return orderId; }
    public double getTotal() { return total; }
    public double getSubtotal() { return subtotal; }
    public double getDiscountAmount() { return discountAmount; }
    public Payment getPayment() { return payment; }
    public Map<MenuItem, Integer> getItems() { return items; }
    public Customer getCustomer() { return customer; }

    public void setDeliveryAddress(Address deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setPayment(Payment payment) { this.payment = payment; }
    public void setTable(Table table) { this.table = table; }
    public void setOrderType(Systemmode orderType) { this.orderType = orderType; }
}

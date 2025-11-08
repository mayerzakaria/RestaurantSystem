package restaurantsystem1;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int onlineOrderCounter = 1;
    private static int dineInOrderCounter = 1;
    private static int takeawayOrderCounter = 1;
    private static LocalDate lastResetDate = LocalDate.now();

    private int orderId;
    private LocalDateTime orderDate;
    private Map<MenuItem, Integer> items;
    private double subtotal;
    private double discountAmount;
    private double total;
    private Status status;
    private Payment payment;
    private Address deliveryAddress;
    private Table table;
    private Systemmode orderType;
    private Customer customer;

    // ===== Constructor for a full order =====
    public Order(Customer customer, Map<MenuItem, Integer> items, Systemmode orderType, Table table) {
        this.orderId = getNextOrderId(orderType);
        this.orderDate = LocalDateTime.now();
        this.items = new HashMap<>(items);
        this.customer = customer;
        this.orderType = orderType;
        this.table = table;
        this.status = Status.PENDING;
        calculateSubtotal();
        applyEliteDiscount();
        calculateTotal();
    }

    // ===== Constructor for an empty order =====
    public Order(Systemmode orderType) {
        this.orderId = getNextOrderId(orderType);
        this.orderDate = LocalDateTime.now();
        this.items = new HashMap<>();
        this.orderType = orderType;
        this.status = Status.PENDING;
    }
    
    // Inside Order class
public int getOrderId() { return orderId; }
public LocalDateTime getOrderDate() { return orderDate; }
public Map<MenuItem, Integer> getItems() { return items; }
public double getSubtotal() { return subtotal; }
public double getDiscountAmount() { return discountAmount; }
public double getTotal() { return total; }
public Status getStatus() { return status; }
public void setPayment(Payment payment) { this.payment = payment; }
public Payment getPayment() { return payment; }
public Address getDeliveryAddress() { return deliveryAddress; }
public void setDeliveryAddress(Address deliveryAddress) { this.deliveryAddress = deliveryAddress; }
public Table getTable() { return table; }
public Customer getCustomer() { return customer; }
public Systemmode getOrderType() { return orderType; }

    

    // ===== Order ID generator per type =====
    private static int getNextOrderId(Systemmode orderType) {
        resetNumberOfOrders(); // reset counters if needed daily
        switch (orderType) {
            case ONLINE_DELIVERY: return onlineOrderCounter++;
            case TAKEAWAY: return takeawayOrderCounter++;
            case DINE_IN:
            default: return dineInOrderCounter++;
        }
    }

    // ===== Reset daily counters at 8:00 AM =====
    private static void resetNumberOfOrders() {
        LocalDate today = LocalDate.now();
        if (today.isAfter(lastResetDate)) {
            onlineOrderCounter = 1;
            dineInOrderCounter = 1;
            takeawayOrderCounter = 1;
            lastResetDate = today;
            System.out.println("New order day started. Order counters reset.");
        }
    }

    // ===== Item management =====
    public void addItem(MenuItem item, int quantity) {
        if (item == null || quantity <= 0) return;
        items.put(item, items.getOrDefault(item, 0) + quantity);
        calculateSubtotal();
        applyEliteDiscount();
        calculateTotal();
    }

    public void removeItem(MenuItem item) {
        if (item == null) return;
        items.remove(item);
        calculateSubtotal();
        applyEliteDiscount();
        calculateTotal();
    }

    public void updateQuantity(MenuItem item, int newQuantity) {
        if (item == null || newQuantity <= 0) return;
        if (items.containsKey(item)) {
            items.put(item, newQuantity);
            calculateSubtotal();
            applyEliteDiscount();
            calculateTotal();
        }
    }

    // ===== Calculations =====
    public void calculateSubtotal() {
        subtotal = 0;
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            subtotal += entry.getKey().getPrice() * entry.getValue();
        }
    }

    public void applyEliteDiscount() {
        if (customer != null && customer.isEliteCustomer() && customer.isSubscriptionActive()) {
            discountAmount = subtotal * 0.10;
        } else {
            discountAmount = 0;
        }
    }

    public void calculateTotal() {
        total = subtotal - discountAmount;
    }

    // ===== Status update =====
    public void updateStatus(Status newStatus) {
        status = newStatus;
        System.out.println("Order status updated to: " + status);
    }

    // ===== Delivery helper =====
    public void deliveryOrder() {
        if (orderType == Systemmode.ONLINE_DELIVERY) {
            if (deliveryAddress != null) {
                System.out.println("Delivering order #" + orderId + " to: " + deliveryAddress.getFulladdress());
            } else {
                System.out.println("Error: No delivery address set for this order!");
            }
        } else {
            System.out.println("This is not an online delivery order.");
        }
    }

    // ===== Order Summary =====
    public String getOrderSummary() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=".repeat(60)).append("\n");
        sb.append("ORDER SUMMARY\n");
        sb.append("=".repeat(60)).append("\n");
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("Date: ").append(orderDate.format(formatter)).append("\n");
        sb.append("Type: ").append(orderType).append("\n");
        sb.append("Customer: ").append(customer != null ? customer.getName() : "N/A").append("\n");
        sb.append("-".repeat(60)).append("\n");
        sb.append("Items:\n");
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            MenuItem item = entry.getKey();
            sb.append(String.format("  - %-25s x%-3d = %.2f EGP\n",
                    item.getName(), entry.getValue(), item.getPrice() * entry.getValue()));
        }
        sb.append("-".repeat(60)).append("\n");
        sb.append("Subtotal: ").append(subtotal).append(" EGP\n");
        sb.append("Discount: ").append(discountAmount).append(" EGP\n");
        sb.append("Total: ").append(total).append(" EGP\n");
        sb.append("Status: ").append(status).append("\n");
        if (deliveryAddress != null) sb.append("Delivery Address: ").append(deliveryAddress.getFulladdress()).append("\n");
        if (table != null) sb.append("Table: #").append(table.getTableNumber()).append("\n");
        sb.append("=".repeat(60)).append("\n");
        return sb.toString();
    }

  
}

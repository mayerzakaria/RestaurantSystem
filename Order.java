package restaurantsystem1;

import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Order {
   private static int onlineOrderCounter = 1;
    private static int dineInOrderCounter = 1;
    private static int takeawayOrderCounter = 1;
    private static LocalDate lastResetDate = LocalDate.now();
    private int orderId;
    private Date orderDate;
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

   
    public Order(Customer customer, Map<MenuItem, Integer> items, Systemmode orderType, Table table) {
        this.orderId = getNextOrderId(orderType);
        this.orderDate = new Date();
        this.items = new HashMap<>(items);
        this.customer = customer;
        this.orderType = orderType;
        this.table = table;
        this.status = Status.PENDING;
        calculateSubtotal();
    }
     private static int getNextOrderId(Systemmode orderType) {
        switch (orderType) {
            case online_delivery:
                return onlineOrderCounter++;
            case takeAway:
                return takeawayOrderCounter++;
            case walk_in:
            default:
                return dineInOrderCounter++;
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

   
    
    
    public Order(Systemmode orderType) {
        this.orderId = getNextOrderId(orderType);
        this.orderDate = new Date();
        this.items = new HashMap<>();
        this.status = Status.PENDING;
        setOrderType(orderType);
    }

    private static void resetNumberOfOrders() {
    LocalDateTime now = LocalDateTime.now();
    LocalTime resetTime = LocalTime.of(8, 0); 

    
    if (now.toLocalDate().isAfter(lastResetDate) && now.toLocalTime().isAfter(resetTime)) {
        onlineOrderCounter = 1;
        dineInOrderCounter = 1;
        takeawayOrderCounter = 1;
        lastResetDate = now.toLocalDate();
        System.out.println("New order day started. Order counter reset");
    }
}
  public void addItem(MenuItem item, int quantity) {
        if (item == null) {
            System.out.println("Cannot add null item!");
            return;
        }
        if (quantity <= 0) {
            System.out.println(" Quantity must be greater than 0!");
            return;
        }

        if (items.containsKey(item)) {
            items.put(item, items.get(item) + quantity);
            System.out.println(" Updated quantity for " + item.getName());
        } else {
            items.put(item, quantity);
            System.out.println(" Added " + item.getName() + " x" + quantity);
        }
        calculateSubtotal();
    }

    
     public void removeItem(MenuItem item) {
        if (item == null) {
            System.out.println(" Cannot remove null item!");
            return;
        }
        if (items.remove(item) != null) {
            System.out.println(" Removed " + item.getName() + " from order");
            calculateSubtotal();
        } else {
            System.out.println(" Item not found in order");
        }
    }

   
  public void updateQuantity(MenuItem item, int newQuantity) {
        if (item == null) {
            System.out.println("Cannot update null item!");
            return;
        }
        if (newQuantity <= 0) {
            System.out.println(" Quantity must be greater than 0!");
            return;
        }

        if (items.containsKey(item)) {
            items.put(item, newQuantity);
            System.out.println(" Updated " + item.getName() + " quantity to " + newQuantity);
            calculateSubtotal();
        } else {
            System.out.println(" Item not found in order.");
        }
    }

   
    public void calculateSubtotal() {
        subtotal = 0;
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            subtotal += entry.getKey().getPrice() * entry.getValue();
        }
    }

  
   public void applyEliteDiscount() {
    if (customer != null && customer.isEliteCustomer() && customer.isSubscriptionActive()) {
        discountAmount = subtotal * 0.10;
        System.out.println(" Elite discount (10%) applied: EGP " + String.format("%.2f", discountAmount));
    } else 
    {
        discountAmount = 0;
        if (customer != null && customer.isEliteCustomer() && !customer.isSubscriptionActive()) {
            System.out.println(" Elite membership expired. Renew to get 10% discount!");
        }
    }
}

   
    public void calculateTotal() {
        total = subtotal - discountAmount;
    }

  
    public void updateStatus(Status newStatus) {
        status = newStatus;
        System.out.println("Order status updated to: " + status);
    }

    
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

   
    public void deliveryOrder() {
        if (orderType == Systemmode.online_delivery) {
            if (deliveryAddress != null) {
                System.out.println("Delivering order " + orderId + " to: " + deliveryAddress.getFulladdress());
            } else {
                System.out.println(" Error: No delivery address set for this order!");
            }
        } else {
            System.out.println(" This is not an online delivery order.");
        }
    }

   
}

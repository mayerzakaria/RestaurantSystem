package restaurantsystem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static int orderCounter = 1;
    
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
    private String customerId;
    
    private Delivery assignedDelivery;
    private double deliveryFee;
    private String deliveryZone;

    public Order(String customerId, Map<MenuItem, Integer> items, 
                 Systemmode orderType, Table table) {
        this.orderId = orderCounter++;
        this.orderDate = LocalDateTime.now();
        this.items = new HashMap<>(items);
        this.customerId = customerId;
        this.orderType = orderType;
        this.table = table;
        this.status = Status.PENDING;
        this.deliveryAddress = null;
        this.deliveryFee = 0.0;
        this.deliveryZone = null;
        this.assignedDelivery = null;
        calculateSubtotal();
    }
    
    public Order(String customerId, Map<MenuItem, Integer> items, 
             Systemmode orderType, Address deliveryAddress) {
        this.orderId = orderCounter++;
        this.orderDate = LocalDateTime.now();
        this.items = new HashMap<>(items);
        this.customerId = customerId;
        this.orderType = orderType;
        this.deliveryAddress = deliveryAddress;
        this.status = Status.PENDING;
        this.table = null;
    
        calculateSubtotal();
    
        if (orderType == Systemmode.ONLINE_DELIVERY && deliveryAddress != null) {
            this.deliveryZone = Delivery.determineZone(deliveryAddress);
            if (deliveryZone != null) {
                this.deliveryFee = Delivery.getBaseFee(deliveryZone);
            } else {
                this.deliveryFee = 0.0;
                this.status = Status.CANCELLED;  
                orderCounter--;  
            }
        } else {
            this.deliveryFee = 0.0;
            this.deliveryZone = null;
        }
    
        this.assignedDelivery = null;
}

    public int getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Map<MenuItem, Integer> getItems() {
        return items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getTotal() {
        return total;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Systemmode getOrderType() {
        return orderType;
    }

    public String getCustomerId() {
        return customerId;
    }
    
    public Delivery getAssignedDelivery() {
        return assignedDelivery;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public String getDeliveryZone() {
        return deliveryZone;
    }

    public void addItem(MenuItem item, int quantity) {
        if (item == null) {
            System.out.println("Cannot add null item!");
            return;
        }
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0!");
            return;
        }

        if (items.containsKey(item)) {
            items.put(item, items.get(item) + quantity);
            System.out.println("Updated quantity for " + item.getName());
        } else {
            items.put(item, quantity);
            System.out.println("Added " + item.getName() + " x" + quantity);
        }
        calculateSubtotal();
    }

    public void removeItem(MenuItem item) {
        if (item == null) {
            System.out.println("Cannot remove null item!");
            return;
        }
        if (items.remove(item) != null) {
            System.out.println("Removed " + item.getName() + " from order");
            calculateSubtotal();
        } else {
            System.out.println("Item not found in order");
        }
    }

    public void updateQuantity(MenuItem item, int newQuantity) {
        if (item == null) {
            System.out.println("Cannot update null item!");
            return;
        }
        if (newQuantity <= 0) {
            System.out.println("Quantity must be greater than 0!");
            return;
        }

        if (items.containsKey(item)) {
            items.put(item, newQuantity);
            System.out.println("Updated " + item.getName() + " quantity to " + newQuantity);
            calculateSubtotal();
        } else {
            System.out.println("Item not found in order.");
        }
    }

    public void calculateSubtotal() {
        subtotal = 0;
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            subtotal += entry.getKey().getPrice() * entry.getValue();
        }
    }

    public void applyEliteDiscount(boolean isElite, boolean isActive) {
        if (isElite && isActive) {
            discountAmount = subtotal * 0.10;
            System.out.println("Elite discount (10%) applied: EGP " + 
                             String.format("%.2f", discountAmount));
        } else {
            discountAmount = 0;
            if (isElite && !isActive) {
                System.out.println("Elite membership expired. Renew to get 10% discount!");
            }
        }
    }

    public void calculateTotal() {
        total = subtotal - discountAmount + deliveryFee;
    }

    public void updateStatus(Status newStatus) {
        status = newStatus;
        System.out.println("Order status updated to: " + status);
    }
    
    public void assignDeliveryPerson(Delivery delivery) {
        if (orderType != Systemmode.ONLINE_DELIVERY) {
            System.out.println("This order type doesn't need delivery!");
            return;
        }
        
        if (deliveryAddress == null) {
            System.out.println("No delivery address specified!");
            return;
        }
        
        if (deliveryZone == null) {
            System.out.println("Delivery is unavailable for this area!");
            return;
        }
        
        this.assignedDelivery = delivery;
        System.out.println("Delivery assigned to: " + delivery.getName());
    }
    
    public void removeDeliveryPerson() {
        if (assignedDelivery != null) {
            System.out.println("Delivery person " + assignedDelivery.getName() + " removed from order");
            this.assignedDelivery = null;
        }
    }
    
    public boolean hasDeliveryPerson() {
        return assignedDelivery != null;
    }
    
    public String getDeliveryInfo() {
        if (orderType != Systemmode.ONLINE_DELIVERY) {
            return "This is not a delivery order";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("\nDELIVERY INFORMATION:\n");
        sb.append("-".repeat(50)).append("\n");
        sb.append("Zone: ").append(deliveryZone != null ? 
            Delivery.getZoneName(deliveryZone) : "Not determined").append("\n");
        sb.append("Delivery Fee: EGP ").append(String.format("%.2f", deliveryFee)).append("\n");
        sb.append("Address: ").append(deliveryAddress != null ? 
            deliveryAddress.getFullAddress() : "Not specified").append("\n");
        
        if (assignedDelivery != null) {
            sb.append("Delivery Person: ").append(assignedDelivery.getName()).append("\n");
            sb.append("Contact: ").append(assignedDelivery.getPhoneNumber()).append("\n");
            sb.append("Status: ").append(assignedDelivery.getStatus()).append("\n");
            
            if (assignedDelivery.getEstimatedDeliveryTime() != null) {
                sb.append("Estimated Time: ").append(
                    assignedDelivery.getEstimatedDeliveryTime().format(
                        DateTimeFormatter.ofPattern("HH:mm"))).append("\n");
            }
        } else {
            sb.append("Delivery Person: Not assigned yet\n");
        }
        
        sb.append("-".repeat(50)).append("\n");
        return sb.toString();
    }

    public String getOrderSummary() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        sb.append("\n").append("=".repeat(60)).append("\n");
        sb.append("                    ORDER SUMMARY\n");
        sb.append("=".repeat(60)).append("\n");
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("Date: ").append(orderDate.format(formatter)).append("\n");
        sb.append("Type: ").append(orderType).append("\n");
        sb.append("Customer ID: ").append(customerId).append("\n");
        sb.append("-".repeat(60)).append("\n");
        sb.append("Items:\n");

        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            MenuItem item = entry.getKey();
            int qty = entry.getValue();
            double itemTotal = item.getPrice() * qty;
            sb.append(String.format("  - %-25s x%-3d = EGP %7.2f\n", 
                                   item.getName(), qty, itemTotal));
        }

        sb.append("-".repeat(60)).append("\n");
        sb.append(String.format("Subtotal:        EGP %7.2f\n", subtotal));
        
        if (deliveryFee > 0) {
            sb.append(String.format("Delivery Fee:    EGP %7.2f\n", deliveryFee));
        }
        
        sb.append(String.format("Discount:        EGP %7.2f\n", discountAmount));
        sb.append(String.format("TOTAL:           EGP %7.2f\n", total));
        sb.append("-".repeat(60)).append("\n");
        sb.append("Status: ").append(status).append("\n");

        if (orderType == Systemmode.ONLINE_DELIVERY) {
            if (deliveryAddress != null) {
                sb.append("Delivery to: ").append(deliveryAddress.getFullAddress()).append("\n");
                if (deliveryZone != null) {
                    sb.append("Zone: ").append(Delivery.getZoneName(deliveryZone)).append("\n");
                }
            }
            if (assignedDelivery != null) {
                sb.append("Delivery by: ").append(assignedDelivery.getName()).append("\n");
                sb.append("Phone: ").append(assignedDelivery.getPhoneNumber()).append("\n");
            }
        }
        
        if (table != null) {
            sb.append("Table: #").append(table.getTableNumber()).append("\n");
        }
        
        sb.append("=".repeat(60)).append("\n");
        return sb.toString();
    }

    public static int getOrderCounter() {
        return orderCounter;
    }

    public static void setOrderCounter(int counter) {
        orderCounter = counter;
    }

    @Override
    public String toString() {
        return "Order #" + orderId + " [" + orderType + ", Status: " + status + 
               ", Total: EGP " + total + "]";
    }

    void updateStatus(Delivery.DeliveryStatus deliveryStatus) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getCashierId() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
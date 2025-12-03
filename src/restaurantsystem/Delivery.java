package restaurantsystem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Delivery extends Person implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private static int deliveryCounter = 1;
    
    public enum DeliveryStatus 
    {
        IDLE("Idle - Available for Orders"),
        ASSIGNED("Order Assigned"),
        PICKED_UP("Order Picked Up from Restaurant"),
        ON_THE_WAY("On the Way to Customer"),
        DELIVERED("Order Delivered"),
        OFFLINE("Offline - Not Available");
        
        private final String description;
        
        DeliveryStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
        
        @Override
        public String toString() {
            return description;
        }
    }
    
    private static final Map<String, String> ZONE_MAP = new HashMap<>();
    
    static {
        ZONE_MAP.put("Zone_A", "Downtown|20.0|20");
        ZONE_MAP.put("Zone_B", "Maadi|30.0|30");
        ZONE_MAP.put("Zone_C", "6October|50.0|45");
        ZONE_MAP.put("Zone_D", "NewCairo|40.0|35");
        ZONE_MAP.put("Zone_E", "Giza|25.0|25");
    }
    
    private int totalDeliveries;
    private Order currentOrder;
    private LocalDateTime pickupTime;
    private LocalDateTime estimatedDeliveryTime;
    private DeliveryStatus status;
    private boolean isAvailable;
    
    public Delivery(String name, String email, String phoneNumber, String password) {
        super(name, email, phoneNumber, password);
        this.id = "D" + String.format("%03d", deliveryCounter++);
        this.totalDeliveries = 0;
        this.isAvailable = true;
        this.status = DeliveryStatus.IDLE;
        this.currentOrder = null;
        this.pickupTime = null;
        this.estimatedDeliveryTime = null;
    }
    
    public void setTotalDeliveries(int totalDeliveries) {
    this.totalDeliveries = totalDeliveries;
}

public void setStatus(DeliveryStatus status) {
    this.status = status;
}

public void setPickupTime(LocalDateTime pickupTime) {
    this.pickupTime = pickupTime;
}

public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) {
    this.estimatedDeliveryTime = estimatedDeliveryTime;
}

public void setCurrentOrder(Order currentOrder) {
    this.currentOrder = currentOrder;
}

    
    
    
    public int getTotalDeliveries() {
        return totalDeliveries;
    }
    
    public Order getCurrentOrder() {
        return currentOrder;
    }
    
    public LocalDateTime getPickupTime() {
        return pickupTime;
    }
    
    public LocalDateTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }
    
    public DeliveryStatus getStatus() {
        return status;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public String getDeliveryPersonId() {
        return id;
    }
    
    public static int getDeliveryCounter() {
        return deliveryCounter;
    }
    
    public static void setDeliveryCounter(int counter) {
        deliveryCounter = counter;
    }
    
    private static String[] parseZoneData(String zoneKey) {
        String data = ZONE_MAP.get(zoneKey);
        if (data != null) {
            return data.split("\\|");
        }
        return null;
    }
    
    public static String getZoneName(String zoneKey) {
        if (zoneKey == null || !ZONE_MAP.containsKey(zoneKey)) {
            return "Unknown Zone";
        }
        String[] data = parseZoneData(zoneKey);
        return data[0];
    }
    
    public static double getBaseFee(String zoneKey) {
        if (zoneKey == null || !ZONE_MAP.containsKey(zoneKey)) {
            return 20.0;
        }
        String[] data = parseZoneData(zoneKey);
        return Double.parseDouble(data[1]);
    }
    
    public static int getDeliveryTimeMinutes(String zoneKey) {
        if (zoneKey == null || !ZONE_MAP.containsKey(zoneKey)) {
            return 30;
        }
        String[] data = parseZoneData(zoneKey);
        return Integer.parseInt(data[2]);
    }
    
    public static String determineZone(Address address) {
        if (address == null) {
            System.out.println("Delivery is unavailable for this area.");
            return null;
        }
        
        String fullAddress = address.getFullAddress().toLowerCase();
        
        if (fullAddress.contains("downtown") || fullAddress.contains("tahrir")) {
            return "Zone_A";
        } else if (fullAddress.contains("maadi")) {
            return "Zone_B";
        } else if (fullAddress.contains("october") || fullAddress.contains("6th") || 
                   fullAddress.contains("6 october")) {
            return "Zone_C";
        } else if (fullAddress.contains("new cairo") || fullAddress.contains("newcairo") ||
                   fullAddress.contains("cairo")) {
            return "Zone_D";
        } else if (fullAddress.contains("giza")) {
            return "Zone_E";
        }
        
        System.out.println("Delivery is unavailable for this area: " + address.getFullAddress());
        return null;
    }
    
    public static void displayAllZones() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                      DELIVERY ZONES");
        System.out.println("=".repeat(70));
        System.out.printf("%-10s | %-15s | %-15s | %-15s\n", 
                         "Zone", "Area", "Delivery Fee", "Est. Time");
        System.out.println("-".repeat(70));
        
        for (Map.Entry<String, String> entry : ZONE_MAP.entrySet()) {
            String[] data = entry.getValue().split("\\|");
            System.out.printf("%-10s | %-15s | EGP %-10.2f | %2s minutes\n",
                entry.getKey(), data[0], Double.parseDouble(data[1]), data[2]);
        }
        System.out.println("=".repeat(70) + "\n");
    }
    
    public static Set<String> getAllZoneKeys() {
        return ZONE_MAP.keySet();
    }
    
    public static boolean zoneExists(String zoneKey) {
        return ZONE_MAP.containsKey(zoneKey);
    }
    
    public boolean acceptOrder(Order order) {
        if (!isAvailable) {
            System.out.println(getName() + " is busy with another order!");
            return false;
        }
        
        if (order == null) {
            System.out.println("Cannot accept null order!");
            return false;
        }
        
        if (order.getOrderType() != Systemmode.ONLINE_DELIVERY) {
            System.out.println("This order is not for delivery!");
            return false;
        }
        
        if (order.getDeliveryAddress() == null) {
            System.out.println("Order has no delivery address!");
            return false;
        }
        
        String zone = determineZone(order.getDeliveryAddress());
        if (zone == null) {
            return false;
        }
        
        this.currentOrder = order;
        this.isAvailable = false;
        this.status = DeliveryStatus.ASSIGNED;
        
        System.out.println("Order " + order.getOrderId() + " assigned to " + getName());
        System.out.println("Delivery to: " + order.getDeliveryAddress().getFullAddress());
        
        return true;
    }
    
    public void pickupOrder() {
        if (currentOrder == null) {
            System.out.println("No order assigned!");
            return;
        }
        
        if (status != DeliveryStatus.ASSIGNED) {
            System.out.println("Order already picked up or in wrong state!");
            return;
        }
        
        this.pickupTime = LocalDateTime.now();
        
        String zone = determineZone(currentOrder.getDeliveryAddress());
        if (zone == null) {
            return;
        }
        
        int deliveryMinutes = getDeliveryTimeMinutes(zone);
        this.estimatedDeliveryTime = pickupTime.plusMinutes(deliveryMinutes);
        
        this.status = DeliveryStatus.PICKED_UP;
        
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println("\nOrder " + currentOrder.getOrderId() + " picked up from restaurant");
        System.out.println("Pickup Time: " + pickupTime.format(timeFormatter));
        System.out.println("Destination: " + getZoneName(zone));
        System.out.println("Delivering to: " + currentOrder.getDeliveryAddress().getFullAddress());
        System.out.println("Estimated Delivery: " + estimatedDeliveryTime.format(timeFormatter));
    }
    
    public void startDelivery() {
        if (currentOrder == null) {
            System.out.println("No order assigned!");
            return;
        }
        
        if (status != DeliveryStatus.PICKED_UP) {
            System.out.println("Order must be picked up first!");
            return;
        }
        
        this.status = DeliveryStatus.ON_THE_WAY;
        
        System.out.println(getName() + " is on the way with Order " + currentOrder.getOrderId());
    }
    
    public void completeDelivery() 
    {
        if (currentOrder == null) {
            System.out.println("No order assigned!");
            return;
        }
        
        LocalDateTime actualDeliveryTime = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("              DELIVERY COMPLETED");
        System.out.println("=".repeat(60));
        System.out.println("Delivery Person: " + getName());
        System.out.println("Order ID: " + currentOrder.getOrderId());
        System.out.println("Customer: " + currentOrder.getCustomerId());
        
        if (pickupTime != null) {
            long actualMinutes = ChronoUnit.MINUTES.between(pickupTime, actualDeliveryTime);
            System.out.println("Actual delivery time: " + actualMinutes + " minutes");
            
            if (estimatedDeliveryTime != null) {
                if (actualDeliveryTime.isAfter(estimatedDeliveryTime)) {
                    long delay = ChronoUnit.MINUTES.between(estimatedDeliveryTime, actualDeliveryTime);
                    System.out.println("Delayed by: " + delay + " minutes");
                } else {
                    long early = ChronoUnit.MINUTES.between(actualDeliveryTime, estimatedDeliveryTime);
                    System.out.println("Delivered " + early + " minutes early!");
                }
            }
        }
        
        System.out.println("Delivered at: " + actualDeliveryTime.format(timeFormatter));
        System.out.println("=".repeat(60) + "\n");
        
        currentOrder.updateStatus(Status.DELIVERED);
        
        this.status = DeliveryStatus.IDLE;
        this.totalDeliveries++;
        this.currentOrder = null;
        this.pickupTime = null;
        this.estimatedDeliveryTime = null;
        this.isAvailable = true;
        
        System.out.println(getName() + " is now available for new orders!");
        System.out.println("Total deliveries completed: " + totalDeliveries + "\n");
    }
    
    public void setAvailable(boolean available) {
        if (available && currentOrder != null) {
            System.out.println("Cannot go available while having an active order!");
            return;
        }
        
        this.isAvailable = available;
        
        if (available) {
            this.status = DeliveryStatus.IDLE;
            System.out.println(getName() + " is now AVAILABLE");
        } else {
            this.status = DeliveryStatus.OFFLINE;
            System.out.println(getName() + " is now OFFLINE");
        }
    }
    
    public String getCurrentDeliveryInfo() {
        if (currentOrder == null) {
            return "No active delivery";
        }
        
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        sb.append("\nCURRENT DELIVERY:\n");
        sb.append("-".repeat(50)).append("\n");
        sb.append("Order ID: ").append(currentOrder.getOrderId()).append("\n");
        sb.append("Customer: ").append(currentOrder.getCustomerId()).append("\n");
        sb.append("Address: ").append(currentOrder.getDeliveryAddress().getFullAddress()).append("\n");
        sb.append("Status: ").append(status).append("\n");
        
        if (pickupTime != null) {
            sb.append("Pickup Time: ").append(pickupTime.format(timeFormatter)).append("\n");
        }
        
        if (estimatedDeliveryTime != null) {
            sb.append("Estimated Delivery: ").append(estimatedDeliveryTime.format(timeFormatter)).append("\n");
        }
        
        sb.append("-".repeat(50)).append("\n");
        
        return sb.toString();
    }
    
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nDELIVERY STATISTICS:\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append("Total Deliveries: ").append(totalDeliveries).append("\n");
        sb.append("Current Status: ").append(status).append("\n");
        sb.append("Available: ").append(isAvailable ? "Yes" : "No").append("\n");
        
        if (currentOrder != null) {
            sb.append("Active Order: ").append(currentOrder.getOrderId()).append("\n");
        }
        
        sb.append("-".repeat(40)).append("\n");
        return sb.toString();
    }
    
    @Override
    public boolean login(String inputId, String inputPassword) {
        if (super.login(inputId, inputPassword)) {
            System.out.println(getName() + " logged in successfully!");
            if (status == DeliveryStatus.OFFLINE) {
                status = DeliveryStatus.IDLE;
                isAvailable = true;
            }
            return true;
        }
        System.out.println("Invalid credentials for " + getName());
        return false;
    }
    
    @Override
    public String getDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=".repeat(60)).append("\n");
        sb.append("            DELIVERY PERSON DETAILS\n");
        sb.append("=".repeat(60)).append("\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("Phone: ").append(phoneNumber).append("\n");
        sb.append("-".repeat(60)).append("\n");
        sb.append("Total Deliveries: ").append(totalDeliveries).append("\n");
        sb.append("Status: ").append(status).append("\n");
        sb.append("Available: ").append(isAvailable ? "Yes" : "No").append("\n");
        
        if (currentOrder != null) {
            sb.append("-".repeat(60)).append("\n");
            sb.append("Current Order: ").append(currentOrder.getOrderId()).append("\n");
            sb.append("Delivery Address: ").append(
                currentOrder.getDeliveryAddress().getFullAddress()).append("\n");
            
            if (pickupTime != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                sb.append("Pickup Time: ").append(pickupTime.format(formatter)).append("\n");
            }
            
            if (estimatedDeliveryTime != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                sb.append("Est. Delivery: ").append(
                    estimatedDeliveryTime.format(formatter)).append("\n");
            }
        }
        
        sb.append("=".repeat(60)).append("\n");
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "Delivery{" +
               "ID='" + id + '\'' +
               ", Name='" + name + '\'' +
               ", Status=" + status +
               ", Available=" + isAvailable +
               ", Deliveries=" + totalDeliveries +
               '}';
    }
}
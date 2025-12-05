package restaurantsystem;

import restaurantsystemdao.CustomerDAO; 
import java.sql.SQLException;            
import java.util.*;
import restaurantsystemdao.AddressDAO;

public class Customer extends Person {
    private static int idCounter = 1;
    
    private boolean isEliteCustomer;
    private Address address;
    private int dineInCount;
    private double subscriptionFee = 100.0;
    private boolean subscriptionActive;
    private int monthsRemaining;

    
    public Customer(String password, boolean isEliteCustomer,
                    Address address, String name, String email, String phoneNumber) {
        super(name, email, phoneNumber, password);
        this.id = "C" + String.format("%03d", idCounter++);
        this.isEliteCustomer = isEliteCustomer;
        this.address = address;
        this.dineInCount = 0;
        this.subscriptionActive = false;
        this.monthsRemaining = 0;
    }
    
      
    public Customer(String id, String name, String email, String phoneNumber, 
                    String password, boolean isEliteCustomer, int dineInCount,
                    boolean subscriptionActive, int monthsRemaining) {
        super(name, email, phoneNumber, password);
        this.id = id;
        this.isEliteCustomer = isEliteCustomer;
        this.dineInCount = dineInCount;
        this.subscriptionActive = subscriptionActive;
        this.monthsRemaining = monthsRemaining; 
    }

    // Getters and Setters
    public String getCustomerId() { return id; }
    
    public boolean isEliteCustomer() { return isEliteCustomer; }
    public void setEliteCustomer(boolean eliteCustomer) { this.isEliteCustomer = eliteCustomer; }
    
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    
    public int getDineInCount() { return dineInCount; }
    public void setDineInCount(int count) { this.dineInCount = count; }
    
    public double getSubscriptionFee() { return subscriptionFee; }
    
    public boolean isSubscriptionActive() { return subscriptionActive && monthsRemaining > 0; }
    public void setSubscriptionActive(boolean subscriptionActive) { 
        this.subscriptionActive = subscriptionActive; 
    }
    
    public int getMonthsRemaining() { return monthsRemaining; }
    public void setMonthsRemaining(int monthsRemaining) { this.monthsRemaining = monthsRemaining; }
    
    public void setid(String id) { this.id = id; }

    public String getId() {      
    return this.id;
}

public String getPhoneNumber() {  
    return this.phoneNumber;
}

public String getPassword() {      
    return this.password;
}

    
    
    
    // FIXED: Register customer method
    public static void registerCustomer(ArrayList<Customer> customers, Scanner scanner) {
        System.out.println("\n=== CUSTOMER REGISTRATION ===");
        
        
          // Validate Name
   
    String name;
    while (true) {
        System.out.print("Enter Name: ");
        name = scanner.nextLine().trim();
        if (name.length() < 3) {
            System.out.println("Name must be at least 3 characters.");
            continue;
        }
        if (!name.matches("^[\\p{L} ]{3,}$")) {
            System.out.println("Name cannot contain numbers or special characters.");
            continue;
        }
        break;
    }

   
    // Validate Email
    
    String email;
    while (true) {
        System.out.print("Enter Email: ");
        email = scanner.nextLine().trim();
        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("Email must contain '@' and a dot.");
            continue;
        }
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(?i:com)$")) {
            System.out.println("Email must be valid and end with .com");
            continue;
        }
        break;
    }

    
    // Validate Phone
  
    String phone;
    while (true) {
        System.out.print("Enter Phone: ");
        phone = scanner.nextLine().trim();
        if (!phone.matches("\\d+")) {
            System.out.println("Phone number must contain digits only.");
            continue;
        }
        if (phone.length() != 11) {
            System.out.println("Phone number must be exactly 11 digits.");
            continue;
        }
        if (!phone.matches("^(011|012|010|015)\\d{8}$")) {
            System.out.println("Phone number must start with 011, 012, 010, or 015.");
            continue;
        }
        break;
    }

 
    // Validate Password
   
    String password;
    while (true) {
        System.out.print("Enter Password: ");
        password = scanner.nextLine();
        if (password.length() < 6) {
            System.out.println("Password must be at least 6 characters.");
            continue;
        }
        if (!password.matches(".*[A-Z].*")) {
            System.out.println("Password must contain at least one uppercase letter.");
            continue;
        }
        if (!password.matches(".*[a-z].*")) {
            System.out.println("Password must contain at least one lowercase letter.");
            continue;
        }
        if (!password.matches(".*[^A-Za-z0-9].*")) {
            System.out.println("Password must contain at least one special character.");
            continue;
        }
        break;
    }

 
    // Validate Address
   
    String addressStr;
    while (true) {
        System.out.print("Enter Address: ");
        addressStr = scanner.nextLine().trim();
        if (addressStr.length() < 5) {
            System.out.println("Address must be at least 5 characters.");
            continue;
        }
        if (!addressStr.matches("^[A-Za-z0-9 ]{5,}$")) {
            System.out.println("Address can only contain letters, numbers, and spaces.");
            continue;
        }
        break;
    }

        
       int newAddressId = AddressDAO.getNextAddressId();
       Address address = new Address(newAddressId, addressStr, true);
        
        // Create customer object
        Customer customer = new Customer(password, false, address, name, email, phone);
        customers.add(customer);
        
        // FIXED: Save to database
        try {
            CustomerDAO dao = new CustomerDAO(); // No parameter needed
            dao.insert(customer); // Use insert() method, not addCustomer()
            System.out.println(" Customer saved in database!");
        address.setCustomerId(customer.getCustomerId());
        AddressDAO.insertAddress(address);
        System.out.println(" Address saved in database!");
        
        
        } catch (SQLException e) {
            System.out.println(" Error saving customer: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n Registration completed!");
        System.out.println("Your Customer ID: " + customer.getCustomerId());
        System.out.println(" Please login from the main menu to continue.");
    }
    
    public static Customer findCustomer(String idOrUsername, ArrayList<Customer> customers) {
        for (Customer c : customers) {
            if (c.getCustomerId().equalsIgnoreCase(idOrUsername)) {
                return c;
            }
        }
        return null;
    }
    
    @Override
    public boolean login(String inputUsername, String inputPassword)
    {
        if ((this.id.equals(inputUsername))
                && this.password.equals(inputPassword)) {
            System.out.println(" Login successful! Welcome back, " + getName() + "!");
            System.out.println(" Dine-in Count: " + dineInCount);
            System.out.println( "  Elite Status: " + (isSubscriptionActive() ? "Active" : "Not Active"));
            return true;
        }
        return false;
    }
    
    public void incrementDineInCount() {
        dineInCount++;
        System.out.println(" Dine-in recorded! Total: " + dineInCount);
        
        // Update in database
        try {
            CustomerDAO dao = new CustomerDAO();
            dao.update(this);
        } catch (SQLException e) {
            System.out.println("️ Warning: Could not update database: " + e.getMessage());
        }
        
        if (dineInCount >= 5 && !isEliteCustomer) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println(" CONGRATULATIONS! ");
            System.out.println("=".repeat(60));
            System.out.println(" You've earned FREE Elite Membership!");
            System.out.println(" 10% discount on ALL orders activated automatically!");
            System.out.println("=".repeat(60) + "\n");
            
            setEliteCustomer(true);
            setSubscriptionActive(true);
            setMonthsRemaining(1);
            
            // Update in database
            try {
                CustomerDAO dao = new CustomerDAO();
                dao.update(this);
            } catch (SQLException e) {
                System.out.println("️ Warning: Could not update elite status in database: " + e.getMessage());
            }
        }
    }
    
    public void subscribeElite(Scanner scanner) {
        System.out.println("\n=== ELITE MEMBERSHIP ===");
        System.out.println(" Fee: EGP " + subscriptionFee);
        System.out.println(" Benefits: 10% discount");
        System.out.println(" Your dine-ins: " + dineInCount + "/5");
        System.out.print("\nSubscribe? (y/n): ");
        String choice = scanner.nextLine();
        
        if (choice.equalsIgnoreCase("y")) {
            System.out.print("Process payment? (y/n): ");
            String confirm = scanner.nextLine();
            subscribeToElite(confirm.equalsIgnoreCase("y"));
        }
    }
    
    public boolean subscribeToElite(boolean paid) {
        if (paid || dineInCount >= 5) {
            setEliteCustomer(true);
            setSubscriptionActive(true);
            setMonthsRemaining(1);
            System.out.println("Elite activated! 10% discount on all orders!");
            
            // Update in database
            try {
                CustomerDAO dao = new CustomerDAO();
                dao.update(this);
            } catch (SQLException e) {
                System.out.println("️ Warning: Could not update subscription in database: " + e.getMessage());
            }
            return true;
        } else if (!paid) {
            System.out.println(" Payment required: EGP " + subscriptionFee);
            return false;
        } else {
            System.out.println(" Need 5 dine-ins. Current: " + dineInCount);
            return false;
        }
    }
    
    @Override
    public String getDetails() {
        return super.getDetails() +
               "\n Customer ID: " + id +
               "\n Elite: " + (isEliteCustomer ? "Yes" : "No") +
               "\n Subscription: " + (isSubscriptionActive() ? "Active" : "Inactive") +
               "\n Address: " + (address != null ? address.getFullAddress() : "Not set") +
               "\n️ Dine-ins: " + dineInCount;
    }
}
package restaurantsystem;
import java.util.*;

public class Customer extends Person {
    
    private static int idCounter = 1;
    private String customerId;
    private String username;
    private String password;
    private boolean isEliteCustomer;
    private Address address;
    
    private int dineInCount;          
    private double subscriptionFee = 100.0;
    private boolean subscriptionActive;
    private int monthsRemaining;  // how many months left in subscription

    
    public Customer(String username, String password, boolean isEliteCustomer,Address address, String name, String email, String phoneNumber) {
        super(name, email, phoneNumber);
      this.customerId= "cust" + idCounter++;
        setUsername(username);
        setPassword(password);
        setEliteCustomer(isEliteCustomer);
        setAddress(address);
    }

   
    public void register(String name, String email, String phoneNumber, String username, String password, Address address) {
        setName(name);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setUsername(username);
        setPassword(password);
        setAddress(address);
        this.customerId= "cust" + idCounter++;
        System.out.println("Registration successful for " + name);
    }
    
    
    
 
public boolean login() {
    Scanner sc = new Scanner(System.in);

    System.out.print("Enter username: ");
    String inputUsername = sc.nextLine();
    
    System.out.print("Enter password: ");
    String inputPassword = sc.nextLine();

    if (this.username.equals(inputUsername) && this.password.equals(inputPassword)) {

        System.out.println("Login successful! Welcome back, " + getName() + "!");
        return true;
    } else {
        System.out.println("Invalid credentials. Please try again.");
        return false;
    }
}


 
    public void updateProfile(String newEmail, String newPhone, Address newAddress) {
        setEmail(newEmail);
        setPhoneNumber(newPhone);
        setAddress(newAddress);
        System.out.println("Profile updated successfully!");
    }

   
    public void subscribeToElite(boolean paid) {
        if (paid || dineInCount > 4) {
            setEliteCustomer(true);
            setSubscriptionActive(true);
            setMonthsRemaining(1); 
            System.out.println("Elite subscription activated! Valid for " + monthsRemaining + " month(s).");
        } else {
            System.out.println("Subscription failed. Payment required or dine-in count too low.");
        }
    }

    
    public void unsubscribeFromElite() {
        setEliteCustomer(false);
        setSubscriptionActive(false);
        setMonthsRemaining(0);
        System.out.println("You have unsubscribed from Elite Membership.");
    }

   
    public boolean isSubscriptionActive() {
        return subscriptionActive && monthsRemaining > 0;
    }

    
    public String getDetails() {
        return "Customer ID: " + customerId +
               "\nName: " + getName() +
               "\nEmail: " + getEmail() +
               "\nPhone: " + getPhoneNumber() +
               "\nUsername: " + username +
               "\nElite Member: " + (isEliteCustomer ? "Yes" : "No") +
               "\nSubscription Active: " + (isSubscriptionActive() ? "Yes" : "No") +
               "\nAddress: " + address;
    }
    
    
    
    

    //  Getters and Setters 
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEliteCustomer() {
        return isEliteCustomer;
    }

    public void setEliteCustomer(boolean eliteCustomer) {
        isEliteCustomer = eliteCustomer;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getDineInCount() {
        return dineInCount;
    }

    public void setDineInCount(int dineInCount) {
        this.dineInCount = dineInCount;
    }

    public double getSubscriptionFee() {
        return subscriptionFee;
    }

    public void setSubscriptionFee(double subscriptionFee) {
        this.subscriptionFee = subscriptionFee;
    }

    public boolean getSubscriptionActive() {
        return subscriptionActive;
    }

    public void setSubscriptionActive(boolean subscriptionActive) {
        this.subscriptionActive = subscriptionActive;
    }

    public int getMonthsRemaining() {
        return monthsRemaining;
    }

    public void setMonthsRemaining(int monthsRemaining) {
        this.monthsRemaining = monthsRemaining;
    }
}

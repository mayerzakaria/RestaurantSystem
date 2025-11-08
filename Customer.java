/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurantsystem1;

import java.util.*;
import java.io.Serializable;

import java.util.Map;

public class Customer extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int idCounter = 1;

    private String username;
    private boolean isEliteCustomer;
    private Address address;
    private int dineInCount;
    private double subscriptionFee = 100.0;
    private boolean subscriptionActive;
    private int monthsRemaining;
 private static final Scanner scanner = new Scanner(System.in);
    public Customer(String username, String password, boolean isEliteCustomer,
                    Address address, String name, String email, String phoneNumber) {
        super(name, email, phoneNumber, password);
        this.id = "CUST" + String.format("%03d", idCounter++);
        this.username = username;
        this.isEliteCustomer = isEliteCustomer;
        this.address = address;
        this.dineInCount = 0;
        this.subscriptionActive = false;
        this.monthsRemaining = 0;
    }

    // ===== Getters & Setters =====
    public String getCustomerId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public boolean isEliteCustomer() { return isEliteCustomer; }
    public void setEliteCustomer(boolean eliteCustomer) { isEliteCustomer = eliteCustomer; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public int getDineInCount() { return dineInCount; }
    public void setDineInCount(int dineInCount) { this.dineInCount = dineInCount; }
    public double getSubscriptionFee() { return subscriptionFee; }
    public void setSubscriptionFee(double subscriptionFee) { this.subscriptionFee = subscriptionFee; }
    public boolean isSubscriptionActive() { return subscriptionActive && monthsRemaining > 0; }
    public void setSubscriptionActive(boolean subscriptionActive) { this.subscriptionActive = subscriptionActive; }
    public int getMonthsRemaining() { return monthsRemaining; }
    public void setMonthsRemaining(int monthsRemaining) { this.monthsRemaining = monthsRemaining; }

    

    
    
    
    
    // ===== Login =====
    @Override
    public boolean login(String inputUsername, String inputPassword) {
        if ((this.username.equals(inputUsername) || this.id.equals(inputUsername))
                && this.password.equals(inputPassword)) {
            System.out.println("Login successful! Welcome back, " + getName() + "!");
            System.out.println("Dine-in Count this month: " + dineInCount);
            System.out.println("Elite Status: " + (isEliteCustomer && isSubscriptionActive() ? "Active" : "Not Active"));
            return true;
        } else {
            System.out.println("Invalid username or password.");
            return false;
        }
    }

    // ===== Dine-in Tracking =====
    public void incrementDineInCount() {
        dineInCount++;
        if (dineInCount >= 5 && !isEliteCustomer) {
            System.out.println("Congratulations! Eligible for Elite membership.");
        }
    }

    public void resetMonthlyDineInCount() {
        dineInCount = 0;
    }

    // ===== Subscription =====
    public boolean subscribeToElite(boolean paid) {
        if (paid || dineInCount >= 5) {
            setEliteCustomer(true);
            setSubscriptionActive(true);
            setMonthsRemaining(1);
            System.out.println("Elite subscription activated! 10% discount applied.");
            return true;
        } else {
            System.out.println("Payment required or not enough dine-ins to subscribe.");
            return false;
        }
    }

    public void unsubscribeFromElite() {
        setEliteCustomer(false);
        setSubscriptionActive(false);
        setMonthsRemaining(0);
    }

    public void decreaseMonthsRemaining() {
        if (monthsRemaining > 0) {
            monthsRemaining--;
            if (monthsRemaining == 0) subscriptionActive = false;
        }
    }

    // ===== Order Methods =====
    public Order placeOnlineOrder(Map<MenuItem, Integer> items, Address deliveryAddress) {
        Order order = new Order(this, items, Systemmode.ONLINE_DELIVERY, null);
        order.setDeliveryAddress(deliveryAddress);
        order.calculateSubtotal();
        order.applyEliteDiscount();
        order.calculateTotal();
        return order;
    }

    public Order placeTakeawayOrder(Map<MenuItem, Integer> items) {
        Order order = new Order(this, items, Systemmode.TAKEAWAY, null);
        order.calculateSubtotal();
        order.applyEliteDiscount();
        order.calculateTotal();
        return order;
    }

    public Order placeDineInOrder(Map<MenuItem, Integer> items, Table table) {
        incrementDineInCount();
        Order order = new Order(this, items, Systemmode.DINE_IN, table);
        order.calculateSubtotal();
        order.applyEliteDiscount();
        order.calculateTotal();
        return order;
    }

    // ===== Profile =====
    public void updateProfile(String newEmail, String newPhone, Address newAddress) {
        setEmail(newEmail);
        setPhoneNumber(newPhone);
        setAddress(newAddress);
    }

    @Override
    public String getDetails() {
        return super.getDetails() +
               "\nCustomer ID: " + id +
               "\nUsername: " + username +
               "\nElite Member: " + (isEliteCustomer ? "Yes" : "No") +
               "\nSubscription Active: " + (isSubscriptionActive() ? "Yes" : "No") +
               "\nAddress: " + (address != null ? address.getFulladdress() : "N/A") +
               "\nDine-in Count: " + dineInCount;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurantsystem1;

import java.util.*;

public class Customer extends Person {

    private static int idCounter = 1;

    private String username;
    private boolean isEliteCustomer;
    private Address address;
    private int dineInCount;
    private double subscriptionFee = 100.0;
    private boolean subscriptionActive;
    private int monthsRemaining;

    // CHANGE: Added a single Scanner object for the entire class
    private static final Scanner scanner = new Scanner(System.in);

    // ===== Constructor =====
    public Customer(String username, String password, boolean isEliteCustomer,
                    Address address, String name, String email, String phoneNumber) {
        super(name, email, phoneNumber, password);
        this.id = "CUST" + String.format("%03d", idCounter++);
        setUsername(username);
        setPassword(password);
        setEliteCustomer(isEliteCustomer);
        setAddress(address);
        this.dineInCount = 0;
        this.subscriptionActive = false;
        this.monthsRemaining = 0;
    }

    // ===== Getters & Setters =====
    public String getCustomerId() { return id; }
    public void setCustomerId(String customerId) { this.id = customerId; }

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

    public boolean getSubscriptionActive() { return subscriptionActive; }
    public void setSubscriptionActive(boolean subscriptionActive) { this.subscriptionActive = subscriptionActive; }

    public int getMonthsRemaining() { return monthsRemaining; }
    public void setMonthsRemaining(int monthsRemaining) { this.monthsRemaining = monthsRemaining; }

    // ===== Customer Registration =====
    public static void registerCustomer(ArrayList<Customer> customers) {
        System.out.println("\n=== CUSTOMER REGISTRATION ===");

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter address: ");
        String addressStr = scanner.nextLine();
        Address address = new Address(1, addressStr, true);

        Customer customer = new Customer(username, password, false, address, name, email, phone);
        customers.add(customer);

        System.out.println("\nRegistration completed!");
        System.out.println("Your Customer ID: " + customer.getCustomerId());
        System.out.println("Please remember your credentials for login.");

        customer.customerMenu(); // CHANGE: now uses instance method instead of static
    }

    // ===== Customer Menu =====
    public void customerMenu() { // CHANGE: made non-static for OOP correctness
        while (true) {
            System.out.println("\n========== CUSTOMER MENU ==========");
            System.out.println("Hello, " + getName() + "!");
            System.out.println("1. Online Delivery");
            System.out.println("2. Takeaway");
            System.out.println("3. Dine-In");
            System.out.println("4. View Profile");
            System.out.println("5. Subscribe to Elite");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> placeOnlineOrder();
                case 2 -> placeTakeawayOrder();
                case 3 -> placeDineInOrder(tables,scanner);
                case 4 -> System.out.println("\n" + getDetails());
                case 5 -> subscribeElite();
                case 6 -> {
                    System.out.println("Logged out successfully!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ===== Order Methods =====
    private void placeOnlineOrder() {
        System.out.println("\n=== ONLINE DELIVERY ORDER ===");

        System.out.println("Delivery Address: " + address.getFulladdress());
        System.out.print("Use this address? (y/n): ");
        String confirm = scanner.nextLine();

        Address deliveryAddress = address;
        if (confirm.equalsIgnoreCase("n")) {
            System.out.print("Enter new delivery address: ");
            String newAddr = scanner.nextLine();
            deliveryAddress = new Address(2, newAddr, false);
        }

        Menu menu = new Menu(); // or get an existing menu instance if you already have one
    Map<MenuItem, Integer> items = menu.selectMenuItems( scanner);
        if (items.isEmpty()) {
            System.out.println("No items selected!");
            return;
        }

        Order order = new Order(this, items, Systemmode.online_delivery, null);
        order.setDeliveryAddress(deliveryAddress);
        order.calculateSubtotal();
        order.applyEliteDiscount();
        order.calculateTotal();

        System.out.println(order.getOrderSummary());
      Payment.processPayment(order);

        System.out.println("\nYour order will be delivered to: " + deliveryAddress.getFulladdress());
    }

    private void placeTakeawayOrder() {
        System.out.println("\n=== TAKEAWAY ORDER ===");

        Menu menu = new Menu(); // or get an existing menu instance if you already have one
    Map<MenuItem, Integer> items = menu.selectMenuItems( scanner);
        if (items.isEmpty()) {
            System.out.println("No items selected!");
            return;
        }

        Order order = new Order(this, items, Systemmode.takeAway, null);
        order.calculateSubtotal();
        order.applyEliteDiscount();
        order.calculateTotal();

        System.out.println(order.getOrderSummary());
       Payment.processPayment(order);

        System.out.println("\nYour order will be ready for pickup in 15-20 minutes!");
    }

    private void placeDineInOrder(ArrayList<Table> tables, Scanner scanner) {
        System.out.println("\n=== DINE-IN ORDER ===");

        Table selectedTable = Table.selectTable(tables, scanner);
        if (selectedTable == null) {
            System.out.println("No available tables!");
            return;
        }

        Menu menu = new Menu(); // or get an existing menu instance if you already have one
    Map<MenuItem, Integer> items = menu.selectMenuItems( scanner);
        if (items.isEmpty()) {
            System.out.println("No items selected!");
            selectedTable.releaseTable();
            return;
        }

        Order order = new Order(this, items, Systemmode.walk_in, selectedTable);
        order.calculateSubtotal();
        order.applyEliteDiscount();
        order.calculateTotal();

        System.out.println(order.getOrderSummary());
        Payment.processPayment(order);

        System.out.println("\nEnjoy your meal at Table " + selectedTable.getTableNumber() + "!");
    }

    // ===== Login Override =====
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

    // ===== Dine-In Tracking =====
    public void incrementDineInCount() {
        dineInCount++;
        System.out.println("Dine-in recorded! Total visits this month: " + dineInCount);

        if (dineInCount > 4 && !isEliteCustomer) {
            System.out.println("Congratulations! You've visited 5+ times. You're eligible for Elite membership!");
        }
    }

    public void resetMonthlyDineInCount() {
        dineInCount = 0;
        System.out.println("Monthly dine-in count reset for " + getName());
    }

    // ===== Profile Update =====
    public void updateProfile(String newEmail, String newPhone, Address newAddress) {
        setEmail(newEmail);
        setPhoneNumber(newPhone);
        setAddress(newAddress);
        System.out.println("Profile updated successfully!");
    }

    // ===== Subscription Handling =====
    public void subscribeToElite(boolean paid) {
        if (paid || dineInCount >= 5) {
            setEliteCustomer(true);
            setSubscriptionActive(true);
            setMonthsRemaining(1);
            System.out.println("Elite subscription activated! Valid for " + monthsRemaining + " month(s).");
            System.out.println("You now get 10% discount on all orders!");
        } else if (!paid) {
            System.out.println("Payment of EGP " + subscriptionFee + " required to activate Elite membership.");
        } else {
            System.out.println("You need at least 5 dine-ins this month to qualify for Elite membership.");
            System.out.println("Current dine-ins: " + dineInCount);
        }
    }

    public void subscribeElite() {
        System.out.println("\n=== ELITE MEMBERSHIP ===");
        System.out.println("Fee: EGP " + subscriptionFee);
        System.out.println("Benefits: 10% discount on all orders");
        System.out.print("Subscribe? (y/n): ");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("y")) {
            System.out.print("Process payment of EGP " + subscriptionFee + "? (y/n): ");
            String confirm = scanner.nextLine();
            subscribeToElite(confirm.equalsIgnoreCase("y"));
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

    // ===== Helper Method: Find Customer =====
    public static Customer findCustomer(String id, ArrayList<Customer> customers) {
        for (Customer c : customers) {
            if (c.getCustomerId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    // ===== toString Override =====
    @Override
    public String getDetails() {
        return super.getDetails()
                + "\nCustomer ID: " + id
                + "\nUsername: " + username
                + "\nElite Member: " + (isEliteCustomer ? "Yes" : "No")
                + "\nSubscription Active: " + (isSubscriptionActive() ? "Yes" : "No")
                + "\nAddress: " + address;
    }
}

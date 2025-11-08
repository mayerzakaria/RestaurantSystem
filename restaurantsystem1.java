package restaurantsystem1;

import java.util.*;


public class restaurantsystem1 
{
    private static RestaurantController controller;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        controller = new RestaurantController();
        controller.initializeSystem();
        displayWelcome();
        mainMenu();
    }

    private static void displayWelcome() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("          ️  WELCOME TO RESTAURANT SYSTEM  ️");
        System.out.println("=".repeat(60));
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("                      MAIN MENU");
            System.out.println("=".repeat(60));
            System.out.println("1.  Register (New Customer)");
            System.out.println("2.  Login");
            System.out.println("3.  View Menu");
            System.out.println("4.  Exit");
            System.out.println("=".repeat(60));
            System.out.print("Choose an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    registerCustomer();
                    break;
                case 2:
                    loginMenu();
                    break;
                case 3:
                    controller.getMenu().displayMenu();
                    break;
                case 4:
                    exitSystem();
                    return;
                default:
                    System.out.println(" Invalid choice!");
            }
        }
    }

    private static void registerCustomer() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("               CUSTOMER REGISTRATION");
        System.out.println("=".repeat(60));

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
        String address = scanner.nextLine();

        Customer customer = controller.registerCustomer(name, email, phone, 
                                                       username, password, address);
        
        if (customer != null) {
            System.out.println("\n Please remember your credentials for login.");

            System.out.print("\nWould you like to login now? (y/n): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("y")) {
                customerMenu(customer);
            }
        }
    }

    private static void loginMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                      LOGIN");
        System.out.println("=".repeat(60));
        System.out.print("Enter your ID (CUST### or CH###): ");
        String id = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (id.toUpperCase().startsWith("CUST")) {
            Customer customer = controller.customerLogin(id, password);
            if (customer != null) {
                customerMenu(customer);
            } else {
                System.out.println(" Login failed! Invalid credentials.");
            }
        } else if (id.toUpperCase().startsWith("CH")) {
            Cashier cashier = controller.cashierLogin(id, password);
            if (cashier != null) {
                cashierMenu(cashier);
            } else {
                System.out.println(" Login failed! Invalid credentials.");
            }
        } else {
            System.out.println(" Invalid ID format!");
        }
    }

    private static void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("                    CUSTOMER MENU");
            System.out.println("=".repeat(60));
            System.out.println("Hello, " + customer.getName() + "!");
            System.out.println("=".repeat(60));
            System.out.println("1.  Online Delivery");
            System.out.println("2.  View Menu");
            System.out.println("3.  View Profile");
            System.out.println("4.  Subscribe to Elite");
            System.out.println("5.  View My Orders");
            System.out.println("6.  Logout");
            System.out.println("=".repeat(60));
            System.out.print("Choose an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    placeOnlineOrder(customer);
                    break;
                case 2:
                    controller.getMenu().displayMenu();
                    break;
                case 3:
                    System.out.println("\n" + customer.getDetails());
                    break;
                case 4:
                    subscribeElite(customer);
                    break;
                case 5:
                    viewCustomerOrders(customer);
                    break;
                case 6:
                    System.out.println(" Logged out successfully!");
                    return;
                default:
                    System.out.println(" Invalid choice!");
            }
        }
    }

    private static void placeOnlineOrder(Customer customer) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("               ONLINE DELIVERY ORDER");
        System.out.println("=".repeat(60));
        System.out.println("Delivery Address: " + customer.getAddress().getFulladdress());
        System.out.print("Use this address? (y/n): ");
        String confirm = scanner.nextLine();

        Address deliveryAddress = customer.getAddress();
        if (confirm.equalsIgnoreCase("n")) {
            System.out.print("Enter new delivery address: ");
            String newAddr = scanner.nextLine();
            deliveryAddress = new Address(2, newAddr, false);
            if (!deliveryAddress.validateAddress()) {
                return;
            }
        }

        Map<MenuItem, Integer> items = selectMenuItems();
        if (items.isEmpty()) {
            System.out.println(" No items selected!");
            return;
        }

        Order order = controller.createOnlineOrder(customer, items, deliveryAddress);
        System.out.println(order.getOrderSummary());

        processPaymentUI(order, null);

        if (order.getStatus() == Status.COMPLETE) {
            System.out.println("\n Your order will be delivered to: " + 
                             deliveryAddress.getFulladdress());
            System.out.println("️ Estimated delivery time: 30-45 minutes");
        }
    }

    private static void cashierMenu(Cashier cashier) {
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("                    CASHIER MENU");
            System.out.println("=".repeat(60));
            System.out.println("Hello, " + cashier.getName() + "!");
            System.out.println("=".repeat(60));
            System.out.println("1. ️ Process Takeaway Order");
            System.out.println("2. ️ Process Dine-In Order");
            System.out.println("3.  View All Tables");
            System.out.println("4.  Release Table (Free Table)");
            System.out.println("5.  View Menu");
            System.out.println("6.  Logout");
            System.out.println("=".repeat(60));
            System.out.print("Choose an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    cashierProcessTakeaway(cashier);
                    break;
                case 2:
                    cashierProcessDineIn(cashier);
                    break;
                case 3:
                    viewTables();
                    break;
                case 4:
                    releaseTableMenu();
                    break;
                case 5:
                    controller.getMenu().displayMenu();
                    break;
                case 6:
                    System.out.println(" Logged out successfully!");
                    return;
                default:
                    System.out.println(" Invalid choice!");
            }
        }
    }

    private static void cashierProcessTakeaway(Cashier cashier) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           CASHIER: PROCESS TAKEAWAY ORDER");
        System.out.println("=".repeat(60));
        System.out.print("Enter customer ID (or 0 for walk-in): ");
        String custId = scanner.nextLine();

        Customer customer = getOrCreateCustomer(custId);
        if (customer == null) return;

        Map<MenuItem, Integer> items = selectMenuItems();
        if (items.isEmpty()) {
            System.out.println(" No items selected!");
            return;
        }

        Order order = controller.createTakeawayOrder(cashier, customer, items);
        cashier.printReceipt(order);
        processPaymentUI(order, cashier);

        if (order.getStatus() == Status.COMPLETE) {
            System.out.println("\n Order will be ready for pickup in 15-20 minutes!");
        }
    }

    private static void cashierProcessDineIn(Cashier cashier) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("            CASHIER: PROCESS DINE-IN ORDER");
        System.out.println("=".repeat(60));

        System.out.print("Enter customer ID (or 0 for walk-in): ");
        String custId = scanner.nextLine();

        Customer customer = getOrCreateCustomer(custId);
        if (customer == null) return;
        Table table = selectTableUI();
        if (table == null) {
            System.out.println(" No available tables!");
            return;
        }

        Map<MenuItem, Integer> items = selectMenuItems();
        if (items.isEmpty()) {
            System.out.println(" No items selected!");
            controller.releaseTable(table);
            return;
        }

        Order order = controller.createDineInOrder(cashier, customer, items, table);
        cashier.printReceipt(order);

        processPaymentUI(order, cashier);

        if (order.getStatus() == Status.COMPLETE) {
            System.out.println("\n Enjoy your meal at Table #" + table.getTableNumber() + "!");
            System.out.print("\n Has the customer finished eating? Release table now? (y/n): ");
            String releaseChoice = scanner.nextLine();
            
            if (releaseChoice.equalsIgnoreCase("y")) {
                controller.releaseTable(table);
                System.out.println(" Table #" + table.getTableNumber() + 
                                 " released and available for new customers!");
            } else {
                System.out.println("️ Table #" + table.getTableNumber() + 
                                 " still occupied. You can release it later from menu option 4.");
            }
        }
    }

    private static Customer getOrCreateCustomer(String custId) {
        if (custId.equals("0")) {
            System.out.print("Customer name: ");
            String name = scanner.nextLine();
            Address tempAddr = new Address(0, "Walk-in", true);
            return new Customer("guest", "guest", false, tempAddr, name, "n/a", "n/a");
        } else {
            Customer customer = controller.findCustomer(custId);
            if (customer == null) {
                System.out.println(" Customer not found!");
            }
            return customer;
        }
    }

    private static Map<MenuItem, Integer> selectMenuItems() {
        Map<MenuItem, Integer> selectedItems = new HashMap<>();

        System.out.println("\n" + "=".repeat(90));
        System.out.println("                              SELECT ITEMS FROM MENU");
        System.out.println("=".repeat(90));

        ArrayList<MenuItem> availableItems = controller.getAvailableMenuItems();

        if (availableItems.isEmpty()) {
            System.out.println(" No items available at the moment!");
            return selectedItems;
        }

        for (int i = 0; i < availableItems.size(); i++) {
            MenuItem item = availableItems.get(i);
            System.out.println((i + 1) + ". " + item.getInfo());
        }

        System.out.println("\n" + "=".repeat(90));
        System.out.println("Select items (enter 0 to finish):");

        while (true) {
            System.out.print("\n Item number: ");
            int itemNum = getIntInput();

            if (itemNum == 0) break;

            if (itemNum < 1 || itemNum > availableItems.size()) {
                System.out.println(" Invalid item number!");
                continue;
            }

            System.out.print("Quantity: ");
            int qty = getIntInput();

            if (qty <= 0) {
                System.out.println(" Quantity must be greater than 0!");
                continue;
            }

            MenuItem selectedItem = availableItems.get(itemNum - 1);
            selectedItems.put(selectedItem, selectedItems.getOrDefault(selectedItem, 0) + qty);

            System.out.println(" Added: " + selectedItem.getName() + " x" + qty);
        }

        return selectedItems;
    }

    private static Table selectTableUI() {
        ArrayList<Table> availableTables = controller.getAvailableTables();

        if (availableTables.isEmpty()) {
            return null;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("                  AVAILABLE TABLES");
        System.out.println("=".repeat(60));

        for (int i = 0; i < availableTables.size(); i++) {
            System.out.println((i + 1) + ". " + availableTables.get(i));
        }

        System.out.print("\nSelect table number: ");
        int choice = getIntInput();

        if (choice < 1 || choice > availableTables.size()) {
            System.out.println(" Invalid choice!");
            return null;
        }

        Table selected = availableTables.get(choice - 1);
        return controller.selectTable(selected.getTableNumber());
    }

    private static void viewTables() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                     ALL TABLES");
        System.out.println("=".repeat(60));
        
        for (Table table : controller.getTables()) {
            System.out.println(table);
        }
    }

    private static void releaseTableMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("               RELEASE TABLE (FREE TABLE)");
        System.out.println("=".repeat(60));
        
        ArrayList<Table> occupiedTables = new ArrayList<>();
        for (Table table : controller.getTables()) {
            if (!table.isAvailable()) {
                occupiedTables.add(table);
            }
        }
        
        if (occupiedTables.isEmpty()) {
            System.out.println(" All tables are already available!");
            return;
        }
        
        System.out.println("Occupied Tables:");
        for (int i = 0; i < occupiedTables.size(); i++) {
            Table table = occupiedTables.get(i);
            System.out.println((i + 1) + ". " + table);
        }
        
        System.out.print("\nEnter table number to release (0 to cancel): ");
        int choice = getIntInput();
        
        if (choice == 0) {
            System.out.println(" Cancelled!");
            return;
        }
        
        if (choice < 1 || choice > occupiedTables.size()) {
            System.out.println(" Invalid choice!");
            return;
        }
        
        Table selectedTable = occupiedTables.get(choice - 1);
        controller.releaseTable(selectedTable);
        
        System.out.println(" Table #" + selectedTable.getTableNumber() + 
                         " is now available for new customers!");
    }

   
    private static void processPaymentUI(Order order, Cashier cashier) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                     PAYMENT");
        System.out.println("=".repeat(60));
        System.out.println("Total Amount: EGP " + String.format("%.2f", order.getTotal()));
        System.out.println("\nSelect payment method:");
        System.out.println("1.  Cash");
        System.out.println("2.  Credit Card");
        System.out.println("3.  Debit Card");
        System.out.println("4.  Mobile Wallet");
        System.out.print("Choice: ");

        int choice = getIntInput();

        Payment.PaymentMethod method;
        switch (choice) {
            case 1:
                method = Payment.PaymentMethod.CASH;
                break;
            case 2:
                method = Payment.PaymentMethod.CREDIT_CARD;
                break;
            case 3:
                method = Payment.PaymentMethod.DEBIT_CARD;
                break;
            case 4:
                method = Payment.PaymentMethod.MOBILE_WALLET;
                break;
            default:
                method = Payment.PaymentMethod.CASH;
        }

        System.out.print("\nEnter payment amount: EGP ");
        double amount = getDoubleInput();

        controller.processPayment(order, amount, method, cashier);
    }


    private static void subscribeElite(Customer customer) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                 ELITE MEMBERSHIP");
        System.out.println("=".repeat(60));
        System.out.println(" Fee: EGP " + customer.getSubscriptionFee());
        System.out.println(" Benefits: 10% discount on all orders");
        System.out.println(" Current dine-ins: " + customer.getDineInCount() + "/5");
        System.out.println("\n Tip: Visit 5 times to get Elite membership for FREE!");
        System.out.println("=".repeat(60));
        System.out.print("\nSubscribe? (y/n): ");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("y")) {
            System.out.print("Process payment of EGP " + customer.getSubscriptionFee() + "? (y/n): ");
            String confirm = scanner.nextLine();
            controller.subscribeToElite(customer, confirm.equalsIgnoreCase("y"));
        }
    }

    private static void viewCustomerOrders(Customer customer) {
        ArrayList<Order> customerOrders = controller.getCustomerOrders(customer.getCustomerId());
        
        if (customerOrders.isEmpty()) {
            System.out.println("\n No orders found.");
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    YOUR ORDERS");
        System.out.println("=".repeat(60));
        
        for (Order order : customerOrders) {
            System.out.println(order);
        }
        
        System.out.println("=".repeat(60));
    }

    private static int getIntInput() {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); 
                return value;
            } catch (InputMismatchException e) {
                System.out.print(" Invalid input! Please enter a number: ");
                scanner.nextLine(); 
            }
        }
    }

    private static double getDoubleInput() {
        while (true) {
            try {
                double value = scanner.nextDouble();
                scanner.nextLine(); 
                return value;
            } catch (InputMismatchException e) {
                System.out.print(" Invalid input! Please enter a number: ");
                scanner.nextLine(); 
            }
        }
    }

    private static void exitSystem() {
        System.out.println("\n" + "=".repeat(60));
        controller.shutdown();
        System.out.println("=".repeat(60));
        System.exit(0);
    }
}

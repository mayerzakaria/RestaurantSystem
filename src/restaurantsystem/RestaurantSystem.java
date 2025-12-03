package restaurantsystem;

import java.util.ArrayList;
import java.util.Scanner;
import restaurantsystemdao.*;
import java.sql.SQLException;

public class RestaurantSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("================================");
        System.out.println("   WELCOME TO RESTAURANT SYSTEM");
        System.out.println("================================\n");

        // Load data from database
        ArrayList<Customer> customers = new ArrayList<>();
        ArrayList<Cashier> cashiers = new ArrayList<>();
        ArrayList<Delivery> deliveryPersons = new ArrayList<>();
        ArrayList<Table> tables = new ArrayList<>();

        try {
            customers = CustomerDAO.getAllCustomers();
            cashiers = CashierDAO.getAllCashiers();
            deliveryPersons = DeliveryDAO.getAllDeliveryPersons();
            tables = TableDAO.listAllTables();
            System.out.println("Data loaded from database successfully!");
        } catch (SQLException e) {
            System.err.println("Error loading data from database: " + e.getMessage());
            e.printStackTrace();
        }

        while (true) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Register (New Customer)");
            System.out.println("2. Login");
            System.out.println("3. View Menu");
            System.out.println("4. View All Tables");
            System.out.println("5. Exit");
            System.out.println("================================");
            System.out.print("Choose an option: ");

            int mainChoice = -1;
            try {
                mainChoice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input!");
                scanner.nextLine();
                continue;
            }

            if (mainChoice == 1) {
                // Register new customer using the existing method in Customer class
                Customer.registerCustomer(customers, scanner);

            } else if (mainChoice == 2) {
                // Login
                System.out.println("\n=== LOGIN ===");
                System.out.print("Enter your ID (CUST### / CH### / DP###): ");
                String id = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                if (id.toUpperCase().startsWith("CUST")) {
                    // Customer login
                    try {
                        CustomerDAO dao = new CustomerDAO();
                        Customer customer = dao.findById(id);
                        if (customer != null && customer.getPassword().equals(password)) {
                            System.out.println("Login successful! Welcome, " + customer.getName());
                            handleCustomerMenu(customer, scanner);
                        } else {
                            System.out.println("Login failed! Invalid credentials.");
                        }
                    } catch (SQLException e) {
                        System.err.println("Error during login: " + e.getMessage());
                    }

                } else if (id.toUpperCase().startsWith("CH")) {
                    // Cashier login
                    try {
                        Cashier cashier = CashierDAO.findById(id);
                        if (cashier != null && cashier.getPassword().equals(password)) {
                            System.out.println("Login successful! Welcome, " + cashier.getName());
                            handleCashierMenu(cashier, scanner, tables);
                        } else {
                            System.out.println("Login failed! Invalid credentials.");
                        }
                    } catch (SQLException e) {
                        System.err.println("Error during login: " + e.getMessage());
                    }

                } else if (id.toUpperCase().startsWith("DP")) {
                    // Delivery person login
                    try {
                        Delivery delivery = DeliveryDAO.findById(id);
                        if (delivery != null && delivery.getPassword().equals(password)) {
                            System.out.println("Login successful! Welcome, " + delivery.getName());
                            handleDeliveryMenu(delivery, scanner);
                        } else {
                            System.out.println("Login failed! Invalid credentials.");
                        }
                    } catch (SQLException e) {
                        System.err.println("Error during login: " + e.getMessage());
                    }

                } else {
                    System.out.println("Invalid ID format!");
                }

            } else if (mainChoice == 3) {
                // View menu
                System.out.println("\n========== MENU ==========");
                ArrayList<MenuItem> menuItems = MenuitemDAO.listAllMenuItems();
                if (menuItems.isEmpty()) {
                    System.out.println("No menu items available.");
                } else {
                    for (MenuItem item : menuItems) {
                        System.out.println(item);
                    }
                }

            } else if (mainChoice == 4) {
                // View all tables
                System.out.println("\n========== ALL TABLES ==========");
                tables = TableDAO.listAllTables();
                if (tables.isEmpty()) {
                    System.out.println("No tables found.");
                } else {
                    for (Table table : tables) {
                        System.out.println(table);
                    }
                }

            } else if (mainChoice == 5) {
                System.out.println("Thank you for using our system!");
                scanner.close();
                System.exit(0);

            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    private static void handleCustomerMenu(Customer customer, Scanner scanner) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n========== CUSTOMER MENU ==========");
            System.out.println("Hello, " + customer.getName() + "!");
            System.out.println("====================================");
            System.out.println("1. View Profile");
            System.out.println("2. Subscribe to Elite");
            System.out.println("3. Logout");
            System.out.println("====================================");
            System.out.print("Choose an option: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input!");
                scanner.nextLine();
                continue;
            }

            if (choice == 1) {
                System.out.println(customer.getDetails());
            } else if (choice == 2) {
                customer.subscribeElite(scanner);
                try {
                    CustomerDAO dao = new CustomerDAO();
                    dao.update(customer);
                } catch (SQLException e) {
                    System.err.println("Error updating customer: " + e.getMessage());
                }
            } else if (choice == 3) {
                System.out.println("Logged out!");
                loggedIn = false;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    private static void handleCashierMenu(Cashier cashier, Scanner scanner, ArrayList<Table> tables) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n========== CASHIER MENU ==========");
            System.out.println("Hello, " + cashier.getName() + "!");
            System.out.println("===================================");
            System.out.println("1. View All Tables");
            System.out.println("2. View Profile");
            System.out.println("3. View Menu");
            System.out.println("4. Logout");
            System.out.println("===================================");
            System.out.print("Choose an option: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input!");
                scanner.nextLine();
                continue;
            }

            if (choice == 1) {
                System.out.println("\n========== ALL TABLES ==========");
                tables = TableDAO.listAllTables();
                for (Table table : tables) {
                    System.out.println(table);
                }
            } else if (choice == 2) {
                System.out.println(cashier.getDetails());
            } else if (choice == 3) {
                ArrayList<MenuItem> menuItems = MenuitemDAO.listAllMenuItems();
                for (MenuItem item : menuItems) {
                    System.out.println(item);
                }
            } else if (choice == 4) {
                System.out.println("Logged out successfully!");
                loggedIn = false;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    private static void handleDeliveryMenu(Delivery delivery, Scanner scanner) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n========== DELIVERY MENU ==========");
            System.out.println("Hello, " + delivery.getName() + "!");
            System.out.println("====================================");
            System.out.println("1. View Profile");
            System.out.println("2. View Statistics");
            System.out.println("3. Logout");
            System.out.println("====================================");
            System.out.print("Choose an option: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input!");
                scanner.nextLine();
                continue;
            }

            if (choice == 1) {
                System.out.println(delivery.getDetails());
            } else if (choice == 2) {
                System.out.println(delivery.getStatistics());
            } else if (choice == 3) {
                System.out.println("Logged out!");
                loggedIn = false;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }
}
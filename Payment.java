/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurantsystem1;

import java.util.Scanner; // ✅ Added for user input

enum PaymentMethod {
    CASH,
    DEBIT_CARD,
    CREDIT_CARD,
    MOBILE_WALLET;
}

public class Payment {
    private double amount;
    private PaymentMethod paymentMethod;
    private Status status;
    private Order order;

    // ✅ Scanner declared once for user input
    private static final Scanner scanner = new Scanner(System.in);

    // Constructor
    public Payment(double amount, PaymentMethod paymentMethod, Order order) {
        setAmount(amount);
        setPaymentMethod(paymentMethod);
        this.status = Status.PENDING;
        setOrder(order);
    }

    // Getters and Setters
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }

    // ✅ Payment validation
    public boolean validatePayment() {
        if (order == null) {
            System.out.println("No order linked to this payment!");
            return false;
        }
        if (this.amount < order.getTotal()) {
            System.out.println("Insufficient payment!");
            return false;
        }
        System.out.println("Payment validated successfully.");
        return true;
    }

    // ✅ Actual payment processing
    public void processPayment() {
        if (validatePayment()) {
            this.status = Status.COMPLETE;
            System.out.println("Payment of EGP " + amount + " via " + paymentMethod + " completed successfully!");
            if (amount > order.getTotal()) {
                double change = amount - order.getTotal();
                System.out.println("Change: EGP " + String.format("%.2f", change));
            }
        } else {
            this.status = Status.FAILED;
            System.out.println("✗ Payment processing failed!");
        }
    }

    // ✅ New static method moved from Main to here
    public static void processPayment(Order order) {
        System.out.println("\n--- PAYMENT ---");
        System.out.println("Total: EGP " + order.getTotal());
        System.out.println("Select payment method:");
        System.out.println("1. Cash");
        System.out.println("2. Credit Card");
        System.out.println("3. Debit Card");
        System.out.println("4. Mobile Wallet");
        System.out.print("Choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        PaymentMethod method;
        switch (choice) {
            case 1: method = PaymentMethod.CASH; break;
            case 2: method = PaymentMethod.CREDIT_CARD; break;
            case 3: method = PaymentMethod.DEBIT_CARD; break;
            case 4: method = PaymentMethod.MOBILE_WALLET; break;
            default: method = PaymentMethod.CASH;
        }

        System.out.print("Enter payment amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        Payment payment = new Payment(amount, method, order);
        payment.processPayment();
    }

    public String getReceipt() {
        return "----- Payment Receipt -----" +
               "\nOrder ID: " + (order != null ? order.getOrderId() : "N/A") +
               "\nAmount Paid: " + amount + " EGP" +
               "\nPayment Method: " + paymentMethod +
               "\nStatus: " + status +
               "\n---------------------------";
    }
}

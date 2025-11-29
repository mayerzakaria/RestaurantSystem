package restaurantsystem;

import java.sql.*;
import util.DB;

public class RestaurantDBHandler 
{

    private static Connection conn = DB.getConnection();

    // ======================================
    // INSERT PERSON
    // ======================================
    public static void insertPerson(Person p) {
        String sql = "INSERT INTO Person(id, name, email, phonenumber, password) "
                   + "VALUES (?, ?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE "
                   + "name=VALUES(name), email=VALUES(email), phonenumber=VALUES(phonenumber), "
                   + "password=VALUES(password)";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getPhoneNumber());
            ps.setString(5, p.getPassword());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Person added/updated: " + p.getName());
            }

        } catch (SQLException e) {
            System.err.println("Error inserting/updating person: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ======================================
    // INSERT RESTAURANT TABLE
    // ======================================
     public static void insertRestaurantTable(Table t) {
        String sql = "INSERT INTO `Table`(tablenumber, capacity, Tablestatus, Cashierid) "
                   + "VALUES (?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE "
                   + "capacity=VALUES(capacity), Tablestatus=VALUES(Tablestatus), Cashierid=VALUES(Cashierid)";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getTableNumber());
            ps.setInt(2, t.getCapacity());
            ps.setString(3, t.getStatus().name());
            ps.setNull(4, java.sql.Types.VARCHAR); // or set actual cashier ID if available

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Table added/updated: " + t.getTableNumber());
            }

        } catch (SQLException e) {
            System.err.println("Error inserting/updating table: " + e.getMessage());
            e.printStackTrace();
        }
     }

    // ======================================
    // INSERT ORDER
    // ======================================
   public static void insertOrder(Order o) {
        String sql = "INSERT INTO `Order` (idOrder, Customerrid, tablenumber, total, status, ordertype, Payment_id, DeliveryId, Cashierid) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE "
                   + "Customerrid=VALUES(Customerrid), tablenumber=VALUES(tablenumber), total=VALUES(total), "
                   + "status=VALUES(status), ordertype=VALUES(ordertype), Payment_id=VALUES(Payment_id), "
                   + "DeliveryId=VALUES(DeliveryId), Cashierid=VALUES(Cashierid)";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, o.getOrderId());
            ps.setString(2, o.getCustomerId());
            ps.setObject(3, o.getTable() != null ? o.getTable().getTableNumber() : null);
            ps.setDouble(4, o.getTotal());
            ps.setString(5, o.getStatus().name());
            ps.setString(6, o.getOrderType().name());
            ps.setObject(7, o.getPayment() != null ? o.getPayment().getId() : null);
            ps.setObject(8, o.getAssignedDelivery() != null ? o.getAssignedDelivery().getDeliveryPersonId(): null);
            ps.setObject(9, o.getCashierId() != null ? o.getCashierId() : null);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Order added/updated: " + o.getOrderId());
            }

        } catch (SQLException e) {
            System.err.println("Error inserting/updating order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ======================================
    // INSERT PAYMENT
    // ======================================
    public static void insertPayment(Payment pay) {
        String sql = "INSERT INTO Payment(paymentId, orderId, amount, paymentMethod, status) "
                   + "VALUES (?, ?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE "
                   + "orderId=VALUES(orderId), amount=VALUES(amount), "
                   + "paymentMethod=VALUES(paymentMethod), status=VALUES(status)";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, pay.getId() != 0 ? pay.getId() : null); // Use null if ID is auto-generated
            ps.setInt(2, pay.getOrderId());
            ps.setDouble(3, pay.getAmount());
            ps.setString(4, pay.getPaymentMethod().name());
            ps.setString(5, pay.getStatus().name());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Payment added/updated: " + pay.getId());
            }

        } catch (SQLException e) {
            System.err.println("Error inserting/updating payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ======================================
    // INSERT MENU
    // ======================================
    public static void insertMenu(Menu m) {
        String sql = "INSERT INTO Menu(menuId, name, description) "
                   + "VALUES (?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE "
                   + "name=VALUES(name), description=VALUES(description)";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, m.getMenuId() != 0 ? m.getMenuId() : null); // null if auto-generated
            ps.setString(2, m.getName());
            ps.setString(3, m.getDescription());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Menu added/updated: " + m.getName());
            }

        } catch (SQLException e) {
            System.err.println("Error inserting/updating menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ======================================
    // INSERT MENU ITEM
    // ======================================
    public static void insertMenuItem(MenuItem mi) {
        String sql = "INSERT INTO MenuItem(itemId, menuId, name, description, price) "
                + "VALUES (?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "menuId=VALUES(menuId), name=VALUES(name), description=VALUES(description), price=VALUES(price)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mi.getItemId());
            ps.setInt(2, mi.getMenuId());
            ps.setString(3, mi.getName());
            ps.setString(4, mi.getDescription());
            ps.setDouble(5, mi.getPrice());

            ps.executeUpdate();
            System.out.println("MenuItem added/updated: " + mi.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ======================================
    // INSERT ONLINE DELIVERY
    // ======================================
  public static void insertDelivery(Delivery d) {
    String sql = "INSERT INTO Delivery(deliveryId, orderId, deliveryAddress, deliveryStatus) "
               + "VALUES (?, ?, ?, ?) "
               + "ON DUPLICATE KEY UPDATE "
               + "orderId=VALUES(orderId), deliveryAddress=VALUES(deliveryAddress), deliveryStatus=VALUES(deliveryStatus)";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, d.getDeliveryPersonId()); 
        ps.setInt(2, d.getCurrentOrder() != null ? d.getCurrentOrder().getOrderId() : 0);
        ps.setString(3, d.getCurrentOrder() != null && d.getCurrentOrder().getDeliveryAddress() != null
                            ? d.getCurrentOrder().getDeliveryAddress().getFullAddress()
                            : null);
        ps.setString(4, d.getStatus().name());

        ps.executeUpdate();
        System.out.println("Delivery added/updated: " + d.getDeliveryPersonId());
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    // ======================================
    // INSERT CUSTOMER (Separate Table)
    // ======================================
    public static void insertCustomer(Customer c) {
    String sql = "INSERT INTO Customer(customerId, name, email, phoneNumber, password, address) "
               + "VALUES (?, ?, ?, ?, ?, ?) "
               + "ON DUPLICATE KEY UPDATE "
               + "name=VALUES(name), email=VALUES(email), phoneNumber=VALUES(phoneNumber), "
               + "password=VALUES(password), address=VALUES(address)";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, c.getCustomerId()); // customerId is String
        ps.setString(2, c.getName());
        ps.setString(3, c.getEmail());
        ps.setString(4, c.getPhoneNumber());
        ps.setString(5, c.getPassword());
        ps.setString(6, c.getAddress() != null ? c.getAddress().getFullAddress() : null);

        ps.executeUpdate();
        System.out.println("Customer added/updated: " + c.getName());
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    // ======================================
    // INSERT CASHIER
    // ======================================
   public static void insertCashier(Cashier cs) {
    String sql = "INSERT INTO Cashier(cashierId, name, email, phoneNumber, password) "
               + "VALUES (?, ?, ?, ?, ?) "
               + "ON DUPLICATE KEY UPDATE "
               + "name=VALUES(name), email=VALUES(email), phoneNumber=VALUES(phoneNumber), password=VALUES(password)";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, cs.getEmployeeId()); 
        ps.setString(2, cs.getName());
        ps.setString(3, cs.getEmail());
        ps.setString(4, cs.getPhoneNumber());
        ps.setString(5, cs.getPassword());

        ps.executeUpdate();
        System.out.println("Cashier added/updated: " + cs.getName());
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}

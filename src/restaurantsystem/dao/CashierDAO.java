package restaurantsystem.dao;

import restaurantsystem.Cashier;
import java.sql.*;

public class CashierDAO {
    private Connection conn;

    public CashierDAO(Connection conn) {
        this.conn = conn;
    }

    public Cashier getCashierById(String id) throws SQLException {
        String sql = """
            SELECT p.name, p.email, p.phoneNumber, p.password,
                   c.Salary, c.shift
            FROM Cashier c
            JOIN Person p ON c.Cashierid = p.id
            WHERE c.Cashierid = ?
        """;

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Cashier cashier = new Cashier(
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phoneNumber"),
                rs.getString("password"),
                rs.getDouble("Salary"),
                rs.getString("shift")
            );
            cashier.setId(id);
            return cashier;
        }
        return null;
    }

    public Cashier login(String id, String password) throws SQLException {
        Cashier cashier = getCashierById(id);
        if (cashier != null && cashier.login(id, password)) {
            return cashier;
        }
        return null;
    }
}
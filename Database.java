package banking;

import java.sql.*;

public class Database {

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(Main.url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createDatabaseWithTable() {
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS card (\n" +
                    "    id INTEGER , \n" +
                    "    number TEXT PRIMARY KEY, \n" +
                    "    pin TEXT, \n" +
                    "    balance INTEGER DEFAULT 0\n" +
                    ");");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void insert(String number, String pin) {
        String sql = "INSERT INTO card(number, pin) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            pstmt.setString(2, pin);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean checkIfExists(String number) {
        String sql = "SELECT id " + "FROM card WHERE number == ?";
        try (Connection conn = this.connect();
        PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setString(1, number);
            ResultSet rs = psmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean checkIfExistsWithPin(String number, String pin) {
       String sql = "SELECT id " + "FROM card WHERE number == ? AND pin == ?";

       try (Connection conn = this.connect();
            PreparedStatement psmt = conn.prepareStatement(sql)){
           psmt.setString(1, number);
           psmt.setString(2, pin);

           ResultSet rs = psmt.executeQuery();
           if (rs.next()) {
               return true;
           } else {
               return false;
           }
       } catch (SQLException e) {
           System.out.println(e.getMessage());
           return false;
       }
    }

    public int getBalance(String number) {
        String sql = "SELECT balance FROM card WHERE number = ?";

        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("balance");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public void showAllData() {
        String sql = "SELECT * FROM card";

        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                                    rs.getString("number") + "\t" +
                                    rs.getString("pin") + "\t" +
                                    rs.getInt("balance"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addIncome(int income, String number) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, income);
            pstmt.setString(2, number);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void doTransfer(String number, int amount, String receiver) {
        String sql = "UPDATE card SET balance = balance - ? WHERE number = ?";
        String sql2 = "UPDATE card SET balance = balance + ? WHERE number = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt.setInt(1, amount);
                pstmt.setString(2, number);
                pstmt2.setInt(1, amount);
                pstmt2.setString(2, receiver);
                pstmt.executeUpdate();
                pstmt2.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeAccount(String number) {
        String sql = "DELETE FROM card WHERE number = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, number);
                pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

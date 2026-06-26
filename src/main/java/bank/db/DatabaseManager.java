package bank.db;

import bank.model.Account;
import bank.model.Branch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;

// Saves and reads accounts from the firstbankdb MySQL database (run via XAMPP).
public class DatabaseManager {

    private static final String JDBC_URL =
            "jdbc:mysql://localhost:3306/firstbankdb?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public DatabaseManager() {
        initialize();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    /** Creates the bank_accounts table if it does not already exist. */
    private void initialize() {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute(
                "CREATE TABLE IF NOT EXISTS bank_accounts (" +
                "account_number VARCHAR(20) NOT NULL PRIMARY KEY, " +
                "first_name VARCHAR(30) NOT NULL, " +
                "last_name VARCHAR(30) NOT NULL, " +
                "nin VARCHAR(14) NOT NULL, " +
                "email VARCHAR(60) NOT NULL, " +
                "phone_number VARCHAR(15) NOT NULL, " +
                "pin VARCHAR(6) NOT NULL, " +
                "date_of_birth VARCHAR(10) NOT NULL, " +
                "account_type VARCHAR(20) NOT NULL, " +
                "branch VARCHAR(20) NOT NULL, " +
                "opening_deposit DOUBLE NOT NULL, " +
                "second_nin VARCHAR(14) NULL, " +
                "created_at VARCHAR(40) NOT NULL" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
            );
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize MySQL database: " + e.getMessage(), e);
        }
    }

    /**
     * Returns the next sequential counter (1, 2, 3, ...) for the given
     * branch and year, based on how many accounts already exist for that
     * branch/year combination. Used to build BRANCHCODE-YYYY-xxxxxx.
     */
    public int nextSequenceFor(Branch branch, int year) {
        String prefix = branch.getCode() + "-" + year + "-";
        String sql = "SELECT COUNT(*) FROM bank_accounts WHERE account_number LIKE ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read account sequence: " + e.getMessage(), e);
        }
    }

    /** Generates the next account number for the given branch, e.g. KLA-2026-000001. */
    public String generateAccountNumber(Branch branch) {
        int year = Year.now().getValue();
        int seq = nextSequenceFor(branch, year);
        return String.format("%s-%d-%06d", branch.getCode(), year, seq);
    }

    /** Inserts a newly opened account record. */
    public void saveAccount(Account account) {
        String sql = "INSERT INTO bank_accounts (account_number, first_name, last_name, nin, email, " +
                "phone_number, pin, date_of_birth, account_type, branch, opening_deposit, " +
                "second_nin, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account.getAccountNumber());
            ps.setString(2, account.getFirstName());
            ps.setString(3, account.getLastName());
            ps.setString(4, account.getNin());
            ps.setString(5, account.getEmail());
            ps.setString(6, account.getPhoneNumber());
            ps.setString(7, account.getPin());
            ps.setString(8, account.getDateOfBirth().toString());
            ps.setString(9, account.accountTypeName());
            ps.setString(10, account.getBranch().getDisplayName());
            ps.setDouble(11, account.getOpeningDeposit());
            ps.setString(12, account.getSecondNin());
            ps.setString(13, java.time.LocalDateTime.now().withNano(0).toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save account: " + e.getMessage(), e);
        }
    }
}

import java.sql.*;
import java.time.LocalDate;

public class SQL_Command {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "8328";


    public static void addperson(String fname, String lname) {
        String command = "INSERT INTO Person (first_name, last_name) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(command)) {
            pstmt.setString(1, fname);
            pstmt.setString(2, lname);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void addbook(String title, String author, Integer year, Integer copies) {
        String command = "INSERT INTO Books (title, author, publish_year, copies) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(command)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, year);
            pstmt.setInt(4, copies);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void CheckoutBook(int personId, int bookId) {
        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(14);
        String insertHistory = "INSERT INTO BookHistory (person_id, book_id, due_date) VALUES (?, ?, ?)";
        String updateStock = "UPDATE Books SET copies = copies - 1 WHERE book_id = ? AND copies > 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt1 = conn.prepareStatement(insertHistory);
                 PreparedStatement pstmt2 = conn.prepareStatement(updateStock)) {
                pstmt1.setInt(1, personId);
                pstmt1.setInt(2, bookId);
                pstmt1.setDate(3, Date.valueOf(dueDate));
                pstmt1.executeUpdate();
                pstmt2.setInt(1, bookId);
                int rowsAffected = pstmt2.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit();
                    System.out.println("Checkout successful!");
                } else {
                    System.out.println("Error: No copies available for this book.");
                    conn.rollback();
                }
            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void returnBook(int personId, int bookId) {
        String updateHistory = "UPDATE BookHistory SET return_date = CURRENT_DATE " +
                "WHERE person_id = ? AND book_id = ? AND return_date IS NULL";
        String updateStock = "UPDATE Books SET copies = copies + 1 WHERE book_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt1 = conn.prepareStatement(updateHistory);
                 PreparedStatement pstmt2 = conn.prepareStatement(updateStock)) {
                pstmt1.setInt(1, personId);
                pstmt1.setInt(2, bookId);
                int historyUpdated = pstmt1.executeUpdate();
                if (historyUpdated > 0) {
                    pstmt2.setInt(1, bookId);
                    pstmt2.executeUpdate();
                    conn.commit();
                } else {
                    conn.rollback();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static int findperson(String fname, String lname) {
        String command = "SELECT person_id FROM Person WHERE first_name = ? AND last_name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(command)) {
            pstmt.setString(1, fname);
            pstmt.setString(2, lname);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("person_id");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static int findbook(String title) {
        String command = "SELECT book_id FROM Books WHERE title = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(command)) {
            pstmt.setString(1, title);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("book_id");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static void CheckoutHistory() {
        String command = "SELECT p.first_name, p.last_name, b.title, h.due_date, h.return_date " +
                "FROM BookHistory h " +
                "JOIN Person p ON h.person_id = p.person_id " +
                "JOIN Books b ON h.book_id = b.book_id " +
                "WHERE h.return_date IS NOT NULL " +
                "ORDER BY h.return_date DESC";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(command);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("--- Completed Returns History ---");
            while (rs.next()) {
                String fname = rs.getString("first_name");
                String lname = rs.getString("last_name");
                String title = rs.getString("title");
                Date dueDate = rs.getDate("due_date");
                Date return_at = rs.getDate("return_date");
                System.out.println(fname + " " + lname + " | " + title + " | Due: " + dueDate + " | Time Returned: " + return_at);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void Loaningout() {
        String command = "SELECT p.first_name, p.last_name, b.title, h.due_date " +
                "FROM BookHistory h " +
                "JOIN Person p ON h.person_id = p.person_id " +
                "JOIN Books b ON h.book_id = b.book_id " +
                "WHERE h.return_date IS NULL";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(command);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("--- Currently Loaned Out ---");
            while (rs.next()) {
                String fname = rs.getString("first_name");
                String lname = rs.getString("last_name");
                String title = rs.getString("title");
                Date dueDate = rs.getDate("due_date");
                System.out.println(fname + " " + lname + " | " + title + " | Due: " + dueDate);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void AllBooks() {
        String command = "SELECT title, author, publish_year, copies FROM Books";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(command);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("--- All of our books ---");
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                int year = rs.getInt("publish_year");
                int copies = rs.getInt("copies");
                System.out.println("Title: " + title + " | Author: " + author + " | Publish Year: " + year + " | Copies: " + copies);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void AllPeople() {
        String command = "SELECT first_name, last_name FROM Person";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(command);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("--- All People ---");
            while (rs.next()) {
                String First = rs.getString("first_name");
                String Last = rs.getString("last_name");
                System.out.println("First Name: " + First + " | Last Name: " + Last);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void delete_person(String fname, String lname) {
        String delperson = "DELETE FROM Person WHERE first_name = ? AND last_name = ?";
        String delreturn = "DELETE FROM BookHistory WHERE person_id = ?";
        int IDperson = findperson(fname,lname);
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(delperson);
             PreparedStatement pstmt2 = conn.prepareStatement(delreturn)) {
            conn.setAutoCommit(false);
            pstmt.setString(1, fname);
            pstmt.setString(2, lname);
            pstmt2.setInt(1, IDperson);
            pstmt2.executeUpdate();
            pstmt.executeUpdate();

            conn.commit();
            System.out.println("Person deleted successfully.\n");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void delete_book(String title) {
        String delbook = "DELETE FROM Books WHERE title = ?";
        String delreturn = "DELETE FROM BookHistory WHERE book_id = ?";

        int IDbook = findbook(title);
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(delbook);
             PreparedStatement pstmt2 = conn.prepareStatement(delreturn)) {

            conn.setAutoCommit(false);
            pstmt.setString(1, title);
            pstmt2.setInt(1, IDbook);

            pstmt2.executeUpdate();
            pstmt.executeUpdate();

            conn.commit();
            System.out.println("Book deleted successfully.\n");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
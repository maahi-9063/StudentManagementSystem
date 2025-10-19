import java.sql.*;
import java.util.Scanner;

public class StudentManagementSystem {

    // Change these according to your DB setup
   //static final String URL = "jdbc:mysql://localhost:3306/student_db";
   static final String URL = "jdbc:mysql://localhost:3306/student_db?useSSL=false&allowPublicKeyRetrieval=true";

static final String USER = "javauser";
static final String PASSWORD = "pass123";


    static Connection conn = null;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database connected successfully!");

            int choice;
            do {
                System.out.println("\n--- Student Management System ---");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> viewStudents();
                    case 3 -> updateStudent();
                    case 4 -> deleteStudent();
                    case 5 -> System.out.println("👋 Exiting...");
                    default -> System.out.println("❌ Invalid choice, try again.");
                }
            } while (choice != 5);

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addStudent() {
        try {
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Roll No: ");
            String rollNo = sc.nextLine();
            System.out.print("Enter Course: ");
            String course = sc.nextLine();
            System.out.print("Enter Marks: ");
            double marks = sc.nextDouble();

            String sql = "INSERT INTO students(name, roll_no, course, marks) VALUES(?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, rollNo);
            ps.setString(3, course);
            ps.setDouble(4, marks);

            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("✅ Student added successfully!");
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void viewStudents() {
        try {
            String sql = "SELECT * FROM students";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n--- Student List ---");
            System.out.printf("%-5s %-20s %-15s %-15s %-5s\n", "ID", "Name", "Roll No", "Course", "Marks");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-15s %-15s %-5.2f\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("roll_no"),
                        rs.getString("course"),
                        rs.getDouble("marks"));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateStudent() {
        try {
            System.out.print("Enter Roll No of Student to Update: ");
            String rollNo = sc.nextLine();

            System.out.print("Enter New Course: ");
            String newCourse = sc.nextLine();
            System.out.print("Enter New Marks: ");
            double newMarks = sc.nextDouble();

            String sql = "UPDATE students SET course = ?, marks = ? WHERE roll_no = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newCourse);
            ps.setDouble(2, newMarks);
            ps.setString(3, rollNo);

            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("✅ Student updated successfully!");
            else System.out.println("❌ No student found with that Roll No.");
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteStudent() {
        try {
            System.out.print("Enter Roll No of Student to Delete: ");
            String rollNo = sc.nextLine();

            String sql = "DELETE FROM students WHERE roll_no = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, rollNo);

            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("✅ Student deleted successfully!");
            else System.out.println("❌ No student found with that Roll No.");
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


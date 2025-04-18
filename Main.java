import java.sql.*;
import java.util.Scanner;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username or email: ");
        String input = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Dotenv dotenv = Dotenv.load();

            String dbUrl = dotenv.get("DB_URL");
            String dbUser = dotenv.get("DB_USER");
            String dbPassword = dotenv.get("DB_PASSWORD");

            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            boolean loggedIn = false;

            String loginQuery = "SELECT role FROM users WHERE username = ? AND password = ?";
            PreparedStatement adminStmt = conn.prepareStatement(loginQuery);
            adminStmt.setString(1, input);
            adminStmt.setString(2, password);
            ResultSet rs = adminStmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println("\u2705 Login successful. Role: " + role);

                if (role.equalsIgnoreCase("admin")) {
                    System.out.println("\u2696\ufe0f Welcome Admin! You have full access.");
                    System.out.print("\nDo you want to update any employee information? (y/n): ");
                    String updateChoice = scanner.nextLine();
                    if (updateChoice.equalsIgnoreCase("y")) {
                        searchEmployeeData(conn, scanner, true);
                    } else {
                        System.out.println("No updates will be made.");
                    }
                    while (true) {
                        System.out.print("\nDo you still want to search for employee information? (y/n): ");
                        String searchAgain = scanner.nextLine();
                        if (searchAgain.equalsIgnoreCase("n")) {
                            System.out.println("Exiting the program...");
                            break;
                        } else if (searchAgain.equalsIgnoreCase("y")) {
                            searchEmployeeData(conn, scanner, false);
                        } else {
                            System.out.println("Invalid input. Please type 'y' or 'n'.");
                        }
                    }
                    loggedIn = true;
                }
            }

            if (!loggedIn && password.equals("emppass")) {
                String empQuery = "SELECT e.empid, e.Fname, e.Lname, e.email, e.HireDate, e.Salary, " +
                                  "RIGHT(e.SSN, 4) AS last4SSN, d.Name AS division_name, jt.job_title " +
                                  "FROM employees e " +
                                  "LEFT JOIN employee_division ed ON e.empid = ed.empid " +
                                  "LEFT JOIN division d ON ed.div_ID = d.ID " +
                                  "LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                                  "LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                                  "WHERE e.email = ?";
                                  
                PreparedStatement empStmt = conn.prepareStatement(empQuery);
                empStmt.setString(1, input);
                ResultSet empData = empStmt.executeQuery();

                if (empData.next()) {
                    System.out.println("\u2705 Login successful. Role: employee");
                    System.out.println("\ud83d\udc64 Welcome Employee. Fetching your data...");
                    System.out.println("\n--- Your Employee Info ---");
                    System.out.println("Name: " + empData.getString("Fname") + " " + empData.getString("Lname"));
                    System.out.println("Employee ID: " + empData.getInt("empid"));
                    System.out.println("Email: " + empData.getString("email"));
                    System.out.println("Hire Date: " + empData.getDate("HireDate"));
                    System.out.println("Salary: $" + empData.getDouble("Salary"));
                    System.out.println("Division: " + empData.getString("division_name"));
                    System.out.println("Job Title: " + empData.getString("job_title"));
                    System.out.println("SSN (last 4): ****-**-" + empData.getString("last4SSN"));
                    loggedIn = true;
                }

                empData.close();
                empStmt.close();
            }

            if (!loggedIn) {
                System.out.println("\u274c Login failed. Invalid credentials.");
            }

            conn.close();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void searchEmployeeData(Connection conn, Scanner scanner, boolean isAdmin) {
        String baseQuery = "SELECT e.*, a.DOB, d.Name AS division_name, jt.job_title " +
                           "FROM employees e " +
                           "JOIN address a ON e.empid = a.empid " +
                           "LEFT JOIN employee_division ed ON e.empid = ed.empid " +
                           "LEFT JOIN division d ON ed.div_ID = d.ID " +
                           "LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                           "LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id ";

        PreparedStatement searchStmt = null;

        System.out.println("\nSearch employee by:");
        System.out.println("1. View all employees");
        System.out.println("2. Name");
        System.out.println("3. HireDate");
        System.out.println("4. SSN");
        System.out.println("5. Employee ID");
        System.out.println("6. Date of Birth (DOB)");
        System.out.print("Enter choice (1-6): ");
        String choice = scanner.nextLine();

        try {
            if (choice.equals("1")) {
                searchStmt = conn.prepareStatement(baseQuery);
                ResultSet empResults = searchStmt.executeQuery();

                while (empResults.next()) {
                    displayEmployeeInfo(empResults);
                }
                empResults.close();

                if (isAdmin) {
                    System.out.print("\nDo you want to update any employee information? (y/n): ");
                    String updateChoice = scanner.nextLine();
                    if (updateChoice.equalsIgnoreCase("y")) {
                        System.out.print("\nEnter Employee ID to update: ");
                        int empidToUpdate = Integer.parseInt(scanner.nextLine().trim());
                        updateEmployeeData(conn, scanner, empidToUpdate);
                    }
                }

            } else if (choice.equals("2")) {
                System.out.print("Enter first name: ");
                String fname = scanner.nextLine();
                System.out.print("Enter last name: ");
                String lname = scanner.nextLine();

                String query = baseQuery + " WHERE e.Fname LIKE ? AND e.Lname LIKE ?";
                searchStmt = conn.prepareStatement(query);
                searchStmt.setString(1, "%" + fname + "%");
                searchStmt.setString(2, "%" + lname + "%");

                ResultSet nameResults = searchStmt.executeQuery();
                boolean found = false;
                while (nameResults.next()) {
                    displayEmployeeInfo(nameResults);
                    found = true;
                }
                if (!found) {
                    System.out.println("No employee found with the name " + fname + " " + lname);
                }
                nameResults.close();

            } else if (choice.equals("3")) {
                System.out.print("Enter Hire Date (yyyy-mm-dd): ");
                String date = scanner.nextLine();
                String query = baseQuery + " WHERE e.HireDate = ?";
                searchStmt = conn.prepareStatement(query);
                searchStmt.setString(1, date);

                ResultSet hireDateResults = searchStmt.executeQuery();
                while (hireDateResults.next()) {
                    displayEmployeeInfo(hireDateResults);
                }
                hireDateResults.close();

            } else if (choice.equals("4")) {
                System.out.print("Enter SSN: ");
                String ssn = scanner.nextLine();
                String query = baseQuery + " WHERE e.SSN = ?";
                searchStmt = conn.prepareStatement(query);
                searchStmt.setString(1, ssn);

                ResultSet ssnResults = searchStmt.executeQuery();
                while (ssnResults.next()) {
                    displayEmployeeInfo(ssnResults);
                }
                ssnResults.close();

            } else if (choice.equals("5")) {
                System.out.print("Enter Employee ID: ");
                int empid = Integer.parseInt(scanner.nextLine().trim());
                String query = baseQuery + " WHERE e.empid = ?";
                searchStmt = conn.prepareStatement(query);
                searchStmt.setInt(1, empid);

                ResultSet empidResults = searchStmt.executeQuery();
                while (empidResults.next()) {
                    displayEmployeeInfo(empidResults);
                }
                empidResults.close();

            } else if (choice.equals("6")) {
                System.out.print("Enter Date of Birth (yyyy-mm-dd): ");
                String dob = scanner.nextLine();
                String query = baseQuery + " WHERE a.DOB = ?";
                searchStmt = conn.prepareStatement(query);
                searchStmt.setString(1, dob);

                ResultSet dobResults = searchStmt.executeQuery();
                boolean found = false;
                while (dobResults.next()) {
                    displayEmployeeInfo(dobResults);
                    found = true;
                }
                if (!found) {
                    System.out.println("No employees found with DOB: " + dob);
                }
                dobResults.close();

            } else {
                System.out.println("❌ Invalid input. Please try again.");
                return;
            }

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format. Please enter a valid number.");
        } finally {
            try {
                if (searchStmt != null) searchStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void displayEmployeeInfo(ResultSet empResults) throws SQLException {
        System.out.println("\n--- Employee Info ---");
        System.out.println("ID: " + empResults.getInt("empid"));
        System.out.println("Name: " + empResults.getString("Fname") + " " + empResults.getString("Lname"));
        System.out.println("Email: " + empResults.getString("email"));
        System.out.println("Hire Date: " + empResults.getDate("HireDate"));
        System.out.println("Salary: $" + empResults.getDouble("Salary"));
        System.out.println("SSN: " + empResults.getString("SSN"));
        System.out.println("DOB: " + empResults.getDate("DOB"));
        System.out.println("Division: " + empResults.getString("division_name"));
        System.out.println("Job Title: " + empResults.getString("job_title"));
        System.out.println("----------------------------------");
    }

    private static void updateEmployeeData(Connection conn, Scanner scanner, int empidToUpdate) {
        try {
            System.out.println("\nEmployee Information for Update: ");
            String query = "SELECT e.*, a.DOB, d.Name AS division_name, jt.job_title " +
                           "FROM employees e " +
                           "JOIN address a ON e.empid = a.empid " +
                           "LEFT JOIN employee_division ed ON e.empid = ed.empid " +
                           "LEFT JOIN division d ON ed.div_ID = d.ID " +
                           "LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                           "LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                           "WHERE e.empid = ?";
                           
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, empidToUpdate);
            ResultSet empResults = stmt.executeQuery();

            if (empResults.next()) {
                displayEmployeeInfo(empResults);

                System.out.println("\nWhat would you like to update?");
                System.out.println("1. Salary");
                System.out.println("2. Job Title");
                System.out.println("3. Division");
                System.out.println("4. Done");
                System.out.print("Enter your choice (1-4): ");
                String choice = scanner.nextLine().trim();

                // Rest of the updateEmployeeData method remains the same
                // ... [keep the existing implementation here]
                
            } else {
                System.out.println("❌ Employee not found!");
            }
            empResults.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format. Please enter a valid number.");
        }
    }
}
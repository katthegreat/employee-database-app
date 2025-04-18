import java.sql.*;
import java.util.HashMap;
import java.util.Map;
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
            // added this dotenv for url, user, and password security
            Dotenv dotenv = Dotenv.load();

            String dbUrl = dotenv.get("DB_URL");
            String dbUser = dotenv.get("DB_USER");
            String dbPassword = dotenv.get("DB_PASSWORD");

            Connection conn = DriverManager.getConnection(
                // contains the variables from above
                dbUrl,
                dbUser, 
                dbPassword //change password in env file to your sql password (same for user if its not root)
            );

            boolean loggedIn = false;

            // Admin login
            String loginQuery = "SELECT role FROM users WHERE username = ? AND password = ?";
            PreparedStatement adminStmt = conn.prepareStatement(loginQuery);
            adminStmt.setString(1, input);
            adminStmt.setString(2, password);
            ResultSet rs = adminStmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println("✅ Login successful. Role: " + role);

                if (role.equalsIgnoreCase("admin")) {
                    System.out.println("⚖️ Welcome Admin! You have full access.");

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

            // Employee login using email and static password
            if (!loggedIn && password.equals("emppass")) {
                String empQuery = "SELECT empid, Fname, Lname, email, HireDate, Salary, RIGHT(SSN, 4) AS last4SSN FROM employees WHERE email = ?";
                PreparedStatement empStmt = conn.prepareStatement(empQuery);
                empStmt.setString(1, input);
                ResultSet empData = empStmt.executeQuery();

                if (empData.next()) {
                    System.out.println("✅ Login successful. Role: employee");
                    System.out.println("👤 Welcome Employee. Fetching your data...");
                    System.out.println("\n--- Your Employee Info ---");
                    System.out.println("Name: " + empData.getString("Fname") + " " + empData.getString("Lname"));
                    System.out.println("Employee ID: " + empData.getInt("empid"));
                    System.out.println("Email: " + empData.getString("email"));
                    System.out.println("Hire Date: " + empData.getDate("HireDate"));
                    System.out.println("Salary: $" + empData.getDouble("Salary"));
                    System.out.println("SSN (last 4): ****-**-" + empData.getString("last4SSN"));
                    loggedIn = true;
                }

                empData.close();
                empStmt.close();
            }

            if (!loggedIn) {
                System.out.println("❌ Login failed. Invalid credentials.");
            }

            conn.close();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void searchEmployeeData(Connection conn, Scanner scanner, boolean isAdmin) {
        String query = "";
        PreparedStatement searchStmt = null;

        System.out.println("\nSearch employee by:");
        System.out.println("1. View all employees");
        System.out.println("2. Name");
        System.out.println("3. HireDate");
        System.out.println("4. SSN");
        System.out.println("5. Employee ID");
        System.out.print("Enter choice (1-5): ");
        String choice = scanner.nextLine();

        try {
            if (choice.equals("1")) {
                query = "SELECT * FROM employees";
                searchStmt = conn.prepareStatement(query);
                ResultSet empResults = searchStmt.executeQuery();

                while (empResults.next()) {
                    displayEmployeeInfo(empResults);
                }

                empResults.close();

                System.out.print("\nDo you want to update any employee information? (y/n): ");
                String updateChoice = scanner.nextLine();

                if (updateChoice.equalsIgnoreCase("y")) {
                    System.out.print("\nEnter Employee ID to update: ");
                    int empidToUpdate = Integer.parseInt(scanner.nextLine().trim());
                    updateEmployeeData(conn, scanner, empidToUpdate);
                }

            } else if (choice.equals("2")) {
                System.out.print("Enter first name: ");
                String fname = scanner.nextLine();
                System.out.print("Enter last name: ");
                String lname = scanner.nextLine();

                query = "SELECT * FROM employees WHERE Fname LIKE ? AND Lname LIKE ?";
                searchStmt = conn.prepareStatement(query);
                searchStmt.setString(1, "%" + fname + "%");
                searchStmt.setString(2, "%" + lname + "%");

                ResultSet nameResults = searchStmt.executeQuery();
                if (nameResults.next()) {
                    displayEmployeeInfo(nameResults);
                } else {
                    System.out.println("No employee found with the name " + fname + " " + lname);
                }

                nameResults.close();
            } else if (choice.equals("3")) {
                System.out.print("Enter Hire Date (yyyy-mm-dd): ");
                String date = scanner.nextLine();
                query = "SELECT * FROM employees WHERE HireDate = ?";
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
                query = "SELECT * FROM employees WHERE SSN = ?";
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
                query = "SELECT * FROM employees WHERE empid = ?";
                searchStmt = conn.prepareStatement(query);
                searchStmt.setInt(1, empid);

                ResultSet empidResults = searchStmt.executeQuery();
                while (empidResults.next()) {
                    displayEmployeeInfo(empidResults);
                }

                empidResults.close();
            } else {
                System.out.println("❌ Invalid input. Please try again.");
                return;
            }

            if (!isAdmin) {
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
    }

    private static void updateEmployeeData(Connection conn, Scanner scanner, int empidToUpdate) {
        try {
            System.out.println("\nEmployee Information for Update: ");
            String query = "SELECT * FROM employees WHERE empid = ?";
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

                if (choice.equals("1")) {
                    
                    // two choices - 1. enter specific salary or 2. apply % increase
                    double currentSalary = empResults.getDouble("Salary");
                    System.out.println("\nCurrent Salary: $" + currentSalary);
                    System.out.println("Select raise type:");
                    System.out.println("1. Enter specific new salary");
                    System.out.println("2. Apply percentage increase");
                    System.out.print("Enter choice (1-2): ");
                    String salaryChoice = scanner.nextLine().trim();

                    if (salaryChoice.equals("1")) {
                        // direct salary update
                        // this is the choice 1
                        System.out.print("Enter new salary: ");
                        double newSalary = Double.parseDouble(scanner.nextLine().trim());
                        String updateSalary = "UPDATE employees SET Salary = ? WHERE empid = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSalary);
                        updateStmt.setDouble(1, newSalary);
                        updateStmt.setInt(2, empidToUpdate);
                        updateStmt.executeUpdate();
                        System.out.println("✅ Salary updated to $" + newSalary);
                        
                    } // choice 2 here for % salary increase
                    else if (salaryChoice.equals("2")) {
                        //  print statements for % increase 
                        System.out.println("\nAvailable Raise Options:");
                        System.out.println("1. 3.2% for salaries $58K-$105K");
                        System.out.println("2. 4.5% for salaries under $58K");
                        System.out.println("3. 2.1% for salaries $105K+");
                        System.out.print("Select raise percentage (1-3): ");
                        String raiseChoice = scanner.nextLine().trim();

                        double percentage = 0;
                        String rangeDescription = "";

                        // switch statment to check range
                        switch (raiseChoice) {
                            case "1":
                                if (currentSalary >= 58000 && currentSalary < 105000) {
                                    percentage = 3.2;
                                    rangeDescription = "58k-105k range (3.2%)";
                                } else {
                                    System.out.println("❌ Employee salary not in 58K-105K range");
                                    return;
                                }
                                
                                break;
                            
                            case "2" :
                                if (currentSalary < 58000) {
                                    percentage = 4.5;
                                    rangeDescription = "under 58K range (4.5%)";
                                } else {
                                    System.out.println("❌ Employee salary not under 58K");
                                    return;
                                }
                                break;

                                case "3":
                                if (currentSalary >= 105000) {
                                    percentage = 2.1;
                                    rangeDescription = "105K+ range (2.1%)";
                                } else {
                                    System.out.println("❌ Employee salary not 105K+");
                                    return;
                                }
                                break;

                            default:
                                System.out.println("❌ Invalid raise selection");
                                // break;
                                return;
                        }

                        double newSalary = currentSalary * (1 + percentage/100);
                        newSalary = Math.round(newSalary * 100) / 100.0; //round to 2 decimal places
                        
                        String updateSalary = "UPDATE employees SET Salary = ? WHERE empid = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSalary);
                        updateStmt.setDouble(1, newSalary);
                        updateStmt.setInt(2, empidToUpdate);
                        updateStmt.execute();

                        System.out.printf("\nApplied %s raise\n", rangeDescription);
                        System.out.printf("  Old Salary: $%.2f\n", currentSalary);
                        System.out.printf("  New Salary: $%.2f\n", newSalary);
                        System.out.printf("  Increase: $%.2f (%.1f%%)\n", newSalary - currentSalary, percentage);
                    }

                } else if (choice.equals("2")) {
                    // Show job title options
                    System.out.println("\nEmployee Job Title Manager");
                    System.out.println("(100, 'software manager')");
                    System.out.println("(101, 'software architect')");
                    System.out.println("(102, 'software engineer')");
                    System.out.println("(103, 'software developer')");
                    System.out.println("(200, 'marketing manager')");
                    System.out.println("(201, 'marketing associate')");
                    System.out.println("(202, 'marketing assistant')");
                    System.out.println("(900, 'Chief Exec. Officer')");
                    System.out.println("(901, 'Chief Finn. Officer')");
                    System.out.println("(902, 'Chief Info. Officer')");
                
                    System.out.print("Enter new job title id: "); 
                    String jobTitleId = scanner.nextLine().trim();

                    // switch statement to match user input
                    String jobTitleText;
                    switch(jobTitleId) {
                        case "100": jobTitleText = "software manager"; break;
                        case "101": jobTitleText = "software architect"; break;
                        case "102": jobTitleText = "software engineer"; break;
                        case "103": jobTitleText = "software developer"; break;
                        case "200": jobTitleText = "marketing manager"; break;
                        case "201": jobTitleText = "marketing associate"; break;
                        case "202": jobTitleText = "marketing assistant"; break;
                        case "900": jobTitleText = "Chief Exec. Officer"; break;
                        case "901": jobTitleText = "Chief Finn. Officer"; break;
                        case "902": jobTitleText = "Chief Info. Officer"; break;
                        default: jobTitleText = "Unknown";
                    }

                    String updateJob = "INSERT INTO employee_job_titles (empid, job_title_id) VALUES (?, ?) " + "ON DUPLICATE KEY UPDATE job_title_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateJob);
                    updateStmt.setInt(1, empidToUpdate);
                    updateStmt.setString(2, jobTitleId);
                    updateStmt.setString(3, jobTitleId);
                    
                    updateStmt.executeUpdate();
                    System.out.println("✅ Job Title updated to: " + jobTitleText);

                } else if (choice.equals("3")) {
                    System.out.print("Enter new division: ");
                    String newDivision = scanner.nextLine().trim();

                    String updateDiv = "INSERT INTO employee_division (empid, div_ID) VALUES (?, ?) " + "ON DUPLICATE KEY UPDATE div_ID = ?";
                    
                    PreparedStatement updateStmt = conn.prepareStatement(updateDiv);
                    updateStmt.setInt(1, empidToUpdate);
                    updateStmt.setString(2, newDivision);
                    updateStmt.setString(3, newDivision);
                    updateStmt.executeUpdate();
                    System.out.println("✅ Division updated to: " + newDivision);

                } else if (choice.equals("4")) {
                    System.out.println("✅ Done. Returning to main menu.");
                } else {
                    System.out.println("❌ Invalid choice. Returning to menu.");
                }

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

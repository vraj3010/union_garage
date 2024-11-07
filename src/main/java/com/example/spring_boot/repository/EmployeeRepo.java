package com.example.spring_boot.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;
import org.apache.el.lang.ELArithmetic.BigDecimalDelegate;

import com.example.spring_boot.config.DatabaseConnector;
import com.example.spring_boot.entity.Employee;
import com.example.spring_boot.entity.Renter;
import com.example.spring_boot.entity.UserDetails;
import java.util.List;
import java.util.ArrayList;
public class EmployeeRepo{

    public static void add_new_emp(long emp_id) {
        String sql = "INSERT INTO employee (emp_id) VALUES (?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, emp_id);

            pstmt.executeUpdate();
            System.out.println("Employee added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update customer details
    public static void upd_emp_detail(Long emp_id, String column, String newValue) {
        String sql = "UPDATE employee SET " + column + " = ? WHERE emp_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newValue);
            pstmt.setLong(2, emp_id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee details updated successfully.");
            } else {
                System.out.println("Employee not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Overloaded method to update numeric fields like phone number, AadharNo
    public static void upd_emp_detail(Long emp_id, String column, BigDecimal newValue) {
        String sql = "UPDATE employee SET " + column + " = ? WHERE emp_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, newValue);
            pstmt.setLong(2, emp_id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee details updated successfully.");
            } else {
                System.out.println("Employee not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    public static void upd_emp_detail(Long emp_id, String column, Integer newValue) {
        String sql = "UPDATE employee SET " + column + " = ? WHERE emp_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newValue);
            pstmt.setLong(2, emp_id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee details updated successfully.");
            } else {
                System.out.println("Employee not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Method to query an employee by ID and return as a Map
    public static Employee getCustomerById(Long custId) {
        String sql = "SELECT * FROM employee WHERE emp_id = ?";
        Employee customerData = null; // Initialize as null

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, custId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                customerData = new Employee(); // Initialize customerData here
                int columnCount = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    // Fixing variable name from userDetails to customerData
                    switch (columnName) {
                        case "emp_id": // Changed to match database column name
                            customerData.setEmpId((Long) columnValue);
                            break;
                        case "salary":
                            customerData.setSalary((Integer) columnValue);
                            break;
                        case "phoneNo":
                            customerData.setPhoneNo((BigDecimal) columnValue);
                            break;
                        case "dept_id":
                            customerData.setDeptId((Long) columnValue);
                            break;
                        case "first_name":
                            customerData.setFirstName((String) columnValue);
                            break;
                        case "middle_name":
                            customerData.setMiddleName((String) columnValue);
                            break;
                        case "last_name":
                            customerData.setLastName((String) columnValue);
                            break;
                        case "houseNo":
                            customerData.setHouseNo((String) columnValue);
                            break;
                        case "street":
                            customerData.setStreet((String) columnValue);
                            break;
                        case "city":
                            customerData.setCity((String) columnValue);
                            break;
                        case "state":
                            customerData.setState((String) columnValue);
                            break;
                        case "AadharNo":
                            customerData.setAadharNo((BigDecimal) columnValue);
                            break;
                        default:
                            break; // or handle unexpected columns
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerData; // This could return null if not found
    }

    // 1. Get all employees
    public static List<Employee> getAllEmployees() {
        List<Employee> employeeList = new ArrayList<>();
        String query = "SELECT * FROM employee";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmpId(rs.getLong("emp_id"));
                employee.setSalary(rs.getInt("salary"));
                employee.setPhoneNo(rs.getBigDecimal("phoneNo"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setMiddleName(rs.getString("middle_name"));
                employee.setLastName(rs.getString("last_name"));
                employee.setHouseNo(rs.getString("houseNo"));
                employee.setStreet(rs.getString("street"));
                employee.setCity(rs.getString("city"));
                employee.setState(rs.getString("state"));
                employee.setAadharNo(rs.getBigDecimal("AadharNo"));
                employee.setDeptId(rs.getLong("dept_id"));

                employeeList.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employeeList;
    }

    // 2. Update salary by employee ID
    public static boolean updateSalaryByEmpId(long empId, int newSalary) {
        String query = "UPDATE employee SET salary = ? WHERE emp_id = ?";
        boolean isUpdated = false;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, newSalary);
            stmt.setLong(2, empId);
            int rowsAffected = stmt.executeUpdate();
            isUpdated = rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    // 3. Delete employee by employee ID
    public static boolean deleteEmployeeByEmpId(long empId) {
        String query = "DELETE FROM employee WHERE emp_id = ?";
        boolean isDeleted = false;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, empId);
            int rowsAffected = stmt.executeUpdate();
            isDeleted = rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isDeleted;
    }

    public static List<Renter> getUnapprovedRenters() {
        List<Renter> unapprovedRenters = new ArrayList<>();
        String sql = "SELECT * FROM renter WHERE status = 'Pending'";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Renter renter = new Renter();
                renter.setRenter_id(rs.getLong("renter_id"));
                renter.setDriver_history(rs.getString("driver_history"));
                renter.setLicense_proof(rs.getString("license_proof"));
                renter.setForm_path(rs.getString("form_path"));
                renter.setLast_rental_date(rs.getDate("last_rental_date"));
                renter.setSignup_date(rs.getDate("signup_date"));
                renter.setReferral_source(rs.getString("referral_source"));
                renter.setStatus(rs.getString("status"));

                unapprovedRenters.add(renter);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return unapprovedRenters;
    }

    public static void verifyRenter(long emp_id, long renter_id) {
        String sql = "UPDATE renter SET status = 'Approved', verifying_emp = ? WHERE renter_id = ?";
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setLong(1, emp_id);  // Set verifying_emp to emp_id
            pstmt.setLong(2, renter_id);  // Identify the specific renter by renter_id
    
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Renter verified successfully.");
            } else {
                System.out.println("Renter not found.");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();}
    }
}

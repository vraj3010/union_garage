package com.example.spring_boot.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.spring_boot.entity.*;
import com.example.spring_boot.config.*;
public class CarsDAO {

    // Method to add a new car entry
    public static void addNewCar(Cars car) {
        String sql = "INSERT INTO cars (vin, proof, customer_id, car_name, model, year, color, mileage) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, car.getVin());
            pstmt.setBytes(2, car.getProof());
            pstmt.setObject(3, car.getCustomerId());  // Allows NULL for nullable Long
            pstmt.setString(4, car.getCarName());
            pstmt.setString(5, car.getModel());
            pstmt.setInt(6, car.getYear());
            pstmt.setString(7, car.getColor());
            pstmt.setObject(8, car.getMileage());  // Allows NULL for nullable Integer

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Car entry added successfully!");
            } else {
                System.out.println("Failed to add car entry.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Cars> getAllCarsByCustomerId(Long customerId) {
        String sql = "SELECT cid, vin, proof, customer_id, car_name, model, year, color, mileage FROM cars WHERE customer_id = ?";
        List<Cars> carsList = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Cars car = new Cars();
                car.setCid(rs.getLong("cid"));
                car.setVin(rs.getString("vin"));
                car.setProof(rs.getBytes("proof"));
                car.setCustomerId(rs.getLong("customer_id"));
                car.setCarName(rs.getString("car_name"));
                car.setModel(rs.getString("model"));
                car.setYear(rs.getInt("year"));
                car.setColor(rs.getString("color"));
                car.setMileage(rs.getObject("mileage") != null ? rs.getInt("mileage") : null); // Handle nullable mileage

                carsList.add(car);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carsList;
    }


    public static byte[] getProofDocumentByCarId(int carId) {
        String sql = "SELECT proof FROM cars WHERE cid = ?";
        byte[] proofDocument = null;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, carId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                proofDocument = rs.getBytes("proof");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return proofDocument;
    }

    public static Cars getCarById(long carId) {
        String sql = "SELECT cid, vin, proof, customer_id, car_name, model, year, color, mileage FROM cars WHERE cid = ?";
        Cars car = null;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, carId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                car = new Cars();
                car.setCid(rs.getLong("cid"));
                car.setVin(rs.getString("vin"));
                car.setProof(rs.getBytes("proof"));
                car.setCustomerId(rs.getLong("customer_id"));
                car.setCarName(rs.getString("car_name"));
                car.setModel(rs.getString("model"));
                car.setYear(rs.getInt("year"));
                car.setColor(rs.getString("color"));
                car.setMileage(rs.getInt("mileage"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return car;}
}
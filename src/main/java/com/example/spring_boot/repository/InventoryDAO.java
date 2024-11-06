package com.example.spring_boot.repository;
import com.example.spring_boot.config.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.spring_boot.entity.*;
public class InventoryDAO {

    // Method to fetch all inventory entries
    public static List<Inventory> getAllInventory() {
        List<Inventory> inventoryList = new ArrayList<>();
        String query = "SELECT inventory_id, location, sell_quantity, rent_quantity, model_id FROM inventory";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Inventory inventory = new Inventory();
                inventory.setInventoryId(rs.getLong("inventory_id"));
                inventory.setLocation(rs.getString("location"));
                inventory.setSellQuantity(rs.getInt("sell_quantity"));
                inventory.setRentQuantity(rs.getInt("rent_quantity"));
                inventory.setModelId(rs.getLong("model_id"));

                inventoryList.add(inventory);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventoryList;
    }


    public static boolean updateInventoryQuantities(long inventoryId, int sellQuantityToAdd, int rentQuantityToAdd) {
        String query = "UPDATE inventory SET sell_quantity = sell_quantity + ?, rent_quantity = rent_quantity + ? WHERE inventory_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters
            stmt.setInt(1, sellQuantityToAdd);
            stmt.setInt(2, rentQuantityToAdd);
            stmt.setLong(3, inventoryId);

            // Execute update
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Return true if the update was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

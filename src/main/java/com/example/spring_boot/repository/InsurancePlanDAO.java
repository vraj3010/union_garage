package com.example.spring_boot.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.spring_boot.entity.*;
import com.example.spring_boot.config.*;
public class InsurancePlanDAO {

    public static List<InsurancePlan> getAllInsurancePlans() {
        String sql = "SELECT plan_id, plan_name, coverage_amt, deductible, premium_amt, frequency FROM insurance_plan";
        List<InsurancePlan> insurancePlans = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                InsurancePlan plan = new InsurancePlan();
                plan.setPlanId(rs.getInt("plan_id"));
                plan.setPlanName(rs.getString("plan_name"));
                plan.setCoverageAmt(rs.getInt("coverage_amt"));
                plan.setDeductible(rs.getInt("deductible"));
                plan.setPremiumAmt(rs.getInt("premium_amt"));
                plan.setFrequency(rs.getString("frequency"));

                insurancePlans.add(plan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return insurancePlans;
    }


    public static InsurancePlan getInsurancePlanById(int planId) {
        String sql = "SELECT plan_id, plan_name, coverage_amt, deductible, premium_amt, frequency FROM insurance_plan WHERE plan_id = ?";
        InsurancePlan insurancePlan = null;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, planId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                insurancePlan = new InsurancePlan();
                insurancePlan.setPlanId(rs.getInt("plan_id"));
                insurancePlan.setPlanName(rs.getString("plan_name"));
                insurancePlan.setCoverageAmt(rs.getInt("coverage_amt"));
                insurancePlan.setDeductible(rs.getInt("deductible"));
                insurancePlan.setPremiumAmt(rs.getInt("premium_amt"));
                insurancePlan.setFrequency(rs.getString("frequency"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return insurancePlan;
    }
}

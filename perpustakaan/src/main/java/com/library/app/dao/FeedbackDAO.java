package com.library.app.dao;

import com.library.app.config.DBConnection;
import com.library.app.model.Feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class FeedbackDAO {
    public void save(Feedback feedback) {
        String sql = "INSERT INTO feedbacks(member_id, sender_name, message) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (feedback.getMemberId() == null) {
                statement.setNull(1, Types.BIGINT);
            } else {
                statement.setLong(1, feedback.getMemberId());
            }
            statement.setString(2, feedback.getSenderName());
            statement.setString(3, feedback.getMessage());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Gagal menyimpan feedback.", exception);
        }
    }

}
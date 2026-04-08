package com.library.app.dao;

import com.library.app.config.DBConnection;
import com.library.app.model.ProcurementRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class ProcurementRequestDAO {
    public void save(ProcurementRequest request) {
        String sql = "INSERT INTO procurement_requests(member_id, requester_name, title, author, note, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (request.getMemberId() == null) {
                statement.setNull(1, Types.BIGINT);
            } else {
                statement.setLong(1, request.getMemberId());
            }
            statement.setString(2, request.getRequesterName());
            statement.setString(3, request.getTitle());
            statement.setString(4, request.getAuthor());
            statement.setString(5, request.getNote());
            statement.setString(6, request.getStatus().name());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Gagal menyimpan request pengadaan.", exception);
        }
    }

}

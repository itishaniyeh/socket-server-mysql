package com.socket.model.da;

import com.socket.database.HikariCPDataSource;
import com.socket.model.to.Packet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PacketDA {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public void insertPacket(Packet packet) {
        try {
            connection = HikariCPDataSource.getConnection();
            preparedStatement = connection.prepareStatement("insert into packets (timestamp, device_sn, packet) values(?,?,?);");
            preparedStatement.setString(1, packet.getTimestamp());
            preparedStatement.setString(2, packet.getDevice_sn());
            preparedStatement.setString(3, packet.getPacket());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            // Closing ResultSet Object
            if (resultSet != null) {
                resultSet.close();
            }
            // Closing PreparedStatement Object
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            // Closing Connection Object
            if (connection != null) {
                connection.close();
            }
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }
}

package org.rex.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Getter {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public Getter(Connection connection) {
        this.connection = connection;
    }
    
    public double getValueTransaction(int register){
        try {
            preparedStatement = connection.prepareStatement("SELECT value FROM transaction WHERE id = ?");
            preparedStatement.setDouble(1, register);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getDouble(1);
            } else {
                System.out.println("Error trying to get value");
                return -1;
            }
        } catch (SQLException e){
            System.out.println("Error trying to get value");
            return -1;
        }
    }

}

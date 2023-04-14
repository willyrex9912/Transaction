package org.rex.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnector {

    private String url = "jdbc:mysql://172.17.0.2:3306/transaction";
    private String user = "root";
    private String password = "1234@asdf";
    private Connection connection;

    public void initialize(){
        try {
            connection = DriverManager.getConnection(url, user, password);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            System.out.println("Connection successfully");
        } catch (SQLException e){
            System.out.println("Start connection error");
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return this.connection;
    }

    public void close(){
        if (connection!=null) {
            try {
                connection.close();
                System.out.println("Connection closed");
            } catch (SQLException e) {
                System.out.println("Close connection error");
                e.printStackTrace();
            }
        }
    }


}

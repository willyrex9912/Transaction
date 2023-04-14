package org.rex.thread;

import org.rex.db.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Timer;

public class Subtractor extends Thread{

    private Connection connection;
    private PreparedStatement preparedStatement;
    private double units;
    private int register;
    private Getter getter;
    private double threadSeconds;
    private double timerSeconds;
    private Timer timer;
    private double initialValue;
    public Subtractor(Connection connection){
        this.connection = connection;
        this.getter = new Getter(this.connection);
        this.register = 1;
    }

    private void subtract(double units, int register) {
        try {
            Thread.sleep(Double.valueOf(threadSeconds * 1000).intValue());
            //connection.prepareStatement("LOCK TABLES transaction WRITE").executeUpdate();
            preparedStatement = connection.prepareStatement("UPDATE transaction SET value = value - ? WHERE id = ?");
            preparedStatement.setDouble(1, units);
            preparedStatement.setDouble(2, register);
            preparedStatement.executeUpdate();
            //connection.prepareStatement("UNLOCK TABLES").executeUpdate();
            System.out.println("Transaction subtract registered with connection: "+this.connection);
        } catch (SQLException e) {
            System.out.println("Transaction subtract error");
        } catch (Exception ie) {
            System.out.println("Error trying to sleep thread");
        }
        System.out.println("[ACTUAL VALUE FROM SUBTRACT]:"+getter.getValueTransaction(1));
    }

    @Override
    public void run() {

        this.subtract(this.units, this.register);
    }

    public void setParameters(double units, double threadSeconds, double timerSeconds){
        this.units = units;
        this.threadSeconds = threadSeconds;
        this.timerSeconds = timerSeconds;
    }

}

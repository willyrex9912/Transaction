package org.rex.thread;

import org.rex.db.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class Operation extends Thread{

    private Connection connection;
    private PreparedStatement preparedStatement;
    private double units;
    private int register;
    private Getter getter;
    private int threadSeconds;
    private int timerSeconds;
    private double initialValue;
    private TypeOperation type;
    private volatile boolean running = true;
    public Operation(TypeOperation type, Connection connection){
        this.connection = connection;
        this.getter = new Getter(this.connection);
        this.register = 1;
        this.type = type;
    }

    private void setInitialValueInDataBase(){
        try {
            preparedStatement = connection.prepareStatement("UPDATE transaction SET value = ?");
            preparedStatement.setDouble(1, initialValue);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            System.out.println("ERROR SETTING INITIAL VALUE IN DB");
        }
    }

    private void increase() {
        try {
            //connection.prepareStatement("LOCK TABLES transaction WRITE").executeUpdate();
            try {
                Thread.sleep(this.timerSeconds);
                connection.setAutoCommit(false);
                preparedStatement = connection.prepareStatement("UPDATE transaction SET value = value + ? WHERE id = ?");
                preparedStatement.setDouble(1, units);
                preparedStatement.setDouble(2, register);
                preparedStatement.executeUpdate();
                System.out.println("[INFORMATION] Transaction increase registered with connection: "+this.connection);
                // TODO: More Actions
                //throw new SQLException();
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                try {
                    System.out.println("[ERROR] Transaction increase error");
                    //Thread.sleep(this.timerSeconds);
                    connection.rollback();
                    System.out.println("Rollback successfully");
                } catch (SQLException r) {
                    System.out.println("Error trying to do rollback");
                } catch (Exception ie) {
                    System.out.println("Error trying to sleep thread");
                }
            }  catch (Exception ie) {
                System.out.println("Error trying to sleep thread");
            }
            if(!connection.getAutoCommit()){
                connection.commit();
                connection.setAutoCommit(true);
            }
            //connection.prepareStatement("UNLOCK TABLES").executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error trying to lock or unlock tables");
        }
        System.out.println("[INFORMATION] Actual subtract value is "+getter.getValueTransaction(1));
    }

    private void subtract() {
        try {
            //Thread.sleep(this.timerSeconds);
            //connection.prepareStatement("LOCK TABLES transaction WRITE").executeUpdate();
            preparedStatement = connection.prepareStatement("UPDATE transaction SET value = value - ? WHERE id = ?");
            preparedStatement.setDouble(1, units);
            preparedStatement.setDouble(2, register);
            preparedStatement.executeUpdate();
            //connection.prepareStatement("UNLOCK TABLES").executeUpdate();
            System.out.println("[INFORMATION] Transaction subtract registered with connection: "+this.connection);
        } catch (SQLException e) {
            System.out.println("Transaction subtract error");
        } catch (Exception ie) {
            System.out.println("Error trying to sleep thread");
        }
        System.out.println("[INFORMATION] Actual subtract value is "+getter.getValueTransaction(1));
    }

    @Override
    public void run() {
        this.setInitialValueInDataBase();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                running = false;
            }
        }, this.threadSeconds);

        while (running) {
            if( this.type == TypeOperation.INCREASE ){
                this.increase();
            } else if( this.type == TypeOperation.SUBTRACT ){
                this.subtract();
            }
        }
        System.out.println("[SUCCESS] Thread finished");
    }

    public void setParameters(double units, double threadSeconds, double timerSeconds, double initialValue){
        this.units = units;
        this.threadSeconds = Double.valueOf(threadSeconds * 1000).intValue();
        this.timerSeconds = Double.valueOf(timerSeconds * 1000).intValue();
        this.initialValue = initialValue;
    }

}

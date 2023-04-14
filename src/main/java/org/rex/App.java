package org.rex;

import org.rex.db.DataBaseConnector;
import org.rex.thread.Operation;
import org.rex.thread.Subtractor;
import org.rex.thread.TypeOperation;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //Initializing connections and classes
        DataBaseConnector connector = new DataBaseConnector();
        DataBaseConnector anotherConnector = new DataBaseConnector();
        connector.initialize();
        anotherConnector.initialize();
        Operation increase = new Operation(TypeOperation.INCREASE, connector.getConnection());
        Operation subtract = new Operation(TypeOperation.SUBTRACT, anotherConnector.getConnection());

        //Setting data
        increase.setParameters(10,10,0.5,0);

        //Applying changes and showing information
        increase.start();

        //Closing database connection
        //connector.close();
    }
}

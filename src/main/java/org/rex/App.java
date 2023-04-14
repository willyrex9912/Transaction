package org.rex;

import org.rex.db.DataBaseConnector;
import org.rex.thread.Operation;
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
        boolean lockAndUnlockTables = false;
        Operation increase = new Operation(TypeOperation.INCREASE, connector.getConnection(), lockAndUnlockTables);
        Operation subtract = new Operation(TypeOperation.SUBTRACT, anotherConnector.getConnection(), lockAndUnlockTables);

        //Setting data
        increase.setParameters(10,24,2,0);
        subtract.setParameters(2,24,4);

        //Applying changes and showing information
        increase.start();
        subtract.start();

        //Closing database connection
        //connector.close();
    }
}

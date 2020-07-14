/**
 * The program provides a sample for Java Engine functions. It creates a matrix and squares the values greater than 5.
 * Copyright 2016-2017 The MathWorks, Inc.
 */

import com.mathworks.engine.MatlabEngine;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class EngineConsoleDemo {
    public static void main(String args[]) {
        try {
//         //Start MATLAB asynchronously
//            Future<MatlabEngine> eng = MatlabEngine.startMatlabAsync();
//
//         // Get engine instance from the future result
//            MatlabEngine ml = eng.get();

//            System.out.println("finding engines");
//            String[] engines = MatlabEngine.findMatlab();
//            for (String engine: engines) {
//                System.out.println(engine);
//            }

            System.out.println("connecting");
            MatlabEngine ml = MatlabEngine.connectMatlab();
            ml.eval("load('C:\\Users\\User\\Documents\\CS\\NEI6\\compact_model_for_testing.mat');");
            ml.eval("load('C:\\Users\\User\\Documents\\CS\\NEI6\\sample.mat');");
            ml.eval("ans = cens.predict(data1);");
            double pred = ml.getVariable("ans");
            System.out.println(pred);

            double[] data = {
                    0.        , 0.9075896 , 0.        , 0.        , 0.47017544,
                    0.36607143, 0.91666667, 0.        , 0.        , 0.        ,
                    1.        , 0.        , 0.08701351, 0.        , 0.        ,
                    0.        , 0.        , 0.        , 1.        , 0.        ,
                    0.        , 0.        , 0.        , 0.        , 0.        ,
                    0.        , 0.        , 0.        , 1.        , 0.        ,
                    0.        , 0.        , 1.        , 0.        , 0.        ,
                    1.        , 0.        };
            ml.putVariable("data", data);
            ml.eval("datapred = cens.predict(data);");
            double datapred = ml.getVariable("datapred");
            System.out.println(datapred);

//            double[] received = ml.getVariable("data");
//            for (double ind: received) {
//                System.out.println(ind);
//            }

//            double datapred = ml.feval("cens.predict", (Object)data);
//            System.out.println(datapred);
            /*
         * Find elements greater than 5
         * 1. Create input matrix
         * 2. Put variable matrix in the MATLAB base workspace
         * 3. Solve A(A>5) in MATLAB
         * 4. Return results and display
         * 5. Call the power function in MATLAB on the returned matrix
         * 6. Display results
         */
            double[][] input = new double[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    double num = Math.random() * 10;
                    input[i][j] = num;
                }
            }
            System.out.println("\nFind numbers from a matrix that are greater than five and square them:\n");
            System.out.println("Input matrix: ");
            for (int i = 0; i < 4; i++) {
                {
                    for (int j = 0; j < 4; j++) {
                        System.out.print(String.format("%.2f", input[i][j]) + "\t");
                    }
                    System.out.print("\n");
                }
            }

            // Put the matrix in the MATLAB workspace
            ml.putVariable("A", input);

            // Evaluate the command to search in MATLAB
            ml.eval("B=A(A>5);");

            // Get result from the workspace
            Future<double[]> futureEval = ml.getVariableAsync("B");
            double[] output = futureEval.get();

            // Display result
            System.out.println("\nElements greater than 5: ");
            for (int i = 0; i < output.length; i++) {
                System.out.print(" " + String.format("%.2f", output[i]));
            }

            // Square the returned elements using the power function in MATLAB
            double[] powResult = ml.feval("power", output, Double.valueOf(2));
            System.out.println("\n\nSquare of numbers greater than 5:");
            for (int i = 0; i < powResult.length; i++) {

                //Set precision for the output values
                System.out.print(" " + String.format("%.2f", powResult[i]));
            }
            System.out.println("\n");

            // Disconnect from the MATLAB session
//            ml.disconnect();
            ml.quit();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

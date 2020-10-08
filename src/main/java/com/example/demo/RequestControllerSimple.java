package com.example.demo;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class RequestControllerSimple {
    MatlabEngine ml;
    int ident;
    public RequestControllerSimple() {
        try {
            ident = 23457;
            ml = MatlabEngine.connectMatlab();
            ml.eval("load('C:\\Users\\User\\Documents\\CS\\NEI6\\compact_model_for_testing.mat');");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/predict")
    public int[] predict(@RequestParam double[] values) {
        int datapred = 0;
        try {
            ml.putVariable("data", values);
            ml.eval("datapred = cens.predict(data);");
            datapred = ((Double) ml.getVariable("datapred")).intValue();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new int[] {datapred, ident++};
    }
}

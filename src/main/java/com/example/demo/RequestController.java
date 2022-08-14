package com.example.demo;

import com.mathworks.engine.MatlabEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

@RestController
public class RequestController {
    MatlabEngine ml;
    @Value("#{systemEnvironment['REDCAP_URL']}")
    private String redcapURL;
    @Value("#{systemEnvironment['REDCAP_TOKEN']}")
    private String redcapToken;
    public RequestController() {
        try {
            ml = MatlabEngine.connectMatlab();
            String path = "model.mat";
            File f = new File(path);
            ml.eval(String.format("load('%s');", f.getCanonicalPath()));
        } catch (InterruptedException | ExecutionException | IOException e) {
            System.out.println("connect/load exception");
            e.printStackTrace();
        }
    }

    @CrossOrigin
    @GetMapping("/predict")
    public String[] predict(@RequestParam double[] values, @RequestParam String site) {
        int datapred = 2;
        try {
            double[] predictionData = new double[37];
            for (int i = 0; i < 10; i++) {
                predictionData[i] = values[i];
            }
            predictionData[10] = 0.3906; // private insurance not used
            predictionData[11] = 0.3420; // evening arrival not used
            predictionData[12] = values[10]; // age
            for (int i = 13; i < 18; i++) { // empty field indicators not used
                predictionData[i] = 0.0;
            }
            predictionData[18] = values[11]; // white
            predictionData[19] = values[14]; // black
            predictionData[20] = Math.min(1, values[12] + values[13] + values[15] + values[16]); // other race
            for (int i = 17; i < 25; i++) {
                predictionData[i + 4] = values[i]; // mechanism of injury
            }
            // intentionality not used
            predictionData[29] = 0.9151;
            predictionData[30] = 0.0105;
            predictionData[31] = 0.00008;
            predictionData[32] = 0.0346;
            predictionData[33] = 0.0344;
            for (int i = 25; i < 28; i++) {
                predictionData[i + 9] = values[i]; // transport distance
            }
            synchronized (RequestController.class) {
                ml.putVariable("data", predictionData);
                System.out.println("put data");
                ml.eval("datapred = cens.predict(data);");
                datapred = (((Double) ml.getVariable("datapred")).intValue() + 1) / 2;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String ident = generateIdentifier();
        REDCapRequest log = new REDCapRequest(values, ident, site, datapred, redcapURL, redcapToken);
        log.doPost();
        return new String[] {String.valueOf(datapred), ident};
    }

    private String generateIdentifier() {
        long epochTime = System.currentTimeMillis();
        return Long.toString(epochTime, 36).toUpperCase();
    }
}

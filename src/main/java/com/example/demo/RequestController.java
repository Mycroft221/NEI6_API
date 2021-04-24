package com.example.demo;

import com.mathworks.engine.MatlabEngine;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class RequestController {
    MatlabEngine ml;
    int ident;
    public RequestController() {
        try {
            ident = 1;
            ml = MatlabEngine.connectMatlab();
            String path = "model.mat";
            System.out.println(path);
            File f = new File(path);
            ml.eval(String.format("load('%s');", f.getCanonicalPath()));
        } catch (InterruptedException | ExecutionException | IOException e) {
            System.out.println("connect/load exception");
            e.printStackTrace();
        }
    }

    @CrossOrigin
    @GetMapping("/predict")
    public int[] predict(@RequestParam double[] values, @RequestParam String site) {
        int datapred = 0;
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
            ml.putVariable("data", predictionData);
            System.out.println("put data");
            ml.eval("datapred = cens.predict(data);");
            datapred = (((Double) ml.getVariable("datapred")).intValue() + 1) / 2;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        MyClass log = new MyClass(values, ident, site);
        log.doPost();
        return new int[] {datapred, ident++};
    }

//    private String[] questions =
//            {
//                    "Leaders are owners. They think long term and don't sacrifice long-term value for short-term results. They never say that's not my job.",
//                    "Leaders start with the customer and work backwards. They work vigorously to earn and keep customer trust. Although leaders pay attention to competitors, they obsess over customers.",
//                    "Leaders expect and require innovation and invention from their teams and always find ways to simplify. They are externally aware, and look for new ideas from everywhere.",
//                    "Leaders are right a lot. They have strong judgment and good instincts. They seek diverse perspectives and work to disconfirm their beliefs.",
//                    "Leaders are never done learning and always seek to improve themselves. They are curious about new possibilities and act to explore them.",
//                    "Leaders raise the performance bar with every hire and promotion. They recognize exceptional talent, and willingly move them throughout the organization.",
//                    "Leaders are continually raising the bar and drive their teams to deliver high quality products, services, and processes.",
//                    "Leaders create and communicate a bold direction that inspires results. They think differently and look around corners for ways to serve customers.",
//                    "Speed matters in business. Many decisions and actions are reversible and do not need extensive study. We value calculated risk taking.",
//                    "Accomplish more with less. Constraints breed resourcefulness, self-sufficiency, and invention.",
//                    "Leaders listen attentively, speak candidly, and treat others respectfully. They are vocally self-critical, even when doing so is awkward or embarrassing.",
//                    "Leaders operate at all levels, stay connected to the details, audit frequently, and are skeptical when metrics and anecdote differ. No task is beneath them.",
//                    "Leaders have conviction and are tenacious. They do not compromise for the sake of social cohesion. Once a decision is determined, they commit wholly.",
//                    "Leaders focus on the key inputs for their business and deliver them with the right quality and in a timely fashion. Despite setbacks, they rise to the occasion and never settle."
//            };
//
//    private String[] answers =
//            {
//                    "Ownership",
//                    "Customer Obsession",
//                    "Invent and Simplfy",
//                    "Are Right, A lot",
//                    "Learn and Be Curious",
//                    "Hire and Develop the Best",
//                    "Insist on the Highest Standards",
//                    "Think Big",
//                    "Bias for Action",
//                    "Frugality",
//                    "Earn Trust",
//                    "Dive Deep",
//                    "Disagree and Commit",
//                    "Deliver Results"
//            };
//
//    private Clock clock = Clock.systemDefaultZone();
//
//    @GetMapping("/question")
//    public Request getQuestion() {
//        int ind = ((int) (clock.millis() / 5000)) % 13;
//        Request res = new Request(questions[ind], answers[ind]);
//        return res;
//    }

}

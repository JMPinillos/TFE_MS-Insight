package com.msinsight;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.msinsight.app.write.MySqlApplicationIntake;

public class LambdaETL implements RequestHandler<MySqlApplicationIntake, String> {

    @Override
    public String handleRequest(MySqlApplicationIntake event, Context context) {
        try {
            MySqlApplicationIntake.main(null);
            context.getLogger().log("LambdaETL executed successfully");
            return "LambdaETL executed successfully";
        } catch (Exception e) {
            context.getLogger().log("Error executing LambdaETL with event data " + "Error: " + e.getMessage());
            throw new RuntimeException("Error executing LambdaETL with event data");
        }
    }
}
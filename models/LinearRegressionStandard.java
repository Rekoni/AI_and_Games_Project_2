package models;

import java.util.ArrayList;
import models.Regression;
import comp34120.ex2.Record;

public class LinearRegressionStandard implements Regression {	
    public double a, b;

    public LinearRegressionStandard(){}

    public void train(ArrayList<Record> records)
    {
        double sumOfLeader, sumOfFollower, squaredSumOfLeader, crossSum;

        sumOfFollower = 0.0;
        sumOfLeader = 0.0;
        crossSum = 0.0;
        squaredSumOfLeader = 0.0;
        for(int i = 0; i < records.size(); i++){
            sumOfFollower += records.get(i).m_followerPrice;
            squaredSumOfLeader = Math.pow(records.get(i).m_leaderPrice, 2);
            sumOfLeader += records.get(i).m_leaderPrice;
            crossSum += records.get(i).m_followerPrice * records.get(i).m_leaderPrice;
        }

        double a_sum = squaredSumOfLeader * sumOfFollower -
                                    sumOfLeader * crossSum;
        double numerator = records.size() * squaredSumOfLeader - Math.pow(sumOfLeader, 2);
        double b_sum = records.size() * crossSum - sumOfLeader * sumOfFollower;

        a = a_sum/numerator;
        b = b_sum/numerator;

    }

    public double predict(double x) throws Exception {
        double prediction;
        
        try {
            prediction = a * x + b;
        }
        catch (Exception e) {
            throw e;
        }

        return prediction;
    }
}
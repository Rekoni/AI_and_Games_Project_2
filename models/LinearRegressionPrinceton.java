package models;

import java.util.ArrayList;
import models.Regression;
import comp34120.ex2.Record;

public class LinearRegressionPrinceton implements Regression {
    public double a, b;

    public LinearRegressionPrinceton(){}

    public void train(ArrayList<Record> records)
	{
        int noOfEntries = records.size();


        double sumx = 0.0, sumy = 0.0;

        for (int i = 0; i < noOfEntries; i++)
		{
            sumx  += records.get(i).m_leaderPrice;
            sumy  += records.get(i).m_followerPrice;
		}
        double xAverage = sumx / noOfEntries;
        double yAverage = sumy / noOfEntries;


        double xSqauredAverage = 0.0;
		    double xyCrossAveraged = 0.0;

        for (int i = 0; i < noOfEntries; i++) {
            xSqauredAverage += Math.pow(records.get(i).m_leaderPrice - xAverage,2);

            xyCrossAveraged += (records.get(i).m_leaderPrice - xAverage) * (records.get(i).m_followerPrice - yAverage);
        }

        a  = xyCrossAveraged / xSqauredAverage;
        b = yAverage - a * xAverage;

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

    public double maxLinear() {
        return (0.3 * a - 0.3 * b - 3) / (0.6 * a - 2);
    }

    public String getCoefficientsString()
    {
        return "a = " + a + "; b = " + b;
    }
}

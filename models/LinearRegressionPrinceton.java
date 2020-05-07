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

        // first pass
        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;

        for (int i = 0; i < noOfEntries; i++)
		{
            sumx  += records.get(i).m_leaderPrice;
            sumy  += records.get(i).m_followerPrice;
		}
        double xAverage = sumx / noOfEntries;
        double yAverage = sumy / noOfEntries;

        // second pass: compute summary statistics
        double xSqauredAverage = 0.0;//, yyAverage = 0.0,
		double xyCrossAveraged = 0.0;

        for (int i = 0; i < noOfEntries; i++) {
            xSqauredAverage += (records.get(i).m_leaderPrice - xAverage) * (records.get(i).m_leaderPrice - xAverage);
          //  yyAverage += (records.get(i).m_followerPrice - yAverage) * (records.get(i).m_followerPrice - yAverage);
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
}
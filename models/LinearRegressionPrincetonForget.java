package models;

import java.util.ArrayList;
import models.Regression;
import comp34120.ex2.Record;

public class LinearRegressionPrincetonForget implements Regression {
    public double a, b;
    public double forgettingFactor;

    public LinearRegressionPrincetonForget(double forgettingFactor){
      this.forgettingFactor = forgettingFactor;
    }

    public void train(ArrayList<Record> records)
	{
        int noOfEntries = records.size();

        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;

        for (int i = 0; i < noOfEntries; i++)
		{
            //double weight_after_forget = Math.pow(forgettingFactor, noOfEntries - i);
            double weight_after_forget = 1;
            sumx  += records.get(i).m_leaderPrice * weight_after_forget;
            sumy  += records.get(i).m_followerPrice * weight_after_forget;
		}
        double xAverage = sumx / noOfEntries;
        double yAverage = sumy / noOfEntries;


        double xSquaredAverage = 0.0;
  		double xyCrossAveraged = 0.0;

        for (int i = 0; i < noOfEntries; i++) {
            double weight_after_forget = Math.pow(forgettingFactor, noOfEntries - i);
            //double weight_after_forget = 1;
            xSquaredAverage += Math.pow(records.get(i).m_leaderPrice - xAverage, 2) * weight_after_forget;
            xyCrossAveraged += (records.get(i).m_leaderPrice - xAverage) * (records.get(i).m_followerPrice - yAverage) * weight_after_forget;
        }

        a  = xyCrossAveraged / xSquaredAverage;
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

    public double maximize() {
        if (a < 1.0/3.0)
            return (0.3 * a - 0.3 * b - 3) / (0.6 * a - 2);
        else if (a > 1.0/3.0 || b > -2.0/6) 
            return Integer.MAX_VALUE;
        else 
            return 1;
    }

    public String getCoefficientsString()
    {
        return "a = " + a + "; b = " + b;
    }
}

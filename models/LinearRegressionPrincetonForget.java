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

        double weight_after_forget;

        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;

        for (int i = 0; i < noOfEntries; i++)
		{
            weight_after_forget = Math.pow(FORGETTING_FACTOR, index);
            sumx  += records.get(i).m_leaderPrice * weight_after_forget;
            sumy  += records.get(i).m_followerPrice * weight_after_forget;
		}
        double xAverage = sumx / noOfEntries;
        double yAverage = sumy / noOfEntries;


      double xSqauredAverage = 0.0;
  		double xyCrossAveraged = 0.0;

        for (int i = 0; i < noOfEntries; i++) {
            xSqauredAverage += Math.pow(records.get(i).m_leaderPrice - xAverage, 2) * weight_after_forget;
            xyCrossAveraged += (records.get(i).m_leaderPrice - xAverage) * (records.get(i).m_followerPrice - yAverage) * weight_after_forget;
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

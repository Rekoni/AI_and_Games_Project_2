package models;

import java.util.ArrayList;
import java.util.List;
import models.Regression;
import comp34120.ex2.Record;
import org.ejml.simple.SimpleMatrix;

public class PolynomialRegression implements Regression {  
    // Quadratic Polynomial Regression
    public double a, b, c;

    public double forgettingFactor;

    public PolynomialRegression(double forgettingFactor){
      this.forgettingFactor = forgettingFactor;
    }

    public void train(ArrayList<Record> records)
    {
        SimpleMatrix m1 = new SimpleMatrix(3, 1);
        SimpleMatrix m2 = new SimpleMatrix(3, 3);

        // Window for limiting the number of records used for training
        int window_size = records.size();
        List<Record> window = records.subList(records.size() - window_size, records.size());

        double[] x = {0.0, 0.0, 0.0, 0.0};

        for(int i = 0; i < window.size(); i++)
        {
            double weight = Math.pow(forgettingFactor, window.size() - i);
            double pl = window.get(i).m_leaderPrice;
            double pf = window.get(i).m_followerPrice;

            for(int j = 0; j < 4; j++)
                x[j] += Math.pow(pl, j+1) * weight;

            m1.set(0, 0, m1.get(0, 0) + pf * weight);
            m1.set(1, 0, m1.get(1, 0) + pl * pf * weight);
            m1.set(2, 0, m1.get(2, 0) + pl * pl * pf * weight);
        }

        m2.set(0, 0, window.size());
        for(int i = 0; i < 3; i++)
        {
            if(i > 0)
                m2.set(0, i, x[i-1]);

            m2.set(1, i, x[i]);
            m2.set(2, i, x[i+1]);
        }

        SimpleMatrix result = m2.invert();
        result = result.mult(m1);

        a = result.get(2, 0);
        b = result.get(1, 0);
        c = result.get(0, 0);
    }

    public double predict(double x) throws Exception {
        double prediction;
        
        try {
            prediction = a * x * x + b * x + c;
        }
        catch (Exception e) {
            throw e;
        }

        return prediction;
    }

    public double getProfit(double x) {
        double profit;
        
        // Profit function coefficients
        double c1 = 0.3 * a;
        double c2 = 0.3 * b - 0.3 * a - 1;
        double c3 = 3 + 0.3 * c - 0.3 * b;
        double c4 = - 2 - 0.3 * c;

        try {
            profit = c1 * x * x * x + c2 * x * x + c3 * x + c4;
        }
        catch (Exception e) {
            throw e;
        }

        return profit;
    }

    public double maximize() {

        // Profit function coefficients
        double c1 = 0.3 * a;
        double c2 = 0.3 * b - 0.3 * a - 1;
        double c3 = 3 + 0.3 * c - 0.3 * b;
        double c4 = - 2 - 0.3 * c;

        // Calculating roots of the first derivative of the profit function
        
        double discriminant = c2 * c2 - 3 * c1 * c3;
        if (discriminant > 0) {
            double x1 = (-c2 - Math.sqrt(c2 * c2 - 3 * c1 * c3)) / (3 * c1);
            double x2 = (-c2 + Math.sqrt(c2 * c2 - 3 * c1 * c3)) / (3 * c1);

            // Order the roots
            if (x1 > x2) {
                double swap = x1;
                x1 = x2;
                x2 = swap;
            }

            // Applying the second derivative of the profit function on the roots
            double sd_x1 = 6 * c1 * x1 + 2 * c2;
            double sd_x2 = 6 * c1 * x2 + 2 * c2;

            // The profit function graph should have one maximum and one minimum
            if (sd_x1 < 0 && sd_x2 > 0)
                // The minimum occurs last, which means that 
                // the profit approaches infinity when the strategy approaches infinity
                return (double) Integer.MAX_VALUE;
            else if (sd_x1 > 0 && sd_x2 < 0) {
                // The maximum occurs last
                if (getProfit(1) > getProfit(x2))
                    // As the function decreases towards the minimum,
                    // there is an interval where the profit is higher 
                    // than the local maximum at x2
                    return 1;
                else
                    return Math.max(1, x2);
            }
            else 
                return 1;

        }
        else if (discriminant == 0) {
            // The two roots are equal
            double x = -c2 / (3 * c1);

            // Applying the second derivative of the profit function on the root
            double sd_x = 6 * c1 * x + 2 * c2;

            // Determine if the nature of the extrema point
            if (sd_x > 0)
                // Point of minimum, 
                // the profit approaches infinity when the strategy approaches infinity
                return (double) Integer.MAX_VALUE;
            else if (sd_x < 0) {
                // Point of maximum
                // if the maximum is not within the strategy space,
                // the profit only decreases with the strategy
                return Math.max(1, x);
            }
            else
                return 1;

        }
        else {
            // No roots of the first derivative, meaning no extrema points in the profit function
            // Meaning the function is monotone

            double primary_coefficient;
            if (c1 != 0)
                primary_coefficient = c1;
            else if (c2 != 0)
                primary_coefficient = c2;
            else if (c3 != 0)
                primary_coefficient = c3;
            else 
                // The function is constant
                return 1;

            if (primary_coefficient > 0)
                // Profit is ascending
                return (double) Integer.MAX_VALUE;
            else
                // Profit is descending
                return 1;
        }

    }

    public String getCoefficientsString()
    {
        return "a = " + a + "; b = " + b + "; c = " + c;
    }
}
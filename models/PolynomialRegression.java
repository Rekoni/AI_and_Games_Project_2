package models;

import java.util.ArrayList;
import java.util.List;
import models.Regression;
import comp34120.ex2.Record;
import org.ejml.simple.SimpleMatrix;

public class PolynomialRegression implements Regression {  
    public double a, b, c;

    public double forgettingFactor;

    public PolynomialRegression(double forgettingFactor){
      this.forgettingFactor = forgettingFactor;
    }

    public void train(ArrayList<Record> records)
    {
        SimpleMatrix m1 = new SimpleMatrix(3, 1);
        SimpleMatrix m2 = new SimpleMatrix(3, 3);

        int window_size = Math.min(50, records.size());
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
        return 0;
    }

    public double maximize() {
        
        double fp = -0.3 * a + 0.3 * b - 1;
        double fr = 3 - 0.3 * (b + c);

        double fq = 0.3 * a;
        double x = (-fp + Math.sqrt(Math.abs(fp * fp - 3 * fq * fr))) / (3 * fq);
        if((6  * fq * x + 2 * fp) < 0)
            return x;
        else
            return (x * -(3 * fq) - 2*fp) / (3 * fq);
    }

    public String getCoefficientsString()
    {
        return "a = " + a + "; b = " + b + "; c = " + c;
    }
}
package models;

import java.util.ArrayList;
import java.util.List;
import models.Regression;
import comp34120.ex2.Record;
import Jama.Matrix;
import Jama.QRDecomposition;

public class PolynomialRegression implements Regression {  
    // Quadratic Polynomial Regression
    public double a, b, c;

    public double forgettingFactor;
    public int windowSize;

    public PolynomialRegression(int windowSize, double forgettingFactor){
      this.windowSize = windowSize;
      this.forgettingFactor = forgettingFactor;
    }

    public void train(ArrayList<Record> records)
    {
        List<Record> window = records.subList(records.size() - windowSize, records.size());

        int n = window.size();

        double[] x = new double[n];
        double[] y = new double[n];
        for(int i = 0; i < n; i++)
        {
            double weight = Math.pow(forgettingFactor, n - i);
            x[i] = window.get(i).m_leaderPrice;
            y[i] = window.get(i).m_followerPrice * weight;
        }
        

        QRDecomposition qrdec = null;
        Matrix mX = null;

        int deg = 2;
        // Reduce degree until vandermode has a full rank
        while (true) {
            double[][] vandermonde = new double[n][deg+1];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= deg; j++) {
                    double weight = Math.pow(forgettingFactor, n - i);
                    vandermonde[i][j] = Math.pow(x[i], j) * weight;
                }
            }
            mX = new Matrix(vandermonde);
            qrdec = new QRDecomposition(mX);
            if (qrdec.isFullRank()) 
                break;
            else
                deg--;
        }
        Matrix mY = new Matrix(y, n);

        Matrix beta = qrdec.solve(mY);
        a = beta.get(2, 0);
        b = beta.get(1, 0);
        c = beta.get(0, 0);
    }

    public double predict(double x) {
        return a * x * x + b * x + c;
    }

    public double maximize() {
        double fr = 3 - 0.3 * b + 0.3 * c;
        double fp = -0.3 * a + 0.3 * b - 1;

        double fq = 0.3 * a;
        double x = (-fp + Math.sqrt(Math.abs(fp * fp - 3 * fq * fr))) / (3 * fq);

        if((6  * fq * x + 2 * fp) < 0)
            return x;
        return (-(x * (3 * fq)) - 2*fp) / (3 * fq);
    }

    public String getCoefficientsString()
    {
        return "a = " + a + "; b = " + b + "; c = " + c;
    }
}
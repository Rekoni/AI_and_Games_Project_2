package models;

import java.util.ArrayList;
import comp34120.ex2.Record;

public interface Regression {
    public void train(ArrayList<Record> records);
    public double predict(double x) throws Exception;
    public double maxLinear();
}
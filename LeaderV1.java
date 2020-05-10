import comp34120.ex2.PlayerImpl;
import comp34120.ex2.PlayerType;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.List;
import comp34120.ex2.Record;
import models.*;

final class LeaderV1 extends PlayerImpl {
	/* The randomizer used to generate random price */
	private final Random m_randomizer = new Random(System.currentTimeMillis());
	private int lastDay;
    private ArrayList<Record> records;
	private Regression regressionModel;

	private LeaderV1()
		throws RemoteException, NotBoundException
	{
		super(PlayerType.LEADER, "Leader v1");

		//regressionModel = new LinearRegressionPrincetonForget(0.95);
		//regressionModel = new LinearRegressionPrinceton();

		// PolynomialRegression(window_size, forgetting_factor)
		regressionModel = new PolynomialRegression(100, 0.98);
	}

	@Override
	public void goodbye()
		throws RemoteException
	{
		m_platformStub.log(m_type, "Goodbye!");
		ExitTask.exit(500);
	}

	/**
	 * To inform this instance to proceed to a new simulation day
	 * @param p_date The date of the new day
	 * @throws RemoteException
	 */
	@Override
	public void proceedNewDay(int p_date)
		throws RemoteException
	{
		m_platformStub.log(m_type, "A New Day!");

		for (lastDay = lastDay + 1; lastDay < p_date; lastDay++) {
			records.add(m_platformStub.query(m_type, lastDay));
		}
		lastDay--;

		regressionModel.train(records);
		try {
			m_platformStub.publishPrice(m_type, genPrice());
		}
		catch (Exception e) {
			m_platformStub.log(m_type, e.getMessage());
		}
	}

	/**
	 * Generate a price based on a given regression model
	 * @return The generated price
	 */
	private float genPrice()
	throws RemoteException
	{
		m_platformStub.log(m_type, "Generating Price. ");
		double price = regressionModel.maximize();
		double predictedPrice;

		try {
			predictedPrice = regressionModel.predict(price);
			m_platformStub.log(m_type, "Predicted Follower Price: " + predictedPrice);
			System.out.println(records.size() + " days coefficients: " + regressionModel.getCoefficientsString());
		}
		catch (Exception e) {
			m_platformStub.log(m_type, e.getMessage());
			ExitTask.exit(500);
		}

		return (float) price;
	}

	public static void main(final String[] p_args)
		throws RemoteException, NotBoundException
	{
		new LeaderV1();
	}

	@Override
    public void startSimulation(int steps) throws RemoteException {

        m_platformStub.log(m_type, "Using model " + " poly");

		records = new ArrayList<Record>();
		lastDay = 0;

    }

	@Override
    public void endSimulation() throws RemoteException {

        m_platformStub.log(m_type, records.size() + " records before the last day.");

    }

	private static class ExitTask
		extends TimerTask
	{
		static void exit(final long p_delay)
		{
			(new Timer()).schedule(new ExitTask(), p_delay);
		}

		@Override
		public void run()
		{
			System.exit(0);
		}
	}
}

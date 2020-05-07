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

final class LeaderV1 extends PlayerImpl {
	/* The randomizer used to generate random price */
	private final Random m_randomizer = new Random(System.currentTimeMillis());
	private int lastDay;
    private List<Record> records;

	private LeaderV1()
		throws RemoteException, NotBoundException
	{
		super(PlayerType.LEADER, "Leader v1");
	}

	@Override
	public void goodbye()
		throws RemoteException
	{
		m_platformStub.log(m_type, "Goodbye!");
		ExitTask.exit(500);
	}

	// Calculates the profit of the leader given leader's and follower's prices
	private double profit_leader(double pl, double pf)
	{
		return demand(pl, pf) * (pl - 1);
	}

	// Calculates the demand given leader's and follower's prices
	private double demand(double pl, double pf) 
	{
		return 2 - pl + 0.3 * pf;
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

		int day;
		Record currentRecord = null;

		for (day = lastDay + 1; day < p_date; day++) {
			currentRecord = m_platformStub.query(m_type, day);
			records.add(currentRecord);
		}

		try {
			m_platformStub.publishPrice(m_type, genPrice(1.8f, 0.05f));
		}
		catch (Exception e) {
			m_platformStub.log(m_type, e.getMessage());
		}
	}

	/**
	 * Generate a random price based Gaussian distribution. The mean is p_mean,
	 * and the diversity is p_diversity
	 * @param p_mean The mean of the Gaussian distribution
	 * @param p_diversity The diversity of the Gaussian distribution
	 * @return The generated price
	 */
	private float genPrice(final float p_mean, final float p_diversity)
	throws RemoteException
	{
		m_platformStub.log(m_type, "Generating Price.");
		return (float) (p_mean + m_randomizer.nextGaussian() * p_diversity);
	}

	public static void main(final String[] p_args)
		throws RemoteException, NotBoundException
	{
		new LeaderV1();
	}

	@Override
    public void startSimulation(int steps) throws RemoteException {

        m_platformStub.log(m_type, "Using model " + this.getClass().getSimpleName());

		records = new ArrayList<Record>();
		lastDay = 0;

    }

	@Override
    public void endSimulation() throws RemoteException {

        m_platformStub.log(m_type, records.size() + " records before the last day.");

    }

	/**
	 * The task used to automatically exit the leader process
	 * @author Xin
	 */
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

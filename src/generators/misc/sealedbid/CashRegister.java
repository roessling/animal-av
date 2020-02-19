package generators.misc.sealedbid;

/**
 * @author Sebastian Ritzenhofen, Felix Rauterberg
 * 
 *         This class represents the shared cash register in which players pay
 *         in or receive money from.
 */
public class CashRegister {

	/**
	 * Current money stock in the shared register.
	 */
	private static double registerBalance = 0;

	/**
	 * @return current balance of cash register
	 */
	public static double getBalance() {
		return registerBalance;
	}

	/**
	 * Pay the specified amount of money into the shared cash register to get the
	 * player exactly on fair share.
	 * 
	 * @param amount
	 *            amount of money to be paid into the shared cash register
	 */
	public static void pay(double amount) {
		registerBalance += amount;
	}

	/**
	 * Withdraw the specified amount of money from the shared cash register to get
	 * the player exactly on his fair share.
	 * 
	 * @param amount
	 *            amount of money to be withdrawn from the shared cash register
	 */
	public static void withdraw(double amount) {
		registerBalance -= amount;
	}
}

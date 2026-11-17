package backend;

public class Purchase extends Transaction {
	
	String source; // WHOEVER GAVE 
	
	public Purchase(String trans_id, int amount, double price, String date) {
		super(trans_id, amount, price, date);
		String source;
	}
	
	@Override
	public boolean validateTransaction() {
		if (price >= 0 || amount > 0) {
			return true;
		}
		return false;
	}
}

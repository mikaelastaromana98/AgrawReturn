package backend;

public class Sale extends Transaction {
	
	Product product;
	
	public Sale(String trans_id, int amount, double price, String date) {
		super(trans_id, amount, price, date);
		Product product;
	}
	
	@Override
	public boolean validateTransaction() {
		if (price > 0 || amount > 0) {
			return true;
		}
		return false;
	}
	
	

}

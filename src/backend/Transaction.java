package backend;

public abstract class Transaction {
	
	String trans_id;
	int amount;
	double price; 
	String date;
	
	public Transaction (String trans_id, int amount, double price, String date) {
		this.trans_id = trans_id;
		this.amount = amount;
		this.price = price;
		this.date = date;
	}
	
	public String getId() {
		return this.trans_id;
	}
	
	public int amount() {
		return this.amount;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public void setId(String trans_id) {
		this.trans_id = trans_id;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setPrice(double price) {
		this.price = price;	
	}
	
	public void setData(String date) {
		this.date = date;
	}
	
	
	public abstract boolean validateTransaction(); // TODO: CHECK IF THE PRODUCT IS PART OF OUR PRODUCT ROSER

}

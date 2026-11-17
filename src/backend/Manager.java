package backend;


public class Manager extends User {
	
	// can add products to inventory
	// restock
	// 
	
	private Inventory inventory;
	
	public void addProduct(Product product, int initialQuantity) {
		this.inventory.addProduct(product, initialQuantity); 
		// TODO: INVOLVE A PRODUCT MANAGER CLASS SO THAT THE MANAGER USER CAN INTRODUCE NEW TYPES OF PRODUCTS
	}
	
	public boolean restockProduct(String productId, int amt) {
		return this.inventory.restockProduct(productId, amt);
		
	}
	
	// TODO: MONITOR SALE HISTORY
	// TODO: ACCESS TRANSACTION HISTORY
	
	

}

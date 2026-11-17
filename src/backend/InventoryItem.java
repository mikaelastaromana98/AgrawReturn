package backend;

import java.io.Serializable;

// wrapper for product class
public class InventoryItem implements Serializable 
{
  private static final long serialVersionUID = 1L;

  private Product product;
  private int quantity;

  public InventoryItem(Product product, int initialQuantity) 
  {
	this.product = product;
	this.quantity = initialQuantity;
  }

  public Product getProduct() 
  { 
    return product; 
  }

  public void setProduct(Product product) 
  { 
    this.product = product; 
  }

  public int getQuantity() 
  { 
    return quantity; 
  }

  public void setQuantity(int quantity) 
  { 
    this.quantity = quantity; 
  }

  public void addStock(int amount) 
  {
    if (amount > 0)
      this.quantity += amount;
  }

  public boolean reduceStock(int amount) {
    if (amount > 0 && this.quantity >= amount) 
    {
        this.quantity -= amount;
        return true;
    }
    return false;
  }
}

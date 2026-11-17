package backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// inventory management system
public class Inventory implements Serializable 
{
  // for saving into files in the future
  private static final long serialVersionUID = 3L;

  private List<InventoryItem> items;

  public Inventory() 
  {
    this.items = new ArrayList<>();
  }

  public List<InventoryItem> getItems() 
  {
    return items;
  }

  public void addProduct(Product product, int initialQuantity) 
  {
    Optional<InventoryItem> existingItem = findItemById(product.getId());

    if (existingItem.isPresent())
        existingItem.get().addStock(initialQuantity);
    else 
      items.add(new InventoryItem(product, initialQuantity));
  }

  public boolean removeProduct(String productId) 
  {
    return items.removeIf(item -> item.getProduct().getId().equalsIgnoreCase(productId));
  }

  public Optional<InventoryItem> findItemById(String productId) 
  {
    return items.stream().filter(item -> item.getProduct().getId().equalsIgnoreCase(productId)).findFirst();
  }

  public boolean restockProduct(String productId, int amount) 
  {
    Optional<InventoryItem> itemOpt = findItemById(productId);

    if (itemOpt.isPresent()) 
    {
      itemOpt.get().addStock(amount);
      return true;
    }
    return false;
  }

  public boolean updateStockQuantity(String productId, int newQuantity) 
  {
    if (newQuantity < 0) 
      return false;

    Optional<InventoryItem> itemOpt = findItemById(productId);

    if (itemOpt.isPresent()) 
    {
      itemOpt.get().setQuantity(newQuantity);
      return true;
    }
    return false;
  }
}

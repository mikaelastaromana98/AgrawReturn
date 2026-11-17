package backend;

import java.util.ArrayList;
import java.util.List;

public abstract class TransactionManager {
  private static boolean isLoaded = false;
  private static List<Transaction> transactionHistory = new ArrayList<>();

  public static void Load() 
  {
    if (isLoaded) return;
    isLoaded = true;
  }

  public static Transaction[] getHistory() 
  {
    return transactionHistory.toArray(new Transaction[0]); 
  }
  
  public static List<Transaction> getHistoryList() 
  {
    return transactionHistory;
  }

  public static void processTransaction(Transaction transaction) 
  {
    if (!transaction.validateTransaction()) return;
    transactionHistory.add(transaction);
  }

  public static double calculateProfit() 
  {
    double profit = 0.0;
    for (Transaction transaction : transactionHistory)
    {
      if (transaction instanceof Sale)
        profit += transaction.getPrice();
      else if (transaction instanceof Purchase)
        profit -= transaction.getPrice();
    }
    return profit;
  }
}

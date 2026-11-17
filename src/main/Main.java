package main;

import backend.Inventory;
import backend.Product;
import backend.TransactionManager;
import frontend.InventoryManagementView;
import frontend.LoginView;
import frontend.TransactionView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application 
{

  private Stage primaryStage;
  private Inventory inventory;

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;

    // Initialize shared backend resources
    TransactionManager.Load();
    inventory = new Inventory();
    inventory.addProduct(new Product("PROD-001", "Charcoal", "bag", 450.00), 100);
    inventory.addProduct(new Product("PROD-002", "Seeds", "1kg high-yield seed pack", 280.00), 50);

    showLoginScreen();
    primaryStage.setTitle("Store Management System");
    primaryStage.show();
  }

  private void showLoginScreen() 
  {
    LoginView loginView = new LoginView(
        this::showInventoryView,  // Manager login action
        this::showTransactionView // Clerk login action
    );
    Scene scene = new Scene(loginView, 400, 350);
    primaryStage.setScene(scene);
  }

  private void showInventoryView() 
  {
    InventoryManagementView inventoryView = new InventoryManagementView(inventory);
    Scene scene = new Scene(inventoryView, 900, 500);
    primaryStage.setScene(scene);
  }

  private void showTransactionView() 
  {
    TransactionView transactionView = new TransactionView(this::showLoginScreen);
    Scene scene = new Scene(transactionView, 900, 500);
    primaryStage.setScene(scene);
  }

  public static void main(String[] args) 
  {
    launch(args);
  }
}

package frontend;

import backend.Purchase;
import backend.Sale;
import backend.Transaction;
import backend.TransactionManager;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TransactionView extends BorderPane 
{

  private TableView<Transaction> table;
  private ObservableList<Transaction> tableData;

  private TextField idField;
  private ComboBox<String> typeBox;
  private TextField amountField;
  private TextField priceField;
  private TextField dateField;
  private Label profitLabel;

  public TransactionView(Runnable onLogout) 
  {
    setPadding(new Insets(15));
    
    // Header with Logout
    HBox topBar = new HBox(10);
    Label headerLabel = new Label("Transaction Management (Clerk)");
    headerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    Button btnLogout = new Button("Logout");
    btnLogout.setOnAction(e -> onLogout.run());
    topBar.getChildren().addAll(headerLabel, spacer, btnLogout);
    topBar.setPadding(new Insets(0, 0, 10, 0));
    setTop(topBar);

    tableData = FXCollections.observableArrayList(TransactionManager.getHistoryList());

    setCenter(createTableSection());
    setRight(createFormSection());
  }

  private TableView<Transaction> createTableSection() 
  {
    table = new TableView<>();
    table.setItems(tableData);

    TableColumn<Transaction, String> idCol = new TableColumn<>("Transaction ID");
    idCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getId()));

    TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
    typeCol.setCellValueFactory(cell -> new SimpleStringProperty(
      cell.getValue() instanceof Sale ? "Sale" : "Purchase"
    ));

    TableColumn<Transaction, Integer> amountCol = new TableColumn<>("Amount");
    amountCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().amount()).asObject());

    TableColumn<Transaction, Double> priceCol = new TableColumn<>("Price (₱)");
    priceCol.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getPrice()).asObject());

    TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
    dateCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate()));

    table.getColumns().addAll(idCol, typeCol, amountCol, priceCol, dateCol);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    return table;
  }

  private VBox createFormSection() 
  {
    VBox form = new VBox(10);
    form.setPadding(new Insets(0, 0, 0, 15));
    form.setPrefWidth(280);
    
    Label formTitle = new Label("Record New Transaction");
    formTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

    idField = new TextField();
    idField.setPromptText("e.g., TXN-001");

    typeBox = new ComboBox<>();
    typeBox.getItems().addAll("Sale", "Purchase");
    typeBox.setValue("Sale");
    typeBox.setMaxWidth(Double.MAX_VALUE);

    amountField = new TextField();
    amountField.setPromptText("e.g., 5");

    priceField = new TextField();
    priceField.setPromptText("e.g., 1500.00");

    dateField = new TextField();
    dateField.setPromptText("e.g., 2026-03-30");

    Button btnProcess = new Button("Process Transaction");
    btnProcess.setMaxWidth(Double.MAX_VALUE);
    btnProcess.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
    btnProcess.setOnAction(e -> handleProcessTransaction());

    Button btnCalcProfit = new Button("Calculate Total Profit");
    btnCalcProfit.setMaxWidth(Double.MAX_VALUE);
    btnCalcProfit.setOnAction(e -> handleCalculateProfit());

    profitLabel = new Label("Net Profit: ₱0.00");
    profitLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #2c3e50;");

    form.getChildren().addAll(
      formTitle,
      new Label("Transaction ID:"), idField,
      new Label("Transaction Type:"), typeBox,
      new Label("Amount / Quantity:"), amountField,
      new Label("Total Price:"), priceField,
      new Label("Date:"), dateField,
      new Separator(),
      btnProcess,
      btnCalcProfit,
      profitLabel
    );

    return form;
  }

  private void handleProcessTransaction() 
  {
    try {
      String id = idField.getText().trim();
      String type = typeBox.getValue();
      int amount = Integer.parseInt(amountField.getText().trim());
      double price = Double.parseDouble(priceField.getText().trim());
      String date = dateField.getText().trim();

      if (id.isEmpty() || date.isEmpty()) 
      {
        showAlert(Alert.AlertType.ERROR, "Validation Error", "ID and Date fields cannot be empty.");
        return;
      }

      Transaction txn;
      if ("Sale".equalsIgnoreCase(type)) {
          txn = new Sale(id, amount, price, date);
      } else {
          txn = new Purchase(id, amount, price, date);
      }

      TransactionManager.processTransaction(txn);
      refreshTable();
      clearForm();
      handleCalculateProfit();
      showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction recorded successfully!");
    } catch (NumberFormatException ex) {
        showAlert(Alert.AlertType.ERROR, "Input Error", "Amount and Price must be valid numbers.");
    }
  }

  private void handleCalculateProfit() 
  {
    double profit = TransactionManager.calculateProfit();
    profitLabel.setText(String.format("Net Profit: ₱%.2f", profit));
  }

  private void refreshTable() 
  {
    tableData.setAll(TransactionManager.getHistoryList());
    table.refresh();
  }

  private void clearForm() 
  {
    idField.clear();
    amountField.clear();
    priceField.clear();
    dateField.clear();
  }

  private void showAlert(Alert.AlertType type, String title, String content) 
  {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}

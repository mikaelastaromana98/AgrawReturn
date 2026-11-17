package frontend;

import backend.Inventory;
import backend.InventoryItem;
import backend.Product;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/*
 * v e r y  rough na hahahaha ui
 */

public class InventoryManagementView extends BorderPane 
{
  // inventory stuff
  private Inventory inventory;
  private TableView<InventoryItem> table;
  private ObservableList<InventoryItem> tableData;

  // inputs and labels
  private TextField idField;
  private TextField nameField;
  private TextField descField;
  private TextField priceField;
  private TextField quantityField;

  // constructor
  public InventoryManagementView(Inventory inventory) 
  {
    this.inventory = inventory;
    this.tableData = FXCollections.observableArrayList(inventory.getItems());

    setPadding(new Insets(15));
    setCenter(createTableSection());
    setRight(createFormSection());
  }

  private TableView<InventoryItem> createTableSection() 
  {
    table = new TableView<>();
    table.setItems(tableData);

    // formatting stuff
    TableColumn<InventoryItem, String> idCol = new TableColumn<>("Product ID");
    idCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduct().getId()));

    TableColumn<InventoryItem, String> nameCol = new TableColumn<>("Name");
    nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduct().getName()));

    TableColumn<InventoryItem, String> descCol = new TableColumn<>("Description");
    descCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduct().getDescription()));

    TableColumn<InventoryItem, Double> priceCol = new TableColumn<>("Price (₱)");
    priceCol.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getProduct().getPrice()).asObject());

    TableColumn<InventoryItem, Integer> qtyCol = new TableColumn<>("Stock Quantity");
    qtyCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getQuantity()).asObject());

    table.getColumns().addAll(idCol, nameCol, descCol, priceCol, qtyCol);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
      if (newSel != null) 
      {
        idField.setText(newSel.getProduct().getId());
        nameField.setText(newSel.getProduct().getName());
        descField.setText(newSel.getProduct().getDescription());
        priceField.setText(String.valueOf(newSel.getProduct().getPrice()));
        quantityField.setText(String.valueOf(newSel.getQuantity()));
      }
    });

    return table;
  }

  private VBox createFormSection() 
  {
    // more formatting stuff
    VBox form = new VBox(10);
    form.setPadding(new Insets(0, 0, 0, 15));
    form.setPrefWidth(300);

    Label titleLabel = new Label("Product & Inventory Control");
    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

    idField = new TextField();
    idField.setPromptText("e.g., PROD-001");

    nameField = new TextField();
    nameField.setPromptText("e.g., Organic Fertilizer");

    descField = new TextField();
    descField.setPromptText("e.g., 50kg bag nitrogen-rich");

    priceField = new TextField();
    priceField.setPromptText("e.g., 450.00");

    quantityField = new TextField();
    quantityField.setPromptText("e.g., 100");

    Button btnAdd = new Button("Add New Product");
    Button btnRestock = new Button("Restock Quantity");
    Button btnUpdate = new Button("Update Product Info");
    Button btnDelete = new Button("Remove Product");

    btnAdd.setMaxWidth(Double.MAX_VALUE);
    btnRestock.setMaxWidth(Double.MAX_VALUE);
    btnUpdate.setMaxWidth(Double.MAX_VALUE);
    btnDelete.setMaxWidth(Double.MAX_VALUE);

    // Event Handlers
    btnAdd.setOnAction(e -> handleAddProduct());
    btnRestock.setOnAction(e -> handleRestockProduct());
    btnUpdate.setOnAction(e -> handleUpdateProduct());
    btnDelete.setOnAction(e -> handleDeleteProduct());

    form.getChildren().addAll(
      titleLabel,
      new Label("Product ID:"), idField,
      new Label("Product Name:"), nameField,
      new Label("Description:"), descField,
      new Label("Price:"), priceField,
      new Label("Quantity / Stock:"), quantityField,
      new Separator(),
      btnAdd, btnRestock, btnUpdate, btnDelete
    );

    return form;
  }

  private void handleAddProduct() 
  {
    try 
    {
      String id = idField.getText().trim();
      String name = nameField.getText().trim();
      String desc = descField.getText().trim();
      double price = Double.parseDouble(priceField.getText().trim());
      int qty = Integer.parseInt(quantityField.getText().trim());

      if (id.isEmpty() || name.isEmpty()) 
      {
        showAlert(Alert.AlertType.ERROR, "Validation Error", "ID and Name cannot be empty.");
        return;
      }

      Product product = new Product(id, name, desc, price);
      inventory.addProduct(product, qty);
      refreshTable();
      clearForm();
      showAlert(Alert.AlertType.INFORMATION, "Success", "Product added successfully!");
    } 
    catch (NumberFormatException ex) 
    {
        showAlert(Alert.AlertType.ERROR, "Input Error", "Price and Quantity must be valid numbers.");
    }
  }

  private void handleRestockProduct() 
  {
    try 
    {
      String id = idField.getText().trim();
      int addQty = Integer.parseInt(quantityField.getText().trim());

      if (inventory.restockProduct(id, addQty)) 
      {
        refreshTable();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Restocked product successfully!");
      } 
      else 
        showAlert(Alert.AlertType.ERROR, "Error", "Product ID not found.");
    } 
    catch (NumberFormatException ex) 
    {
      showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid stock quantity to add.");
    }
  }

  private void handleUpdateProduct() 
  {
    InventoryItem selected = table.getSelectionModel().getSelectedItem();
    if (selected == null) 
    {
      showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a product from the table to update.");
      return;
    }

    try 
    {
      selected.getProduct().setName(nameField.getText().trim());
      selected.getProduct().setDescription(descField.getText().trim());
      selected.getProduct().setPrice(Double.parseDouble(priceField.getText().trim()));
      selected.setQuantity(Integer.parseInt(quantityField.getText().trim()));

      refreshTable();
      showAlert(Alert.AlertType.INFORMATION, "Success", "Product information updated!");
    } 
    catch (NumberFormatException ex) 
    {
      showAlert(Alert.AlertType.ERROR, "Input Error", "Please ensure Price and Quantity are valid numbers.");
    }
  }

  private void handleDeleteProduct() 
  {
    String id = idField.getText().trim();

    if (inventory.removeProduct(id)) {
      refreshTable();
      clearForm();
      showAlert(Alert.AlertType.INFORMATION, "Success", "Product removed from inventory.");
    } 
    else
      showAlert(Alert.AlertType.ERROR, "Error", "Product ID not found.");
  }

  private void refreshTable() 
  {
    tableData.setAll(inventory.getItems());
    table.refresh();
  }

  private void clearForm() 
  {
    idField.clear();
    nameField.clear();
    descField.clear();
    priceField.clear();
    quantityField.clear();
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

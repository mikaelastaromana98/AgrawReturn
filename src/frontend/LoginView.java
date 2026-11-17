package frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginView extends VBox 
{

  private TextField userField;
  private PasswordField passField;
  private Button btnManager;
  private Button btnClerk;

  public LoginView(Runnable onManagerLogin, Runnable onClerkLogin) 
  {
    setSpacing(15);
    setPadding(new Insets(30));
    setAlignment(Pos.CENTER);
    setMaxWidth(350);

    Label titleLabel = new Label("System Login");
    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    userField = new TextField();
    userField.setPromptText("Username");

    passField = new PasswordField();
    passField.setPromptText("Password");

    btnManager = new Button("Login as Manager");
    btnManager.setMaxWidth(Double.MAX_VALUE);
    btnManager.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white;");

    btnClerk = new Button("Login as Clerk");
    btnClerk.setMaxWidth(Double.MAX_VALUE);
    btnClerk.setStyle("-fx-background-color: #00ff00; -fx-text-fill: white;");

    btnManager.setOnAction(e -> onManagerLogin.run());
    btnClerk.setOnAction(e -> onClerkLogin.run());

    getChildren().addAll(
      titleLabel,
      new Label("Username:"), userField,
      new Label("Password:"), passField,
      btnManager, btnClerk
    );
  }
}

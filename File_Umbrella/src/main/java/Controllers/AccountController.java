package Controllers;

import UmbrellaPackage.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AccountController {
    @FXML
    TextField accountNameTF;
    @FXML
    TextField accountPasswordTF;
    @FXML
    SubScene createAccScene;

    @FXML
    private void handleAccountAuth(ActionEvent event) {

        String url = "jdbc:mysql://127.0.0.1:3306/Accounts";
        String user = "root";
        String password = "1234";

        String accountName = accountNameTF.getText();
        String accountPassword = accountPasswordTF.getText();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("USE accounts");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (username VARCHAR(255), password VARCHAR(255), PRIMARY KEY (password))");
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + accountName + "' AND password = '" + accountPassword + "'");
            boolean accountExists = rs.next();

            if (accountExists) {
                // open filetransfercontroller class
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("download-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setTitle("File Umbrella");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } else if (!accountExists) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Account does not exist");
                alert.setHeaderText("No account found with the information entered");
                alert.setContentText("Please create an account or enter the correct details");
            }  else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please ensure all fields are filled");
                alert.setContentText("Please try again");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while checking for account existence");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleNewAcc(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("createacc-view.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleLoginScene(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}

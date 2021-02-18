package sample;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.DBUtil;

import static util.DBUtil.*;

public class Controller implements Initializable {
    @FXML
    private TextField txtEnterId;
    @FXML
    private TextField textEmail;
    @FXML
    private TextField textNewEmail;
    @FXML
    private PasswordField textPassword;

    Stage dialogStage = new Stage();
    Scene scene;

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public Controller() {
        connection = DBUtil.connectdb();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        String username = textEmail.getText().toString();
        String password = textPassword.getText().toString();

        String sql = "SELECT * FROM employee WHERE email = ? and password = ?";

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                infoBox("Enter Correct Username and Password", "Failed", null);
            }else{
                infoBox("Login Successfully", "Success", null);
                Node source = (Node) event.getSource();
                dialogStage = (Stage) source.getScene().getWindow();
                dialogStage.close();
                scene = new Scene(FXMLLoader.load(getClass().getResource("Menu.fxml")));
                dialogStage.setScene(scene);
                dialogStage.show();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void infoBox(String infoMessage, String titleBar, String headerMessage)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    public static void updateUsername(String id, String username)throws SQLException,ClassNotFoundException{
        String updateStmt =

                        " UPDATE employee\n" +
                        " SET email = '" + username + "'\n" +
                        " WHERE id = " + id + ";\n";


         try {
             dbExecuteUpdate(updateStmt);
         } catch (SQLException e) {
             System.out.print("Error occurred while UPDATE Operation: " + e);
             throw  e;
         }


    }

    public void handleButtonUpdateAction(ActionEvent event) throws SQLException,
            ClassNotFoundException {
        //String username = textEmail.getText().toString();
         try{
             updateUsername(txtEnterId.getText(),textNewEmail.getText());
             System.out.print("Username updated for user Id: " + txtEnterId.getText() + "\n" );
         }
         catch (SQLException e) {
             System.out.println("Problem occurred while updating email: " + e);
         }
    }


}

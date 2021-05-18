package client.gui.controllers;


import client.Client;
import data.Data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInWindowController {

    public static void setStartStage(Stage startStage) {
        LogInWindowController.startStage = startStage;
    }

    private static Stage startStage;

    private static String prevWindow;

    public static void setPrevWindow(String prevWindow) {
        LogInWindowController.prevWindow = prevWindow;
    }

    @FXML
    private TextField password;

    @FXML
    private Button enterButton;

    @FXML
    private Button back;

    @FXML
    private TextField login;

    @FXML
    void enter(ActionEvent event) {
        Client.setLogin(login.getText());
        Client.setPassword(password.getText());
        Data data = new Data("authorization", null, Client.getLogin(), Client.getPassword());

        try {
            String ans = Client.sendCommand(data);
                if (ans.equals("You authorization")) {
                    CommandsWindowController.setStartStage(Client.changeWindow("/client/gui/scenes/commands.fxml", startStage, 400, 530));
                }else {
                    Client.showWindow(200, 400, ans, Color.RED);
                }
        }catch (IOException e) {
            Client.showWindow(200, 400, "Server is tired. Try to reconnect later", Color.RED);
        }

    }

    @FXML
    void back(ActionEvent event) {
        StartWindowController.setStage(Client.changeWindow(prevWindow, startStage, 435, 100));
    }

}

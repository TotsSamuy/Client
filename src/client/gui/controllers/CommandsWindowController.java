package client.gui.controllers;

import client.Client;
import data.Data;
import exceptions.RecursiveScript;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import processor.FileProcessor;

import static data.Resources.HELP;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class CommandsWindowController {

    public Button scriptButton;
    ObservableList<String> languages = FXCollections.observableArrayList("Русский", "Український", "português", "Español");
    private static Stage startStage;

    private Stage getStage;
    private String arg;

    public static void setStartStage(Stage startStage) {
        CommandsWindowController.startStage = startStage;
    }

    @FXML
    private Button removeGreaterButton;

    @FXML
    private Button helpButton;

    @FXML
    public Button tableButton;

    @FXML
    public Button visualizationButton;

    @FXML
    private Button maxByCommentButton;

    @FXML
    private Button addIfMaxButton;

    @FXML
    private Button addButton;

    @FXML
    private ChoiceBox<String> language;

    @FXML
    private Button sumOfDiscountButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button printUniquePriceButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button showButton;

    @FXML
    private Button infoButton;

    @FXML
    private Button userNameButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button removeByIdButton;

    @FXML
    private Button addIfMinButton;

    @FXML
    void initialize() {
        userNameButton.setText(Client.getLogin());
        language.setValue("Русский");
        language.setItems(languages);
    }

    @FXML
    void exit(ActionEvent event) {
        System.exit(2);
    }

    @FXML
    void add(ActionEvent event) {
        GetTicketController.setCommandName("add");
        GetTicketController.setStage(Client.changeWindow("/client/gui/scenes/ticket.fxml", startStage, 400, 300));
    }

    @FXML
    void addIfMin(ActionEvent event) {
        GetTicketController.setCommandName("add_if_min");
        GetTicketController.setStage(Client.changeWindow("/client/gui/scenes/ticket.fxml", startStage, 400, 300));
    }

    @FXML
    void addIfMax(ActionEvent event) {
        GetTicketController.setCommandName("add_if_max");
        GetTicketController.setStage(Client.changeWindow("/client/gui/scenes/ticket.fxml", startStage, 400, 300));
    }

    private void send(Data data, double height, double width) {
        try {
            String ans = Client.sendCommand(data);
            StringBuilder corAns = new StringBuilder();
            int cnt = 1;
            if (!data.getCommandName().equals("help")) {
                while (ans.length() > 100 * cnt) {
                    corAns.append(ans.substring((cnt - 1) * 100, cnt * 100) + "\n");
                    cnt++;
                }
                if (corAns.length() > 0) {
                    corAns.append(ans.substring(100 * (cnt - 1)));
                    ans = corAns.toString();
                }
            }
            CommandsWindowController.setStartStage(Client.changeWindow("/client/gui/scenes/commands.fxml", startStage, 450, 530));
            Client.showWindow(height, width, ans, Color.BLACK);
        } catch (IOException e) {
            Client.showWindow(200, 400, "Server is tired. Try to reconnect later", Color.RED);
        }
    }

    public void getWindow(String field, String commandName) {
        Label label = new Label(field);
        TextField textField = new TextField();
        //final String[] arg = new String[1];
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int id = checkId(textField.getText());
                if (id == -1) {
                    Client.showWindow(150, 200, "Incorrect id", Color.RED);
                } else {
                    List<Object> args = new ArrayList<>();
                    args.add(id);
                    send(new Data(commandName, args, Client.getLogin(), Client.getPassword()), 200, 400);
                }
                getStage.close();
            }
        });
        label.setFont(new Font(20));
        VBox vBox = new VBox(label, textField);
        vBox.setSpacing(25);
        BorderPane pane = new BorderPane(vBox);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMinHeight(100);
        stage.setHeight(200);
        stage.setMinWidth(150);
        stage.setWidth(300);
        getStage = stage;
        stage.show();
    }

    public void getScriptWindow(String field) {
        Label label = new Label(field);
        TextField textField = new TextField();
        //final String[] arg = new String[1];
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FileProcessor processor = new FileProcessor(textField.getText(), new TreeSet<>());
                    List<Data> coms = processor.readData();
                    for (Data com : coms) {
                        Client.sendCommand(com);
                    }
                    Client.showWindow(150, 300, "Script executed", Color.GREEN);
                } catch (IOException e) {
                    Client.showWindow(150, 300, "Problems with the file which you enter", Color.RED);
                } catch (RecursiveScript recursiveScript) {
                    Client.showWindow(150, 300, "Error! Recursive in script", Color.RED);
                }
                getStage.close();
            }
        });
        label.setFont(new Font(20));
        VBox vBox = new VBox(label, textField);
        vBox.setSpacing(25);
        BorderPane pane = new BorderPane(vBox);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMinHeight(100);
        stage.setHeight(200);
        stage.setMinWidth(150);
        stage.setWidth(300);
        getStage = stage;
        stage.show();
    }

    public void getUpdateWindow(String field) {
        Label label = new Label(field);
        TextField textField = new TextField();
        //final String[] arg = new String[1];
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int id = checkId(textField.getText());
                if (id == -1) {
                    Client.showWindow(150, 200, "Incorrect id", Color.RED);
                } else {
                    GetTicketController.setCommandName("update");
                    GetTicketController.addArg(id);
                    GetTicketController.setStage(Client.changeWindow("/client/gui/scenes/ticket.fxml", startStage, 400, 300));
                }
                getStage.close();
            }
        });
        label.setFont(new Font(20));
        VBox vBox = new VBox(label, textField);
        vBox.setSpacing(25);
        BorderPane pane = new BorderPane(vBox);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMinHeight(100);
        stage.setHeight(200);
        stage.setMinWidth(150);
        stage.setWidth(300);
        getStage = stage;
        stage.show();
    }

    int checkId(String data) {
        try {
            return Integer.parseInt(data);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @FXML
    void maxByComment(ActionEvent event) {
        send(new Data("max_by_comment", null, Client.getLogin(), Client.getPassword()), 400, 970);
    }

    @FXML
    void info(ActionEvent event) {
        send(new Data("info", null, Client.getLogin(), Client.getPassword()), 200, 400);
    }

    @FXML
    void printUniquePrice(ActionEvent event) {
        send(new Data("print_unique_price", null, Client.getLogin(), Client.getPassword()), 200, 400);
    }

    @FXML
    void help(ActionEvent event) {
        send(new Data("help", null, Client.getLogin(), Client.getPassword()), 400, 850);
    }

    @FXML
    void show(ActionEvent event) {
        send(new Data("show", null, Client.getLogin(), Client.getPassword()), 400, 970);
    }

    @FXML
    void removeById(ActionEvent event) {
        getWindow("id", "remove_by_id");
    }

    @FXML
    void removeGreater(ActionEvent event) {
        GetTicketController.setCommandName("remove_greater");
        GetTicketController.setStage(Client.changeWindow("/client/gui/scenes/ticket.fxml", startStage, 400, 300));
    }

    @FXML
    void update(ActionEvent event) {
        getUpdateWindow("id");
    }

    @FXML
    void sumOfDiscount(ActionEvent event) {
        send(new Data("sum_of_discount", null, Client.getLogin(), Client.getPassword()), 100, 200);
    }

    @FXML
    void clear(ActionEvent event) {
        send(new Data("clear", null, Client.getLogin(), Client.getPassword()), 400, 300);
    }

    @FXML
    void changeUser(ActionEvent event) {
        StartWindowController.setStage(Client.changeWindow("/client/gui/scenes/start.fxml", startStage, 435, 100));
    }

    @FXML
    public void visualize(ActionEvent event) {

    }

    @FXML
    public void openTable(ActionEvent event) {
        TableController.setStage(Client.changeWindow("/client/gui/scenes/table.fxml", startStage, 300, 600));
    }

    public void executeScript(ActionEvent event) {
        getScriptWindow("File name");
    }
}

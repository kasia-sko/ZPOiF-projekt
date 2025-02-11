package org.example.app_nbp;

import jakarta.mail.MessagingException;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class LeftBottomPaneBuilder {

    private TextField bidField;
    private TextField askField;
    private ComboBox<String> currencyComboBox;

    public LeftBottomPaneBuilder(TextField bidField, TextField askField, ComboBox<String> currencyComboBox) {
        this.bidField = bidField;
        this.askField = askField;
        this.currencyComboBox = currencyComboBox;
    }

    public VBox build() {
        VBox bottomPane = new VBox(10);
        bottomPane.setAlignment(Pos.CENTER);

        Label title = new Label("Otrzymaj maila z kursem waluty");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10px");

        Label mailLabel = new Label("Podaj maila");
        mailLabel.setFont(new Font(14));
        TextField mailField = new TextField();
        mailField.setPromptText("Twój e-mail");
        mailField.setFont(new Font(14));
        mailField.setMaxWidth(200);

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setStyle("-fx-font-size: 14px;");

        Button sendButton = getSendButton(mailField, errorLabel);

        bottomPane.getChildren().addAll(title, mailLabel, mailField, sendButton, errorLabel);

        return bottomPane;
    }

    private Button getSendButton(TextField mailField, Label errorLabel) {
        Button sendButton = new Button("Wyślij");
        sendButton.getStyleClass().add("green-button");
        sendButton.setStyle("-fx-font-size: 14px;");
        sendButton.setOnAction(event -> {
            String recipentEmail = mailField.getText();
            String currency = currencyComboBox.getValue();
            String bid = bidField.getText();
            String ask = askField.getText();
            ErrorLabelThread errorLabelThread = new ErrorLabelThread(errorLabel);
            errorLabelThread.setDaemon(true);
            if (!recipentEmail.isEmpty() && currency != null && !bid.isEmpty() && !ask.isEmpty()) {
                try {
                    new EmailSender().sendEmail(recipentEmail, currency, bid, ask);
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText("Wysłano");
                    errorLabelThread.start();
                } catch (MessagingException e) {
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText("Niepoprawny mail");
                    errorLabelThread.start();
                }
            }
            else if (recipentEmail.isEmpty()){
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Podaj maila");
                errorLabelThread.start();
            }
            else if (currency == null){
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Wybierz walutę");
                errorLabelThread.start();
            }
            else {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Załaduj kurs");
                errorLabelThread.start();
            }

        });
        return sendButton;
    }

}

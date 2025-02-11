package org.example.app_nbp;

import app.data.BuySellRate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.Locale;

import static app.data.DataParser.parseTableC;
import static org.example.app_nbp.RightPaneBuilder.getFlagPathForCurrency;

public class LeftTopPaneBuilder {

    private TextField bidField;
    private TextField askField;
    private ComboBox<String> currencyComboBox;
    private Label dateLabel;

    public VBox build() {
        VBox topPane = new VBox();

        topPane.setAlignment(Pos.CENTER);

        Label title = new Label("Kurs walut");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10px;");

        VBox currencyBox = new VBox(10);
        Label selectCurrencyLabel = new Label("Wybierz walutę");
        selectCurrencyLabel.setFont(new Font(14));

        // Flaga
        ImageView flagView = new ImageView();
        flagView.setFitWidth(50);
        flagView.setFitHeight(30);

        currencyComboBox = new ComboBox<>();
        ObservableList<String> currencies = FXCollections.observableArrayList(
                "USD", "AUD", "CAD", "EUR", "HUF", "CHF", "GBP", "JPY", "CZK", "DKK", "NOK", "SEK"
        );
        currencyComboBox.setPrefWidth(85);
        currencyComboBox.setPrefHeight(30);
        currencyComboBox.setItems(currencies);
        currencyComboBox.setValue(currencies.getFirst());
        currencyComboBox.setOnAction(event -> comboBoxAction(flagView));

        // Default
        currencyComboBox.setValue("USD");
        flagView.setImage(new Image(getFlagPathForCurrency("USD")));

        // HBox dla ComboBox i flagi
        HBox comboWithFlag = new HBox(10);
        comboWithFlag.getChildren().addAll(currencyComboBox, flagView);


        dateLabel = new Label("");
        dateLabel.setFont(new Font(14));

        bidField = new TextField();
        bidField.setEditable(false);
        bidField.setMouseTransparent(true);
        bidField.setFont(new Font(14));
        bidField.setMaxWidth(120);
        Label bidLabel = new Label("Kupno");
        bidLabel.setFont(new Font(14));
        bidLabel.setMaxWidth(120);
        askField = new TextField();
        askField.setEditable(false);
        askField.setMouseTransparent(true);
        askField.setFont(new Font(14));
        askField.setMaxWidth(120);
        Label askLabel = new Label("Sprzedaż");
        askLabel.setFont(new Font(14));
        askLabel.setMaxWidth(120);

        Button loadButton = new Button("Załaduj");
        loadButton.getStyleClass().add("green-button");
        loadButton.setStyle("-fx-font-size: 14px;");
        loadButton.setOnAction(event -> comboBoxAction(flagView));

        VBox bidBox = new VBox(10);
        bidBox.getChildren().addAll(bidLabel, bidField, loadButton);
        VBox askBox = new VBox(10);
        askBox.getChildren().addAll(askLabel,askField);

        HBox amountBox = new HBox(10);
        amountBox.getChildren().addAll(bidBox, askBox);

        currencyBox.getChildren().addAll(selectCurrencyLabel, comboWithFlag, dateLabel, amountBox);

        HBox currencySelectionBox = new HBox(10);
        currencySelectionBox.setAlignment(Pos.CENTER);
        currencySelectionBox.getChildren().addAll(currencyBox);

        topPane.getChildren().addAll(title, currencySelectionBox);

        return topPane;
    }

    private void comboBoxAction(ImageView flagView) {
        String currency = currencyComboBox.getValue();

        if (currency != null) {
            try {
                String flagPath = getFlagPathForCurrency(currency);
                flagView.setImage(new Image(flagPath));
                BuySellRate rate = parseTableC(currency).getRates().getFirst();
                double bid = rate.getBid();
                double ask = rate.getAsk();
                bidField.setText(String.format(Locale.US, "%.4f", bid));
                askField.setText(String.format(Locale.US, "%.4f", ask));
                dateLabel.setText("Kurs z dnia " + rate.getEffectiveDate());
            } catch (NumberFormatException | IOException | InterruptedException e) {
                bidField.setText("Błąd");
                askField.setText("Błąd");
                dateLabel.setText("");
            }
        } else {
            bidField.setText("Brak danych");
            askField.setText("Brak danych");
            dateLabel.setText("");
        }

    }

    public TextField getBidField() {
        return bidField;
    }

    public TextField getAskField() {
        return askField;
    }

    public ComboBox<String> getCurrencyComboBox() {
        return currencyComboBox;
    }
}

package org.example.app_nbp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.Locale;

import static app.data.DataParser.parseTableC;

public class RightPaneBuilder {

    public VBox build() {
        VBox rightPane = new VBox(20);
        rightPane.setAlignment(Pos.CENTER);

        Label title = new Label("Kalkulator walut");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10px;");

        // VBox dla waluty 1
        VBox currencyBox1 = new VBox(15);
        Label selectCurrencyLabel1 = new Label("Sprzedaję");
        selectCurrencyLabel1.setFont(new Font(16));

        ComboBox<String> currencyComboBox1 = new ComboBox<>();
        currencyComboBox1.setPromptText("Wybierz walutę");
        currencyComboBox1.setPrefWidth(85);
        currencyComboBox1.setPrefHeight(30);

        // Flaga
        ImageView flagView1 = new ImageView();
        flagView1.setFitWidth(50);
        flagView1.setFitHeight(30);

        // Default
        currencyComboBox1.setValue("PLN");
        flagView1.setImage(new Image(getFlagPathForCurrency("PLN")));

        //HBox dla ComboBox i flagi
        HBox comboWithFlag1 = new HBox(10);
        comboWithFlag1.getChildren().addAll(currencyComboBox1, flagView1);

        TextField amountField1 = new TextField();
        amountField1.setFont(new Font(14));
        amountField1.setPromptText("Kwota w walucie");
        amountField1.setMaxWidth(150);
        amountField1.setPrefHeight(30);

        currencyBox1.getChildren().addAll(selectCurrencyLabel1, comboWithFlag1, amountField1);

        // Obsługa zmiany waluty
        currencyComboBox1.setOnAction(event -> {
            String selectedCurrency = currencyComboBox1.getValue();
            if (selectedCurrency != null) {
                String flagPath = getFlagPathForCurrency(selectedCurrency);
                flagView1.setImage(new Image(flagPath));
            }
        });

        // VBox dla waluty 2
        VBox currencyBox2 = new VBox(15);

        Label selectCurrencyLabel2 = new Label("Kupuję");
        selectCurrencyLabel2.setFont(new Font(16));

        ComboBox<String> currencyComboBox2 = new ComboBox<>();
        currencyComboBox2.setPromptText("Wybierz walutę");
        currencyComboBox2.setPrefWidth(85);
        currencyComboBox2.setPrefHeight(30);

        // Flaga
        ImageView flagView2 = new ImageView();
        flagView2.setFitWidth(50);
        flagView2.setFitHeight(30);

        //Default
        currencyComboBox2.setValue("USD");
        flagView2.setImage(new Image(getFlagPathForCurrency("USD")));

        //HBox dla ComboBox i flagi
        HBox comboWithFlag2 = new HBox(10);
        comboWithFlag2.getChildren().addAll(currencyComboBox2, flagView2);

        TextField amountField2 = new TextField();
        amountField2.setFont(new Font(14));
        amountField2.setPromptText("Kwota w walucie");
        amountField2.setMaxWidth(150);
        amountField2.setPrefHeight(30);

        currencyBox2.getChildren().addAll(selectCurrencyLabel2, comboWithFlag2, amountField2);

        // Obsługa zmiany waluty
        currencyComboBox2.setOnAction(event -> {
            String selectedCurrency = currencyComboBox2.getValue();
            if (selectedCurrency != null) {
                String flagPath = getFlagPathForCurrency(selectedCurrency);
                flagView2.setImage(new Image(flagPath));
            }
        });

        // Przyciski zamiany i przeliczenia
        Button swapButton = new Button("⇄"); // Strzałka
        swapButton.getStyleClass().add("swap-button");
        swapButton.setOnAction(event -> {
            String tempCurrency = currencyComboBox1.getValue();
            currencyComboBox1.setValue(currencyComboBox2.getValue());
            currencyComboBox2.setValue(tempCurrency);
            String amountText1 = amountField1.getText();
            if (!amountText1.isEmpty()) {
                try {
                    double amount1 = Double.parseDouble(amountText1);
                    String fromCurrency = currencyComboBox1.getValue();
                    String toCurrency = currencyComboBox2.getValue();
                    double convertedAmount = convertCurrencyFrom(amount1, fromCurrency, toCurrency);
                    amountField2.setText(String.format(Locale.US, "%.2f", convertedAmount));
                } catch (NumberFormatException | IOException | InterruptedException e) {
                    amountField1.setText("Błąd");
                    amountField2.setText("Błąd");
                }
            }
        });

        // Przyciski do przeliczania z ikonami strzałek
        Button convertButtonFrom = new Button("Przelicz \u2192"); // Strzałka w prawo
        convertButtonFrom.setStyle("-fx-font-size: 14px; -fx-pref-height: 30px; -fx-pref-width: 85px;");
        convertButtonFrom.getStyleClass().add("green-button");
        convertButtonFrom.setOnAction(event -> {
            String currency1 = currencyComboBox1.getValue();
            String currency2 = currencyComboBox2.getValue();
            String amountText1 = amountField1.getText();
            if (currency1 != null && currency2 != null && !amountText1.isEmpty()) {
                try {
                    double amount1 = Double.parseDouble(amountText1);
                    double amount2 = convertCurrencyFrom(amount1, currency1, currency2);
                    amountField2.setText(String.format(Locale.US, "%.2f", amount2));
                } catch (NumberFormatException | IOException | InterruptedException e) {
                    amountField2.setText("Błąd");
                }
            } else {
                amountField2.setText("Brak danych");
            }
        });

        Button convertButtonTo = new Button("Przelicz \u2190"); // Strzałka w lewo
        convertButtonTo.setStyle("-fx-font-size: 14px; -fx-pref-height: 30px; -fx-pref-width: 85px;");
        convertButtonTo.getStyleClass().add("green-button");
        convertButtonTo.setOnAction(event -> {
            String currency1 = currencyComboBox1.getValue();
            String currency2 = currencyComboBox2.getValue();
            String amountText2 = amountField2.getText();

            if (currency1 != null && currency2 != null && !amountText2.isEmpty()) {
                try {
                    double amount2 = Double.parseDouble(amountText2);
                    double amount1 = convertCurrencyTo(amount2, currency1, currency2);
                    amountField1.setText(String.format(Locale.US, "%.2f", amount1));
                } catch (NumberFormatException | IOException | InterruptedException e) {
                    amountField1.setText("Błąd");
                }
            } else {
                amountField1.setText("Brak danych");
            }
        });

        currencyBox1.getChildren().add(convertButtonFrom);
        currencyBox2.getChildren().add(convertButtonTo);

        // HBox dla obu VBoxów i przycisku zamiany
        HBox currencySelectionBox = new HBox(60);
        currencySelectionBox.setAlignment(Pos.CENTER);
        currencySelectionBox.getChildren().addAll(currencyBox1, swapButton, currencyBox2);

        ObservableList<String> currencies = FXCollections.observableArrayList(
                "PLN", "USD", "AUD", "CAD", "EUR", "HUF", "CHF", "GBP", "JPY", "CZK", "DKK", "NOK", "SEK"
        );
        currencyComboBox1.setItems(currencies);
        currencyComboBox2.setItems(currencies);

        rightPane.getChildren().addAll(title, currencySelectionBox);

        return rightPane;
    }

    private double convertCurrencyFrom(double amount, String fromCurrency, String toCurrency) throws IOException, InterruptedException {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        } else if (fromCurrency.equals("PLN")) {
            return amount / parseTableC(toCurrency).getRates().getFirst().getAsk();
        } else if (toCurrency.equals("PLN")){
            return amount * parseTableC(fromCurrency).getRates().getFirst().getBid();
        } else{
            double bid = parseTableC(fromCurrency).getRates().getFirst().getBid();
            double ask = parseTableC(toCurrency).getRates().getFirst().getAsk();
            return (amount * bid) / ask;
        }
    }
    private double convertCurrencyTo(double amount, String fromCurrency, String toCurrency) throws IOException, InterruptedException {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        } else if (fromCurrency.equals("PLN")) {
            return amount * parseTableC(toCurrency).getRates().getFirst().getAsk();
        } else if (toCurrency.equals("PLN")){
            return amount / parseTableC(fromCurrency).getRates().getFirst().getBid();
        } else{
            double bid = parseTableC(fromCurrency).getRates().getFirst().getBid();
            double ask = parseTableC(toCurrency).getRates().getFirst().getAsk();
            return (amount * ask) / bid;
        }
    }
    protected static String getFlagPathForCurrency(String currency) {
        return switch (currency) {
            case "PLN" -> "flags/poland.png";
            case "USD" -> "flags/usa.png";
            case "EUR" -> "flags/europe.png";
            case "GBP" -> "flags/uk.png";
            case "JPY" -> "flags/japan.png";
            case "AUD" -> "flags/australia.png";
            case "CAD" -> "flags/canada.png";
            case "CHF" -> "flags/switzerland.png";
            case "HUF" -> "flags/hungary.png";
            case "CZK" -> "flags/czech.png";
            case "DKK" -> "flags/denmark.png";
            case "NOK" -> "flags/norway.png";
            case "SEK" -> "flags/sweden.png";
            case "THB" -> "flags/thailand.png";
            case "HKD" -> "flags/hongkong.png";
            case "NZD" -> "flags/newzealand.png";
            case "SGD" -> "flags/singapore.png";
            case "UAH" -> "flags/ukraine.png";
            case "ISK" -> "flags/island.png";
            case "RON" -> "flags/romania.png";
            case "BGN" -> "flags/bulgaria.png";
            case "TRY" -> "flags/turkey.png";
            case "ILS" -> "flags/israel.png";
            case "CLD" -> "flags/chile.png";
            case "PHP" -> "flags/philippines.png";
            case "MXN" -> "flags/mexico.png";
            case "ZAR" -> "flags/rpa.png";
            case "BRL" -> "flags/brazil.png";
            case "MYR" -> "flags/malaysia.png";
            case "IDR" -> "flags/indonesia.png";
            case "INR" -> "flags/indie.png";
            case "KRW" -> "flags/southkorea.png";
            case "CNY" -> "flags/china.png";
            default -> "flags/default.png"; // Domyślna flaga błąd
        };
    }

}


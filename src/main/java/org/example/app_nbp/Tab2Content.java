package org.example.app_nbp;

import app.data.DataParser;
import app.data.ExchangeRate;
import app.data.ExchangeRateResponse;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.app_nbp.RightPaneBuilder.getFlagPathForCurrency;

public class Tab2Content {

    private LineChart<String, Number> lineChart;
    private AnchorPane layout;
    private Label errorLabel;
    private TextFlow textFlow;
    private TextField investTextField;
    private TextField profitTextField;

    public AnchorPane build(Stage stage) {
        // Główny kontener AnchorPane
        layout = new AnchorPane();

        // Lewe, środkowe i prawe kolumny
        VBox vBoxLeft = new VBox(10);
        VBox vBoxMiddle = new VBox(15);
        VBox vBoxRight = new VBox(10);

        vBoxRight.setAlignment(Pos.CENTER);
        vBoxLeft.setAlignment(Pos.CENTER);
        vBoxMiddle.setAlignment(Pos.CENTER);

        HBox dateHBox = new HBox(10);
        VBox dateLeftVBox = new VBox(10);

        Button investPlotButton = new Button("Wykres inwestycji");
        investPlotButton.getStyleClass().add("green-button");
        investPlotButton.setStyle("-fx-font-size: 12px; -fx-pref-height: 25px; -fx-pref-width: 120px;");

        HBox plotHBox = new HBox(10);

        GridPane defaultDatePane = new GridPane();
        defaultDatePane.setHgap(10);
        defaultDatePane.setVgap(10);

        Button weekButton = new Button("Tydzień");
        weekButton.setStyle("-fx-font-size: 12px; -fx-pref-height: 25px; -fx-pref-width: 80px;");
        Button monthButton = new Button("Miesiąc");
        monthButton.setStyle("-fx-font-size: 12px; -fx-pref-height: 25px; -fx-pref-width: 80px;");
        Button threeMonthButton = new Button("3 miesiące");
        threeMonthButton.setStyle("-fx-font-size: 12px; -fx-pref-height: 25px; -fx-pref-width: 80px;");
        Button yearButton = new Button("Rok");
        yearButton.setStyle("-fx-font-size: 12px; -fx-pref-height: 25px; -fx-pref-width: 80px;");
        defaultDatePane.add(weekButton,0,0);
        defaultDatePane.add(monthButton,1,0);
        defaultDatePane.add(threeMonthButton,0,1);
        defaultDatePane.add(yearButton,1,1);

        Label investLabel = new Label("Zainwestowana kwota w złotówkach");
        investTextField = new TextField();
        investTextField.setPromptText("Wpisz kwotę");

        Label profitLabel = new Label("Kwota po inwestycji");
        profitTextField = new TextField();
        profitTextField.setEditable(false);
        profitTextField.setMouseTransparent(true);

        investLabel.setStyle("-fx-font-size: 12px;");
        investTextField.setStyle("-fx-font-size: 12px;");
        profitLabel.setStyle("-fx-font-size: 12px;");
        profitTextField.setStyle("-fx-font-size: 12px;");

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        errorLabel.setVisible(false);

        Label label = new Label("Waluta:");
        label.setStyle("-fx-font-size: 16px;");

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefHeight(30);

        List<String> currencyListA = null;
        try {
            currencyListA = DataParser.createCurrencyCodeList("A");
            currencyListA.removeLast();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        comboBox.getItems().addAll(currencyListA);

        // Flaga
        ImageView flagView = new ImageView();
        flagView.setFitWidth(50);
        flagView.setFitHeight(30);

        // Default
        comboBox.setValue("USD");
        flagView.setImage(new Image(getFlagPathForCurrency("USD")));

        // HBox dla ComboBox i flagi
        HBox comboWithFlag = new HBox(10);
        comboWithFlag.getChildren().addAll(comboBox, flagView);
        comboWithFlag.setAlignment(Pos.CENTER);

        // Obsługa zmiany waluty
        comboBox.setOnAction(event -> {
            String selectedCurrency = comboBox.getValue();
            if (selectedCurrency != null) {
                String flagPath = getFlagPathForCurrency(selectedCurrency);
                flagView.setImage(new Image(flagPath));
            }
        });

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Data początkowa");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Data końcowa");

        Label selectedDatesLabel = new Label("Wybierz przedział czasowy");
        selectedDatesLabel.setStyle("-fx-font-size: 12px;");

        // Ustawienia dla dat
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && item.isBefore(LocalDate.now().minusDays(367))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;");
                }
                if (item != null && (item.isAfter(LocalDate.now()) || item.isEqual(LocalDate.now()))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;");
                }
                if (dp == startDatePicker && endDatePicker.getValue() != null && item.isAfter(endDatePicker.getValue())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;");
                }
                if (dp == endDatePicker && startDatePicker.getValue() != null && item.isBefore(startDatePicker.getValue())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;");
                }
            }
        };

        startDatePicker.setDayCellFactory(dayCellFactory);
        endDatePicker.setDayCellFactory(dayCellFactory);

        // Tworzenie osi X i Y dla wykresu
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Data");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Wartość");
        lineChart = new LineChart<>(xAxis, yAxis);

        EventHandler<ActionEvent> buttonClickHandler = e -> {
            Button sourceButton = (Button) e.getSource();
            String buttonText = sourceButton.getText();
            LocalDate currentDate = LocalDate.now();
            LocalDate startDate = null;
            LocalDate endDate = currentDate;

            switch (buttonText) {
                case "Tydzień":
                    startDate = currentDate.minusWeeks(1);
                    break;
                case "Miesiąc":
                    startDate = currentDate.minusMonths(1);
                    break;
                case "3 miesiące":
                    startDate = currentDate.minusMonths(3);
                    break;
                case "Rok":
                    startDate = currentDate.minusYears(1);
                    break;
            }

            startDatePicker.setValue(startDate);
            endDatePicker.setValue(endDate);
        };

        weekButton.setOnAction(buttonClickHandler);
        monthButton.setOnAction(buttonClickHandler);
        threeMonthButton.setOnAction(buttonClickHandler);
        yearButton.setOnAction(buttonClickHandler);

        Button buttonPlot = new Button("Wykres waluty");
        buttonPlot.getStyleClass().add("green-button");
        buttonPlot.setStyle("-fx-font-size: 12px; -fx-pref-height: 25px; -fx-pref-width: 120px;");
        buttonPlot.setOnAction(e -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String code = comboBox.getValue();

            if (startDate != null && endDate != null && code != null) {
                // Aktualizacja wykresu
                if (!layout.getChildren().contains(lineChart)) {
                    layout.getChildren().add(lineChart);
                    AnchorPane.setTopAnchor(lineChart, 280.0);
                    AnchorPane.setLeftAnchor(lineChart, 50.0);
                    AnchorPane.setRightAnchor(lineChart, 50.0);
                    AnchorPane.setBottomAnchor(lineChart, 50.0);
                }
                updateLineChart(String.valueOf(startDate), String.valueOf(endDate), code, "currency");

            } else {
                System.out.println("Uzupełnij wszystkie pola!");
            }
        });

        investPlotButton.setOnAction(e -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String code = comboBox.getValue();

            if (startDate != null && endDate != null && code != null && !investTextField.getText().isEmpty()) {
                try {
                    Double.parseDouble(investTextField.getText());
                } catch (NumberFormatException ex) {
                    investTextField.setText("Błąd");
                }
                // Aktualizacja wykresu
                if (!layout.getChildren().contains(lineChart)) {
                    layout.getChildren().add(lineChart);
                    AnchorPane.setTopAnchor(lineChart, 280.0);
                    AnchorPane.setLeftAnchor(lineChart, 50.0);
                    AnchorPane.setRightAnchor(lineChart, 50.0);
                    AnchorPane.setBottomAnchor(lineChart, 50.0);
                }
                updateLineChart(String.valueOf(startDate), String.valueOf(endDate), code, "invest");

            } else {
                errorLabel.setText("Uzupełnij wszystkie pola!");
                errorLabel.setVisible(true);
                lineChart.setVisible(false);
                textFlow.setVisible(false);
            }
        });

        // Kontener dla DatePickerów
        VBox datePickersBox = new VBox(10, startDatePicker, endDatePicker);
        datePickersBox.setAlignment(Pos.CENTER);
        dateLeftVBox.getChildren().addAll(datePickersBox);
        dateHBox.getChildren().addAll(dateLeftVBox, defaultDatePane);
        dateHBox.setAlignment(Pos.CENTER);
        plotHBox.setAlignment(Pos.CENTER);
        plotHBox.getChildren().addAll(buttonPlot, investPlotButton);

        vBoxMiddle.getChildren().addAll(label, comboWithFlag, selectedDatesLabel, dateHBox, plotHBox, errorLabel);
        textFlow = new TextFlow();
        vBoxLeft.getChildren().add(textFlow);
        vBoxRight.getChildren().addAll(investLabel,investTextField, profitLabel,profitTextField);

        // Dodanie kolumn do layoutu
        layout.getChildren().addAll(vBoxLeft, vBoxMiddle, vBoxRight);

        AnchorPane.setTopAnchor(vBoxLeft, 200.0);
        AnchorPane.setLeftAnchor(vBoxLeft, 200.0);
        AnchorPane.setTopAnchor(vBoxMiddle, 20.0);
        AnchorPane.setLeftAnchor(vBoxMiddle, 0.0);
        AnchorPane.setRightAnchor(vBoxMiddle, 0.0);
        AnchorPane.setTopAnchor(vBoxRight, 100.0);
        AnchorPane.setRightAnchor(vBoxRight, 250.0);

        // Ustawienie rozmiaru wykresu
        AnchorPane.setTopAnchor(lineChart, 250.0);
        AnchorPane.setLeftAnchor(lineChart, 50.0);
        AnchorPane.setRightAnchor(lineChart, 50.0);
        AnchorPane.setBottomAnchor(lineChart, 50.0);

        return layout;
    }

    private void updateLineChart(String startDate, String endDate, String code, String type) {
        errorLabel.setVisible(false);
        lineChart.setVisible(true);
        textFlow.setVisible(true);
        try {
            lineChart.getData().clear();
            lineChart.setAnimated(true);

            if (type.equals("currency")) {
                lineChart.setTitle("Wykres kursu " + code + " w dniach od " + startDate + " do " + endDate);
            } else {
                lineChart.setTitle("Wykres inwestycji " + code + " w dniach od " + startDate + " do " + endDate);

            }
            // Pobieranie danych o kursach
            ExchangeRateResponse exchangeRateResponse = DataParser.parseTableA(startDate, endDate, code);

            // Tworzenie mapy z datami i wartościami kursu
            Map<LocalDate, Double> exchangeRates = exchangeRateResponse.getRates()
                    .stream()
                    .collect(Collectors.toMap(
                            rate -> LocalDate.parse(rate.getEffectiveDate()),
                            ExchangeRate::getMid,
                            (v1, v2) -> v1
                    ));

            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<LocalDate> allDates = generateDateRange(start, end);
            List<LocalDate> sortedDates = allDates
                    .stream()
                    .sorted()
                    .toList();

            Double firstValue = exchangeRates.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().isEqual(start) || !entry.getValue().isNaN())
                    .filter(entry -> !entry.getKey().isBefore(start))
                    .min(Map.Entry.comparingByKey())
                    .map(Map.Entry::getValue)
                    .orElse((double) 0);
            Double lastValue = exchangeRates.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().isEqual(end) || !entry.getValue().isNaN())
                    .filter(entry -> !entry.getKey().isAfter(end))
                    .max(Map.Entry.comparingByKey())
                    .map(Map.Entry::getValue)
                    .orElse((double) 0);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Kurs " + code);
            if (type.equals("currency")) {
                for (LocalDate date : allDates) {
                    Double value = exchangeRates.getOrDefault(date, null);
                    if (value != null) {
                        series.getData().add(new XYChart.Data<>(date.toString(), value));
                    } else {
                        series.getData().add(new XYChart.Data<>(date.toString(), null));
                    }
                }
            } else {
                for (LocalDate date : allDates) {
                    if (exchangeRates.getOrDefault(date, null) != null) {
                        Double value = Math.round((Double.parseDouble(investTextField.getText()) *
                                (exchangeRates.getOrDefault(date, 0.0) - firstValue))*100.0)/100.0;
                        series.getData().add(new XYChart.Data<>(date.toString(), value));
                    } else {
                        series.getData().add(new XYChart.Data<>(date.toString(), null));
                    }
                }
            }

            XYChart.Data<String, Number> minData = series.getData().stream()
                    .filter(data -> data.getYValue() != null)
                    .min(Comparator.comparing(data -> data.getYValue().doubleValue()))
                    .orElse(null);

            XYChart.Data<String, Number> maxData = series.getData().stream()
                    .filter(data -> data.getYValue() != null)
                    .max(Comparator.comparing(data -> data.getYValue().doubleValue()))
                    .orElse(null);

            double maxValue = Objects.requireNonNull(maxData).getYValue().doubleValue();
            String maxDay = maxData.getXValue();

            double minValue = Objects.requireNonNull(minData).getYValue().doubleValue();
            String minDay = minData.getXValue();

            // Ustawienie marginesu dla osi Y
            NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
            double margin = (maxValue - minValue) * 0.1;
            yAxis.setAutoRanging(false);
            if (margin < 0.1){
                yAxis.setLowerBound(minValue - margin);
                yAxis.setUpperBound(maxValue + margin);
            } else if (margin < 1){
                yAxis.setLowerBound(Math.floor((minValue - margin) * 100.0) / 100.0);
                yAxis.setUpperBound(Math.ceil((maxValue + margin) * 100.0) / 100.0);
            } else{
                yAxis.setLowerBound(Math.floor((minValue - margin)));
                yAxis.setUpperBound(Math.ceil((maxValue + margin)));
            }

            double range = yAxis.getUpperBound() - yAxis.getLowerBound();
            int ticks = 5;
            double tickUnit = range / (ticks - 1);
            yAxis.setTickUnit(tickUnit);

            // Tworzenie osi X z datami
            CategoryAxis xAxis = (CategoryAxis) lineChart.getXAxis();
            List<String> dateLabels = sortedDates.stream()
                    .map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .toList();

            xAxis.getCategories().clear();
            xAxis.getCategories().addAll(dateLabels);

            // Nowa seria danych
            lineChart.getData().add(series);
            textFlow.getChildren().clear();

            double difference;

            if (type.equals("currency")) {
                // linia average
                double average = exchangeRates.values().stream()
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0);
                XYChart.Series<String, Number> averageSeries = new XYChart.Series<>();
                averageSeries.setName("Srednia wartosc");
                for (LocalDate date : sortedDates) {
                    averageSeries.getData().add(new XYChart.Data<>(date.toString(), average));
                }
                lineChart.getData().add(averageSeries);
                for (XYChart.Data<String, Number> data : averageSeries.getData()) {
                    Node node = data.getNode();
                    if (node != null) {
                        node.setStyle("-fx-background-color: transparent;"); // Ukrycie punktów na linii
                    }
                }
                Node line = averageSeries.getNode().lookup(".chart-series-line");
                if (line != null) {
                    line.setStyle("-fx-stroke-dash-array: 5 5; -fx-stroke-width: 1; -fx-stroke: gray;");
                }

                String averageText = String.format("Średnia: %.3f zł", average);
                Text averageTextStyled = new Text(averageText + "\n");
                averageTextStyled.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

                Text currencyText = new Text("Waluta: " + code + "\n");
                Text minText = new Text(String.format("Minimum: %.3f zł\n", minValue));
                Text maxText = new Text(String.format("Maksimum: %.3f zł", maxValue));

                currencyText.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
                minText.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
                maxText.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

                difference = lastValue - firstValue;
                String differenceText = String.format("%+.3f zł   \u2191%.3f%%", difference, difference / firstValue * 100);
                if(difference < 0){
                    differenceText = String.format("%+.3f zł   \u2193%.3f%%", difference, difference / firstValue * 100);

                }
                Text differenceTextStyled = new Text(differenceText + "\n");
                differenceTextStyled.setStyle("-fx-font-size: 14px; -fx-text-fill: red;");
                if (difference < 0) {
                    differenceTextStyled.setFill(Color.RED);
                } else {
                    differenceTextStyled.setFill(Color.GREEN);
                }
                textFlow.getChildren().addAll(currencyText, differenceTextStyled, averageTextStyled, minText, maxText);


            } else {
                difference = Double.parseDouble(investTextField.getText()) * (lastValue - firstValue);
                String differenceText = String.format("%+.2f zł", difference);
                profitTextField.setText(differenceText);
                if (difference < 0) {
                    profitTextField.setStyle("-fx-text-fill: red;");
                } else {
                    profitTextField.setStyle("-fx-text-fill: green;");
                }

                Text currencyText = new Text("Waluta: " + code + "\n");
                Text maxText = new Text(String.format("Maksymalny zysk: %.2f zł w dniu %s\n", maxValue, maxDay));
                Text minText = new Text(String.format("Maksymalna strata: %.2f zł w dniu %s", minValue, minDay));
                currencyText.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
                maxText.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
                minText.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
                textFlow.getChildren().addAll(currencyText, maxText, minText);
            }

            // najezdzanie na punkt na wykresie
            for (XYChart.Data<String, Number> data : series.getData()) {
                Platform.runLater(() -> {
                    Node node = data.getNode();
                    if (node != null) {
                        node.setStyle("-fx-background-color: red; -fx-background-radius: 6px; -fx-pref-width: 6px; -fx-pref-height: 5px;");
                        Tooltip tooltip = new Tooltip("" + data.getXValue() + "\n" + data.getYValue());
                        Tooltip.install(node, tooltip);
                        node.setOnMouseEntered(event -> {
                            node.setStyle("-fx-background-color: #ff0000, whitesmoke; -fx-background-insets: 0, 2; -fx-background-radius: 5;");
                        });
                    }
                });
            }

            lineChart.setLegendVisible(false);
            // bardzo wazna rzecz :)
            lineChart.setAnimated(false);

        } catch (NumberFormatException e) {
                errorLabel.setText("Wpisana kwota nie jest liczbą!");
                errorLabel.setVisible(true);
                lineChart.setVisible(false);
                textFlow.setVisible(false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorLabel.setText("Wybierz inny przedział czasowy");
            errorLabel.setVisible(true);
            lineChart.setVisible(false);
            textFlow.setVisible(false);
        }
    }

    private List<LocalDate> generateDateRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateRange = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            dateRange.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return dateRange;
    }

}
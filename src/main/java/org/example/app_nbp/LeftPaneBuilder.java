package org.example.app_nbp;

import javafx.geometry.Orientation;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class LeftPaneBuilder {

    public VBox build() {
        VBox mainContainer = new VBox();

        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);

        LeftTopPaneBuilder leftTopPaneBuilder = new LeftTopPaneBuilder();
        splitPane.getItems().add(leftTopPaneBuilder.build());

        TextField bidField = leftTopPaneBuilder.getBidField();
        TextField askField = leftTopPaneBuilder.getAskField();
        ComboBox<String> currencyComboBox = leftTopPaneBuilder.getCurrencyComboBox();

        LeftBottomPaneBuilder leftBottomPaneBuilder = new LeftBottomPaneBuilder(bidField, askField, currencyComboBox);

        splitPane.getItems().add(leftBottomPaneBuilder.build());

        splitPane.setDividerPositions(0.5);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        mainContainer.getChildren().add(splitPane);

        return mainContainer;
    }
}

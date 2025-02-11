package org.example.app_nbp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class StartTabBuilder {

    private TabPane tabPane;
    private Tab tab2;
    private Tab tab3;

    public StartTabBuilder(TabPane tabPane, Tab tab2, Tab tab3) {
        this.tabPane = tabPane;
        this.tab2 = tab2;
        this.tab3 = tab3;
    }

    public BorderPane build() {
        BorderPane root = new BorderPane();

        VBox vBox = new VBox(10);
        VBox vBoxLeft = new VBox(10);
        vBoxLeft.setAlignment(Pos.TOP_CENTER);
        vBoxLeft.setPadding(new Insets(0, 0, 0, 20));
        VBox vBoxRight = new VBox(10);
        vBoxRight.setAlignment(Pos.TOP_CENTER);
        HBox hBox =  new HBox(10);
        hBox.setAlignment(Pos.TOP_CENTER);

        vBox.setAlignment(Pos.CENTER);
        Label title = new Label("Cześć! Tu Walutozaur!");
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;; -fx-text-fill: #5CAA7A;");

        Text text = new Text("W tej aplikacji sprawdzisz dane dotyczące kursów walut pobieranych z Narodowego Banku Polskiego.\n\n");
        text.setStyle("-fx-font-size: 20px;");

        Hyperlink hyperlinkToTab2 = new Hyperlink("W pierwszej zakładce");
        hyperlinkToTab2.setStyle("-fx-font-size: 16px; -fx-text-fill: #2f4f3d; -fx-font-weight: bold;");
        Hyperlink hyperlinkToTab3 = new Hyperlink("W drugiej zakładce");
        hyperlinkToTab3.setStyle("-fx-font-size: 16px; -fx-text-fill: #2f4f3d; -fx-font-weight: bold;");

        Text text1 = new Text("możesz:\n" +
                "- stworzyć wykres kursu wybranej waluty w wybranym przedziale czasowym,\n" +
                "- sprawdzić, ile zarobiłbyś inwestując wpisaną kwotę w złotówkach.\n");
        text1.setStyle("-fx-font-size: 16px;");

        Text text2 = new Text("możesz:\n" +
                "- sprawdzić aktualny kurs wybranej waluty,\n" +
                "- podać swój adres mailowy i otrzymać informację o interesującym cię kursie,\n" +
                "- skorzystać z kalkulatora walut, aby przeliczyć wartości pomiędzy różnymi walutami.\n\n");
        text2.setStyle("-fx-font-size: 16px;");

        TextFlow textFlow1 = new TextFlow();
        Text text3 = new Text("Walutozaur w weekendy i święta odpoczywa, dlatego w te dni możesz napotkać braki danych.");
        text3.setStyle("-fx-font-size: 16px;");

        textFlow1.getChildren().addAll(text, hyperlinkToTab2, text1, hyperlinkToTab3, text2, text3);

        hyperlinkToTab2.setOnAction(event -> tabPane.getSelectionModel().select(tab2));
        hyperlinkToTab3.setOnAction(event -> tabPane.getSelectionModel().select(tab3));

        Image walutozaurImg = new Image("https://cdn.pixabay.com/photo/2021/03/05/22/44/dinosaur-6072475_640.png");
        ImageView walutozaur = new ImageView(walutozaurImg);

        walutozaur.setPreserveRatio(true);
        walutozaur.setFitWidth(800);
        walutozaur.setFitHeight(500);

        vBoxRight.getChildren().add(walutozaur);
        vBoxLeft.getChildren().addAll(textFlow1);
        hBox.getChildren().addAll(vBoxLeft, vBoxRight);
        vBox.getChildren().addAll(title, hBox);

        // stopka:
        HBox footer = new HBox();
        footer.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px;");
        footer.setSpacing(10);
        Label footerText = new Label("© 2025 Walutozaur.\nAutorzy: Martyna Sadowska, Katarzyna Skoczylas, Aleksandra Zawadka.");
        footerText.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        footerText.setTextAlignment(TextAlignment.CENTER);

        footer.getChildren().add(footerText);
        footer.setAlignment(Pos.CENTER);

        root.setCenter(vBox);
        root.setBottom(footer);

        return root;
    }
}

package org.example.app_nbp;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class ErrorLabelThread extends Thread{

    Label errorLabel;

    public ErrorLabelThread(Label errorLabel) {
        this.errorLabel = errorLabel;
    }

    @Override
    public void run() {
        try {
            sleep(5000);
            Platform.runLater(() -> errorLabel.setText(""));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}

package org.openjfx;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class LobbyController {

    @FXML
    private void switchToGame() throws IOException {
        App.setRoot("game");
    }

}

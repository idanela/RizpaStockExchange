package ui.main.stockExchangeApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.net.URL;

public class RizpaStockExchangeApplication extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Rizpa Stock Exchange System");
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("App.fxml");
        loader.setLocation(url);

        Parent root = loader.load(url.openStream());
        RizpaStockExchangeController controller = loader.getController();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

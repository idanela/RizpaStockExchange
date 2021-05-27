package uI.main.stockExchangeApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import stockExchangeEngine.StockExchangeEngine;


import java.net.URL;

public class RizpaStockExchangeApplication extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        StockExchangeEngine engine = new StockExchangeEngine();
        primaryStage.setTitle("Rizpa Stock Exchange System");
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("App.fxml");
        loader.setLocation(url);

        Parent root = loader.load(url.openStream());
        RizpaStockExchangeController controller = loader.getController();
        controller.setEngine(engine);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

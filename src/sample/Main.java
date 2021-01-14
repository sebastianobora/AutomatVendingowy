package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.dao.DataAccessObject;
import sample.model.DeviceResources;
import sample.util.Constants;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent mainRoot = FXMLLoader.load(getClass().getResource("resources/main.fxml"));

        Scene mainScene = new Scene(mainRoot);
        primaryStage.setTitle("Automat Vendingowy");
        //primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        DataAccessObject dao = DataAccessObject.getInstance();
        DeviceResources deviceResources = DeviceResources.getInstance();

        if(!deviceResources.getCoins().isEmpty() && !deviceResources.getProducts().isEmpty()){
            dao.saveData(dao.CSVCoinParser(deviceResources.getCoins()), Constants.COINS_PATH);
            dao.saveData(dao.CSVProductParser(deviceResources.getProducts()), Constants.PRODUCTS_PATH);
        }
    }
}

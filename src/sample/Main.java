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

    private static Stage MainStage;

    public static Stage getMainStage() {
        return MainStage;
    }

    private void setMainStage(Stage primaryStage) {
        Main.MainStage = primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        setMainStage(primaryStage);
        MainStage.setTitle("Automat Vendingowy");

        Parent mainRoot = FXMLLoader.load(getClass().getResource("resources/main.fxml"));

        Scene mainScene = new Scene(mainRoot);
        MainStage.initStyle(StageStyle.DECORATED);
        MainStage.setScene(mainScene);
        MainStage.setResizable(false);
        MainStage.show();
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

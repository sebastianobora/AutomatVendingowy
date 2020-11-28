package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.GridPane;
import sample.model.DeviceResources;
import sample.model.Product;
import sample.util.Constants;

import java.io.File;
import java.util.*;

public class MainController {
    DataController dataController;
    DeviceResources deviceResources;

    @FXML
    private GridPane gridPaneItems;

    @FXML
    private Label topScreen;


    public void addItemsToGrid(ArrayList<Product> products){
        products.sort(Product::compareTo);
        Iterator<Product> listIterator = products.listIterator();
        Product nextItem;
        Label label;
        ImageView imageView;
        Image image;

        for(int i = 0; i < 18; i+=3){
            for(int j = 0; j < 4; j++){
                    if(!listIterator.hasNext()) return;
                    nextItem = listIterator.next();

                    imageView = new ImageView();
                    image = getImage(nextItem.getImgPath());

                    imageView.setImage(image);
                    imageView.setFitHeight(80);
                    imageView.setFitWidth(60);
                    gridPaneItems.add(imageView, j, i);

                    label = new Label();
                    label.setText(String.valueOf(nextItem.getSlot()));
                    gridPaneItems.add(label, j, i+1);

                    label = new Label();
                    label.setText(String.valueOf(nextItem.getPrice()) + String.valueOf(Constants.CURRENCY));
                    gridPaneItems.add(label, j, i+2);
            }
        }
    }


    public void keyboardClickInsertTopScreen(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        String number = button.getText();
        topScreen.setText(topScreen.getText() + number);
    }

    public Image getImage(String imgPath) {
        File file = new File(imgPath);
        return new Image(file.toURI().toString());
    }

    public void clearTopScreen() {
        this.dataController = new DataController();

        this.deviceResources = new DeviceResources(
                dataController.getData(Constants.PRODUCTS_PATH),
                dataController.getData(Constants.COINS_PATH)
        );
        addItemsToGrid(this.deviceResources.getProducts());
        topScreen.setText("");
    }
}

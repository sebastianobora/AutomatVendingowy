package sample.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import sample.model.Coin;
import sample.model.DeviceResources;
import sample.model.Product;
import sample.util.Constants;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    DataController dataController;
    DeviceResources deviceResources;
    ArrayList<Coin> tempCoins;
    double tempValue;

    boolean productSelected;

    @FXML
    private GridPane gridPaneItems;

    @FXML
    private Label topScreen, middleScreen;

    @FXML
    private Label leftBottomScreen, rightBottomScreen, centerBottomScreen;

    @FXML
    private Label rightBottomScreenCurrency, leftBottomScreenCurrency, centerBottomScreenCurrency;

    @FXML
    private MenuButton coinButton;
    
    @FXML
    private Button takeChangeButton;

    @FXML
    private ImageView takeProductButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addItemsToGrid();
        addCoinButton();
        cancel();
    }

    public MainController() {
        this.dataController = new DataController();
        this.deviceResources = new DeviceResources(
                dataController.getData(Constants.PRODUCTS_PATH),
                dataController.getData(Constants.COINS_PATH)
        );
        tempCoins = new ArrayList<>();
        for(Coin coin : deviceResources.getCoins()){
            tempCoins.add(coin.getCopy());
        }
    }

    public void addCoinButton(){
        ArrayList<MenuItem> choices = new ArrayList<>();

        for(Coin coin : tempCoins){
            choices.add(new MenuItem(coin.toString()));
        }

        coinButton.getItems().addAll(choices);


        for(MenuItem choice : choices){
            choice.setOnAction((childrenEvent) -> {
                if(productSelected){
                    if( tempValue < Constants.MAX_TEMP_VALUE){
                        coinButton.setText("Added"+ " " +choice.getText());
                        addCoin(choices.indexOf(choice));
                    }
                        else{
                            delayedMessageBottom("MAX","");
                    }
                }
            });
        }
    }

    public void addItemsToGrid(){
        Iterator<Product> listIterator = deviceResources.getProducts().listIterator();

        Product nextItem;
        Label label;
        ImageView imageView;
        Image image;

        for(int i = 0; i < Constants.ROWS * 3; i+=3){
            for(int j = 0; j < Constants.COLUMNS; j++){
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
                    label.setText(String.format("%.2f", nextItem.getPrice()) + " " + String.valueOf(Constants.CURRENCY));
                    gridPaneItems.add(label, j, i+2);
            }
        }
    }

    public void keyboardClickInsertTopScreen(MouseEvent mouseEvent) {
        if(!productSelected){
            Button button = (Button) mouseEvent.getSource();
            String number = button.getText();
            topScreen.setText(topScreen.getText() + number);
        }
    }

    public Image getImage(String imgPath) {
        File file = new File(imgPath);
        return new Image(file.toURI().toString());
    }

    public void cancel(){
        tempValue = 0.00;
        productSelected = false;
        topScreen.setText("");
        middleScreen.setText("Choose item");
        leftBottomScreen.setText("");
        rightBottomScreen.setText("");
        leftBottomScreenCurrency.setText("");
        rightBottomScreenCurrency.setText("");
        coinButton.setText("Coins");
    }

    public void delayedCancel(){
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(runCancel -> cancel());
        pause.play();
    }

    public void delayedMessageBottom(String top, String down){
        centerBottomScreen.setText(top);
        centerBottomScreenCurrency.setText(down);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(event -> clearAddCoin());
        pause.play();
    }

    public void delayedMessageTopMid(String top, String down){
        topScreen.setText(top);
        middleScreen.setText(down);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.8));
        pause.setOnFinished(event -> clearAddCoin());
        pause.play();
    }



    public void clearAddCoin(){
        centerBottomScreen.setText("");
        centerBottomScreenCurrency.setText("");
    }

    public void checkProduct(MouseEvent mouseEvent) {
        // "OK" button
        if(!productSelected) {
            productSelected = true;
            if (//TODO: eksport do metody ten if
                    !topScreen.getText().equals("") &&
                            Integer.parseInt(topScreen.getText()) <= Constants.MAX_SLOT &&
                            Integer.parseInt(topScreen.getText()) >= Constants.MIN_SLOT
            ) {
                Integer index = deviceResources.findProductBySlot(topScreen.getText());
                if (index != null && deviceResources.getProducts().get(index).isAvailable()) {
                        Product currProduct = deviceResources.getProducts().get(index);

                        middleScreen.setText(currProduct.getName());
                        leftBottomScreen.setText(String.format("%.2f", currProduct.getPrice()));
                        leftBottomScreenCurrency.setText(Constants.CURRENCY);
                        rightBottomScreen.setText(String.format("%.2f", tempValue));
                        rightBottomScreenCurrency.setText(Constants.CURRENCY);
                    } else {
                    topScreen.setText("Product");
                    middleScreen.setText("unavailable");
                    delayedCancel();
                }
            } else {
                middleScreen.setText("Wrong product");
                delayedCancel();
                }
        }else{
            tryPurchase();
        }
    }

    public void addCoin(int index){
        tempValue+=tempCoins.get(index).getNominal();
        tempValue = Math.round(tempValue * 100d) / 100d;

        rightBottomScreen.setText(String.format("%.2f", tempValue));
        tempCoins.get(index).incrementQuantity();
    }

    public void tryPurchase(){
        Integer productIndex = deviceResources.findProductBySlot(topScreen.getText());
        double change = tempValue - deviceResources.getProducts().get(productIndex).getPrice();
        change = Math.round(change * 100d) / 100d;

        if(change >= 0){
            if(changePossible(change)){
                ArrayList<Coin> changeCoins = new ArrayList<>();
                changeCoins = getChange(change);

                topScreen.setText("Transaction");
                middleScreen.setText("complete");
                delayedMessageTopMid("Take", "product&change");

                Image image = getImage(deviceResources.getProducts().get(productIndex).getImgPath());
                takeProductButton.setImage(image);
            }else{
                topScreen.setText("Can't give");
                middleScreen.setText("change");
            }
            delayedCancel();
        }else{
            delayedMessageBottom("ADD", "COINS");
        }
    }

    public boolean changePossible(double change){
        for(int i = tempCoins.size() - 1; i>=0 ; i--){
            if(tempCoins.get(i).getQuantity() > 0 && change/tempCoins.get(i).getNominal() >= 1){
                change = Math.round(change * 100d) / 100d;
                change-=tempCoins.get(i).getNominal()*Math.floor(change/tempCoins.get(i).getNominal());
            }
        }
        return change == 0;
    }

    public ArrayList<Coin> getChange(double change){
        ArrayList<Coin> changeCoins = new ArrayList<>();

        for(int i = tempCoins.size() - 1; i>=0 ; i--){
            if(tempCoins.get(i).getQuantity() > 0 && change/tempCoins.get(i).getNominal() >= 1){
                changeCoins.add(new Coin(tempCoins.get(i).getNominal(), (int)(Math.floor(change/tempCoins.get(i).getNominal()))));
                change-= tempCoins.get(i).getNominal()*Math.floor(change/tempCoins.get(i).getNominal());
                tempCoins.get(i).decrementQuantity((int)(Math.floor(change/tempCoins.get(i).getNominal())));
            }
        }
        return changeCoins;
    }

    public void takeProduct(MouseEvent mouseEvent) {
        takeProductButton.setImage(null);
    }

    public void takeChange(MouseEvent mouseEvent) {
    }
}

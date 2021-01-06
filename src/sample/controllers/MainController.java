package sample.controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sample.Main;
import sample.dao.DataAccessObject;
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
    private static DeviceResources deviceResources;

    private double coinsValue;
    private boolean keyboardBlocked, productSelected, productTaken, withdrawTaken, error;

    private Stage popUp;

    @FXML
    private Pane mainPane;

    @FXML
    private GridPane gridPaneItems;

    @FXML
    private Label topScreen, middleScreen, leftBottomScreen, rightBottomScreen, middleBottomScreen,
            rightBottomScreenFloor, leftBottomScreenFloor, middleBottomScreenFloor;

    @FXML
    private MenuButton addCoinButton;

    @FXML
    private ImageView takeWithdrawButton, takeProductButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DataAccessObject dao = DataAccessObject.getInstance();
        deviceResources = DeviceResources.getInstance();

        try{
            deviceResources.setProducts(dao.loadData(Constants.PRODUCTS_PATH));
            deviceResources.setCoins(dao.loadData(Constants.COINS_PATH));
        }catch(Exception e){
            error = true;
            keyboardBlocked = true;
            middleScreen.setText(e.getMessage());
        }

        coinsValue = 0.0;
        productTaken = true;
        withdrawTaken = true;
        addItemsToSlots(deviceResources.getProducts());
        setAddCoinButton();
        takeProductButton.setOnMouseClicked(null);
        takeWithdrawButton.setOnMouseClicked(null);
        if(!error){
            cancel();
        }
    }

    public void setAddCoinButton() {
        ArrayList<MenuItem> choices = new ArrayList<>();

        for (Coin coin : deviceResources.getCoins()) {
            choices.add(new MenuItem(coin.toString()));
        }

        addCoinButton.getItems().addAll(choices);


        for (MenuItem choice : choices) {
            choice.setOnAction((childrenEvent) -> {
                if (productSelected) {
                    if (coinsValue < Constants.MAX_TEMP_VALUE) {
                        addCoinButton.setText("Added" + " " + choice.getText());
                        addCoin(choices.indexOf(choice));
                    } else {
                        middleBottomScreen.setText("MAX");
                        middleBottomScreenFloor.setText("");
                        delayedMethod(0.5, e -> clearMidBottomScreens());
                    }
                }
            });
        }
    }

    public void addItemsToSlots(ArrayList<Product> products) {
        Iterator<Product> listIterator = products.listIterator();
        Product nextItem;
        Label label;
        ImageView imageView;
        Image image;

        for (int i = 0; i < Constants.ROWS * 3; i += 3) {
            for (int j = 0; j < Constants.COLUMNS; j++) {
                if (!listIterator.hasNext()) return;
                nextItem = listIterator.next();

                imageView = new ImageView();
                image = new Image(new File(nextItem.getImgPath()).toURI().toString());

                imageView.setImage(image);
                imageView.setFitHeight(80);
                imageView.setFitWidth(60);
                gridPaneItems.add(imageView, j, i);

                label = new Label();
                label.setFont(Font.font("Arial"));
                label.setText(String.valueOf(nextItem.getSlot()));
                gridPaneItems.add(label, j, i + 1);

                label = new Label();
                label.setFont(Font.font("Arial"));
                label.setText(String.format("%.2f", nextItem.getPrice()) + " " + Constants.CURRENCY);
                gridPaneItems.add(label, j, i + 2);
            }
        }
    }

    public void keyboardClickToScreen(MouseEvent mouseEvent) {
        if (!keyboardBlocked) {
            if (productTaken && withdrawTaken) {
                if (!productSelected) {
                    Button button = (Button) mouseEvent.getSource();
                    String number = button.getText();
                    topScreen.setText(topScreen.getText() + number);
                }
            } else {
                middleBottomScreen.setText("take");
                middleBottomScreenFloor.setText("items");
                delayedMethod(0.5, e-> clearMidBottomScreens());
            }
        }
    }

    public void cancel() {
        if(!error){
            if (coinsValue > 0) {
                ArrayList<Coin> withdrawCoins = deviceResources.getWithdraw(coinsValue);
                coinsValue = 0;
                setTakeWithdrawButton();
                setWithdrawPopUp(withdrawCoins);
            }
            keyboardBlocked = false;
            productSelected = false;
            topScreen.setText("");
            middleScreen.setText("Choose item");
            leftBottomScreen.setText("");
            rightBottomScreen.setText("");
            leftBottomScreenFloor.setText("");
            rightBottomScreenFloor.setText("");
            addCoinButton.setText("Coins");
        }
    }

    public void delayedMethod(double time, EventHandler<ActionEvent> e) {
        PauseTransition pause = new PauseTransition(Duration.seconds(time));
        pause.setOnFinished(e);
        pause.play();
    }

    public void clearMidBottomScreens(){
        middleBottomScreen.setText("");
        middleBottomScreenFloor.setText("");
    }

    public void OKButton() {
        if (withdrawTaken && productTaken) {
            if (!keyboardBlocked) {
                if (!topScreen.getText().equals("") &&
                        Integer.parseInt(topScreen.getText()) <= Constants.MAX_SLOT &&
                        Integer.parseInt(topScreen.getText()) >= Constants.MIN_SLOT) {
                    Product currProduct = deviceResources.findProductBySlot(topScreen.getText());
                    if (!productSelected) {
                        productSelected = true;
                        if (currProduct != null && currProduct.isAvailable()) {
                            middleScreen.setText(currProduct.getName());
                            leftBottomScreen.setText(String.format("%.2f", currProduct.getPrice()));
                            leftBottomScreenFloor.setText(Constants.CURRENCY);
                            rightBottomScreen.setText(String.format("%.2f", coinsValue));
                            rightBottomScreenFloor.setText(Constants.CURRENCY);
                        } else {
                            topScreen.setText("Product");
                            if(currProduct == null){
                                middleScreen.setText("not exist");
                            }else{
                                middleScreen.setText("unavailable");
                            }
                            keyboardBlocked = true;
                            delayedMethod(1.5, e -> cancel());
                        }
                    } else {
                        purchase(currProduct);
                    }
                } else {
                    middleScreen.setText("Wrong product");
                    keyboardBlocked = true;
                    delayedMethod(1.5, e->cancel());
                }
            }
        } else {
            middleBottomScreen.setText("take");
            middleBottomScreenFloor.setText("items");
            delayedMethod(0.5, e -> clearMidBottomScreens());
        }
    }

    public void addCoin(int index) {
        coinsValue += deviceResources.getCoins().get(index).getNominal();
        coinsValue = Math.round(coinsValue * 100d) / 100d;

        rightBottomScreen.setText(String.format("%.2f", coinsValue));
        deviceResources.getCoins().get(index).incrementQuantity();
    }

    public void purchase(Product currProduct) {
        double change = coinsValue - currProduct.getPrice();
        change = Math.round(change * 100d) / 100d;

        if (change >= 0) {
            productSelected = false;
            if (deviceResources.isWithdrawPossible(change)) {
                withdrawTaken = false;
                productTaken = false;

                ArrayList<Coin> withdrawCoins = deviceResources.getWithdraw(change);
                topScreen.setText("Take");
                middleScreen.setText("product&change");

                setTakeProductButton(currProduct);
                if (change > 0) {
                    setTakeWithdrawButton();
                    setWithdrawPopUp(withdrawCoins);
                } else {
                    withdrawTaken = true;
                }
            } else {
                ArrayList<Coin> withdrawCoins = deviceResources.getWithdraw(coinsValue);
                setTakeWithdrawButton();
                setWithdrawPopUp(withdrawCoins);
                topScreen.setText("Can't give");
                middleScreen.setText("change");
            }
            coinsValue = 0;
            delayedMethod(1.5, e->cancel());
        } else {
            middleBottomScreen.setText("add");
            middleBottomScreenFloor.setText("coins");
            delayedMethod(0.5, e-> clearMidBottomScreens());
        }
    }

    public void takeProduct() {
        productTaken = true;
        takeProductButton.setCursor(Cursor.DEFAULT);
        takeProductButton.setImage(null);
        takeProductButton.setOnMouseClicked(null);
    }

    public void takeWithdraw() {
        withdrawTaken = true;
        takeWithdrawButton.setCursor(Cursor.DEFAULT);
        takeWithdrawButton.setImage(null);
        takeWithdrawButton.setOnMouseClicked(null);
        popUp.show();
    }

    public void setTakeProductButton(Product currProduct) {
        Image image = new Image(new File(currProduct.getImgPath()).toURI().toString());
        takeProductButton.setOnMouseClicked(e->{
            takeProduct();
            currProduct.decrementQuantity();
        });
        takeProductButton.setCursor(Cursor.OPEN_HAND);
        takeProductButton.setImage(image);
    }

    public void setTakeWithdrawButton() {
        Image image = new Image(new File(Coin.getImgPath()).toURI().toString());
        takeWithdrawButton.setOnMouseClicked(e-> takeWithdraw());
        takeWithdrawButton.setCursor(Cursor.OPEN_HAND);
        takeWithdrawButton.setImage(image);
    }

    public void setWithdrawPopUp(ArrayList<Coin> withdrawCoins) {
        Label coins = new Label();
        for (Coin coin : withdrawCoins) {
            coins.setText(coins.getText() + coin.toString() + " : " + coin.getQuantity() + "\n");
        }

        popUp = new Stage();
        popUp.setTitle("Withdraw");

        StackPane stackPane = new StackPane();
        stackPane.setPrefSize(135, 135);
        stackPane.getChildren().add(coins);
        StackPane.setAlignment(coins, Pos.TOP_CENTER);

        Scene popupScene = new Scene(stackPane);

        popUp.setResizable(false);
        popUp.setScene(popupScene);
        popUp.initStyle(StageStyle.DECORATED);
        popUp.initOwner(mainPane.getScene().getWindow());
        //popUp.setX(mainPane.getScene().getWindow().getX() + 150);
        //popUp.setY(mainPane.getScene().getWindow().getY() + 200);
    }
}
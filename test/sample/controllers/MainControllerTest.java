package sample.controllers;


import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import sample.Main;
import sample.model.DeviceResources;
import sample.model.Product;

import java.util.concurrent.TimeoutException;

@ExtendWith(ApplicationExtension.class)
class MainControllerTest{

    private final String okButton = "OK";
    private final String addCoinButton = "#addCoinButton";
    private final String middleScreen = "#middleScreen";
    private final String rightBottomScreen = "#rightBottomScreen";
    private final String topScreen = "#topScreen";
    private final String middleBottomScreen = "#middleBottomScreen";
    private final String middleBottomScreenFloor = "#middleBottomScreenFloor";

    @Start
    private void start(Stage primaryStage) throws Exception {
        Main main = new Main();
        main.start(primaryStage);
    }

    @AfterEach
    void afterEach() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    @Test
    void noChooseProductOk(FxRobot robot) {
        robot.clickOn(LabeledMatchers.hasText(okButton));
        FxAssert.verifyThat(middleScreen, LabeledMatchers.hasText("Wrong product"));
    }

    @Test
    void addCoin(FxRobot robot) {
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(1)));
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(2)));
        robot.clickOn(LabeledMatchers.hasText(okButton));
        robot.clickOn(addCoinButton);
        robot.clickOn(LabeledMatchers.hasText("5,00 PLN"));
        FxAssert.verifyThat(rightBottomScreen, LabeledMatchers.hasText("5,00"));
    }

    @Test
    void cancelAfterAddCoin(FxRobot robot){
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(1)));
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(2)));
        robot.clickOn(LabeledMatchers.hasText(okButton));
        robot.clickOn(addCoinButton);
        robot.clickOn(LabeledMatchers.hasText("5,00 PLN"));
        FxAssert.verifyThat(rightBottomScreen, LabeledMatchers.hasText("5,00"));
    }

    @Test
    void selectedProductShownProper(FxRobot robot){
        DeviceResources deviceResources = DeviceResources.getInstance();
        Product product = deviceResources.findProductBySlot("12");

        robot.clickOn(LabeledMatchers.hasText(String.valueOf(1)));
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(2)));
        robot.clickOn(LabeledMatchers.hasText(okButton));
        FxAssert.verifyThat(topScreen, LabeledMatchers.hasText(product.getSlot()));
    }

    @Test
    void selectedOkWithoutAddedCoins(FxRobot robot){
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(1)));
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(2)));
        robot.clickOn(LabeledMatchers.hasText(okButton));
        robot.clickOn(LabeledMatchers.hasText(okButton));
        FxAssert.verifyThat(middleBottomScreen, LabeledMatchers.hasText("add"));
        FxAssert.verifyThat(middleBottomScreenFloor, LabeledMatchers.hasText("coins"));
    }

    @Test
    void notEnoughCoins(FxRobot robot){
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(1)));
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(2)));
        robot.clickOn(LabeledMatchers.hasText(okButton));
        robot.clickOn(addCoinButton);
        robot.clickOn(LabeledMatchers.hasText("1,00 PLN"));
        robot.clickOn(LabeledMatchers.hasText(okButton));
        FxAssert.verifyThat(middleBottomScreen, LabeledMatchers.hasText("add"));
        FxAssert.verifyThat(middleBottomScreenFloor, LabeledMatchers.hasText("coins"));
    }

    @Test
    void keyboardCheck(FxRobot robot) {
        for(int i = 0; i <= 9; i++){
            robot.clickOn(LabeledMatchers.hasText(String.valueOf(i)));
            FxAssert.verifyThat(topScreen, LabeledMatchers.hasText(String.valueOf(i)));
            Platform.runLater(
                    () -> robot.lookup(topScreen).queryLabeled().setText("")
            );
        }
    }
}
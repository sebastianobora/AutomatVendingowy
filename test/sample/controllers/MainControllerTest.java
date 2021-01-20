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

    private final String OK_BUTTON = "OK";
    private final String ADD_COIN_BUTTON = "#addCoinButton";
    private final String MIDDLE_SCREEN = "#middleScreen";
    private final String RIGHT_BOTTOM_SCREEN = "#rightBottomScreen";
    private final String TOP_SCREEN = "#topScreen";
    private final String MIDDLE_BOTTOM_SCREEN = "#middleBottomScreen";
    private final String MIDDLE_BOTTOM_SCREEN_FLOOR = "#middleBottomScreenFloor";

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
        robot.clickOn(LabeledMatchers.hasText(OK_BUTTON));
        FxAssert.verifyThat(MIDDLE_SCREEN, LabeledMatchers.hasText("Wrong product"));
    }

    @Test
    void addCoin(FxRobot robot) {
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(1)));
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(2)));
        robot.clickOn(LabeledMatchers.hasText(OK_BUTTON));
        robot.clickOn(ADD_COIN_BUTTON);
        robot.clickOn(LabeledMatchers.hasText("5,00 PLN"));
        FxAssert.verifyThat(RIGHT_BOTTOM_SCREEN, LabeledMatchers.hasText("5,00"));
    }

    @Test
    void cancelAfterAddCoin(FxRobot robot){
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(1)));
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(2)));
        robot.clickOn(LabeledMatchers.hasText(OK_BUTTON));
        robot.clickOn(ADD_COIN_BUTTON);
        robot.clickOn(LabeledMatchers.hasText("5,00 PLN"));
        FxAssert.verifyThat(RIGHT_BOTTOM_SCREEN, LabeledMatchers.hasText("5,00"));
    }

    @Test
    void selectedProductShownProper(FxRobot robot){
        DeviceResources deviceResources = DeviceResources.getInstance();
        Product product = deviceResources.findProductBySlot("12");

        robot.clickOn(LabeledMatchers.hasText(String.valueOf(1)));
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(2)));
        robot.clickOn(LabeledMatchers.hasText(OK_BUTTON));
        FxAssert.verifyThat(TOP_SCREEN, LabeledMatchers.hasText(product.getSlot()));
    }

    @Test
    void selectedOkWithoutAddedCoins(FxRobot robot){
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(1)));
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(2)));
        robot.clickOn(LabeledMatchers.hasText(OK_BUTTON));
        robot.clickOn(LabeledMatchers.hasText(OK_BUTTON));
        FxAssert.verifyThat(MIDDLE_BOTTOM_SCREEN, LabeledMatchers.hasText("add"));
        FxAssert.verifyThat(MIDDLE_BOTTOM_SCREEN_FLOOR, LabeledMatchers.hasText("coins"));
    }

    @Test
    void notEnoughCoins(FxRobot robot){
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(1)));
        robot.clickOn(LabeledMatchers.hasText(String.valueOf(2)));
        robot.clickOn(LabeledMatchers.hasText(OK_BUTTON));
        robot.clickOn(ADD_COIN_BUTTON);
        robot.clickOn(LabeledMatchers.hasText("1,00 PLN"));
        robot.clickOn(LabeledMatchers.hasText(OK_BUTTON));
        FxAssert.verifyThat(MIDDLE_BOTTOM_SCREEN, LabeledMatchers.hasText("add"));
        FxAssert.verifyThat(MIDDLE_BOTTOM_SCREEN_FLOOR, LabeledMatchers.hasText("coins"));
    }

    @Test
    void keyboardCheck(FxRobot robot) {
        for(int i = 0; i <= 9; i++){
            robot.clickOn(LabeledMatchers.hasText(String.valueOf(i)));
            FxAssert.verifyThat(TOP_SCREEN, LabeledMatchers.hasText(String.valueOf(i)));
            Platform.runLater(
                    () -> robot.lookup(TOP_SCREEN).queryLabeled().setText("")
            );
        }
    }
}
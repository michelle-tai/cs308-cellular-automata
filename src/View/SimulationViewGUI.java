package View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.util.Duration;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;
import xml.SimulationXMLFileChooser;
import xml.simulationXML;

public class SimulationViewGUI {

    private static final double WIDTH = 1000;
    private static final double HEIGHT = 1024;
    private final static int SUBSCENE_WIDTH = 800;
    private final static int SUBSCENE_HEIGHT = 600;

    private Stage simulationViewStage;
    private Scene simulationViewScene;
    private AnchorPane simulationViewPane;
    private SimulationViewSubscene mySubscene;

    private static final String RESOURCES = "resources";
    public static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES + ".";
    public static final String DEFAULT_RESOURCE_FOLDER = "/" + RESOURCES + "/";
    public static final String BLANK = " ";

    private  String font ;
    private SimulationViewInfoLabel simulationViewLabel;
    private SimulationViewButton mySimulationStartButton;
    private SimulationViewButton mySimulationStopButton;
    private SimulationViewButton mySimulationStepButton, mySimulationContinueButton;
    private SimulationViewButton mySimulationLoadNewFileButton;
    private ResourceBundle myResources;
    private String language;
    private boolean stepboolean = false;
    private double stepUpdateCount ;
    private simulationXML simulationXMLInfo;


    /**
     * Create a view of the given model of a web browser with prompts in the given language.
     */
    public SimulationViewGUI(String language) throws FileNotFoundException {

        myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + language);
        font = myResources.getString("FontStylePath");
        language = "English";
        setGameScene();
        createBackgroundImage();
        createSubScene();
        createSimulationPane();
    }

    private void makeTopButtons() {
        HBox boxWIthButtons = new HBox(10);
        boxWIthButtons.setPrefWidth(WIDTH);
        boxWIthButtons.setPrefHeight(50);
        boxWIthButtons.setLayoutY(10);
        boxWIthButtons.setLayoutY(10);
        mySimulationStartButton = makeButton("StartCommand", event -> startSimulation());
        boxWIthButtons.getChildren().add(mySimulationStartButton);

        mySimulationStopButton = makeButton("StopCommand", event -> stopSimulation());
        boxWIthButtons.getChildren().add(mySimulationStopButton);

        mySimulationContinueButton = makeButton("ContinueCommand", event -> continueSimulation());
        boxWIthButtons.getChildren().add(mySimulationContinueButton);

        mySimulationStepButton = makeButton("StepCommand", event -> stepThroughSimulation());
        boxWIthButtons.getChildren().add(mySimulationStepButton);

        mySimulationLoadNewFileButton = makeButton("LoadFileCommand", event -> {
            try {
                loadFile();
                System.out.println("file loaded");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        boxWIthButtons.getChildren().add(mySimulationLoadNewFileButton);


        simulationViewPane.getChildren().add(boxWIthButtons);
    }

    private void makeBottomLabelScene() throws FileNotFoundException {
        HBox labelAtBottom = new HBox();
        labelAtBottom.setPrefHeight(50);
        labelAtBottom.setPrefWidth(WIDTH);
        labelAtBottom.setLayoutY(HEIGHT - 268);
        labelAtBottom.setLayoutX(0);
        simulationViewLabel = new SimulationViewInfoLabel(myResources.getString("WelcomeMessage"), (int) WIDTH, 50);
        simulationViewLabel.setFont((Font.loadFont(new FileInputStream(new File(font)), 23)));
        labelAtBottom.setAlignment(Pos.CENTER);
        labelAtBottom.getChildren().addAll(simulationViewLabel);
        simulationViewPane.getChildren().add(labelAtBottom);
    }

    private void startSimulation() {
        mySubscene.start(simulationXMLInfo);
    }

    private void stopSimulation() {
        mySubscene.animation.stop();
    }

    private void continueSimulation(){
        mySubscene.animation.play();
    }

    private void stepThroughSimulation() {
        if(! stepboolean){
            mySubscene.animation.stop();
            stepboolean = true;
            stepUpdateCount = mySubscene.getCurrTime();
        }
        else {
            boolean startAnimation = true;
            stepUpdateCount = mySubscene.getCurrTime();
//            mySubscene.animation.play();
//            mySubscene.animation.setDelay(new Duration(10000));
//            while(stepUpdateCount < stepUpdateCount + 5){
//                stepUpdateCount++;
//                if(startAnimation){
//                    mySubscene.animation.play();
//                    startAnimation = false;
//                }
//                else {
//                    continue;
//                }
//            }
//            System.out.println(".");
            mySubscene.animation.stop();
//            if (mySubscene.getCurrTime() - stepUpdateCount <= 1){
//                mySubscene.animation.stop();
//            }
//            stepUpdateCount = mySubscene.getCurrTime();
//            mySubscene.getCurrTime();

            //mySubscene.animation.stop();
        }
    }

    private void loadFile() throws Exception {
        // TO DO: Michelle add xml stuff
        SimulationXMLFileChooser fileChooser = new SimulationXMLFileChooser();
        fileChooser.openFile(simulationViewStage);
        simulationXMLInfo = fileChooser.getSimulationXMLInfo();
        simulationViewLabel.setText(myResources.getString("SimulationTitle") + " " + simulationXMLInfo.getTitle());
    }

    // Display given message as an error in the GUI
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(myResources.getString("ErrorTitle"));
        alert.setContentText(message);
        alert.showAndWait();
    }

    private SimulationViewButton makeButton(String property, EventHandler<ActionEvent> handler) {

        SimulationViewButton result = new SimulationViewButton(myResources.getString(property), language);
        String label = myResources.getString(property);

        final String IMAGEFILE_SUFFIXES = String.format(".*\\.(%s)", String.join("|", ImageIO.getReaderFileSuffixes()));
        if (label.matches(IMAGEFILE_SUFFIXES)) {
            result.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(DEFAULT_RESOURCE_FOLDER + label))));
        } else {
            result.setText(label);
        }
        result.setOnAction(handler);

        return result;
    }

    /**
     * method to obtain the stage of the view
     * @return baseStage
     */
    public Stage getSimulationViewStage() {
        return simulationViewStage;
    }

    private void setGameScene() throws FileNotFoundException {
        simulationViewPane = new AnchorPane();
        simulationViewScene = new Scene(simulationViewPane, WIDTH, HEIGHT);
        simulationViewStage = new Stage();
        simulationViewStage.setScene(simulationViewScene);
        simulationViewStage.setResizable(false);
    }

    private void createBackgroundImage(){
        Image backgroundImage = new Image(myResources.getString("SimulationBackground"), false);
        BackgroundImage simulationViewBackground;
        simulationViewBackground = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        simulationViewPane.setBackground(new Background(simulationViewBackground));
    }

    private void createSimulationPane() throws FileNotFoundException {
        makeBottomLabelScene();
        makeTopButtons();
    }

    private void createSubScene(){
        mySubscene = new SimulationViewSubscene(SUBSCENE_WIDTH, SUBSCENE_HEIGHT);
        simulationViewPane.getChildren().add(mySubscene);
    }

}




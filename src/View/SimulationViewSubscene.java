package View;

import Cells.RectCell;
import IndividualSimulations.Fire;
import IndividualSimulations.GoL;
import cellsociety.Cell;
import cellsociety.Simulation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
//import java.util.Random;
import xml.simulationXML;

public class SimulationViewSubscene extends SubScene{

    private final static String SUBSCENE_BACKGROUND_IMAGE = "resources/blue_background_for_popup.png";
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private static final String RESOURCES = "resources";
    public static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES + ".";
    private ResourceBundle myResources;
    private Simulation HardCodeSimulation;
    public Timeline animation;
    private AnchorPane mySubscenePane;
    private simulationXML simInfo;
    private Simulation XMLSimulation;

    public SimulationViewSubscene(int width, int height, simulationXML simXML) {
        super(new AnchorPane(), width, height);
        simInfo = simXML;
        prefWidth(width);
        prefHeight(height);
        setBackground();
        subsceneLayout();
        beginAnimation();
    }

    private void subsceneLayout(){
        setLayoutX(100);
        setLayoutY(124);
    }

    private void setBackground(){
        Image backgroundImage = new Image(myResources.getString("SubImage"), false);
        BackgroundImage subsceneViewBackground;
        subsceneViewBackground = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        //AnchorPane subroot = (AnchorPane) this.getRoot();
        mySubscenePane = (AnchorPane) this.getRoot();
        //subroot.setBackground(new Background(subsceneViewBackground));
        mySubscenePane.setBackground(new Background(subsceneViewBackground));
    }

    /**
     * Public method to enable easy acces to the subcene pane
     * @return the pane of the subscene
     */

    public AnchorPane getPane() {
        return (AnchorPane) this.getRoot();
    }

    private void step(double secondDelay){

//        HardCodeSimulation.update(secondDelay,10);
//        // Check for fire simulation
//        if (HardCodeSimulation.checkToContinue()){
//            animation.stop();
//        }
//    }
//
//    public void start () {
//        //createHardCodedSimulation();
//        createHardCodedSimulationForFire();
//        HardCodeSimulation.update(secondDelay,10);
        XMLSimulation.update(secondDelay, 10);
        if (XMLSimulation.checkToContinue()){
            animation.stop();
        }
    }

    public void start () {
//        createHardCodedSimulation();
        createXMLSIM();
        animation.play();
    }


    public void createHardCodedSimulation(){
        List<List<Cell>> myListOfList = new ArrayList<>();
        int row = 100;
        int col = 100;
        int cellWidth = 800/row;
        int cellHeight = 600/col;
        for (int i = 0; i < row; i++){
            myListOfList.add(new ArrayList<Cell>());
            for (int j=0; j< col;j++){
                int status = (Math.random() <=0.5) ?0:1;
                Cell cell = new RectCell(i, j, cellWidth, cellHeight, status);
                myListOfList.get(i).add(cell);
                mySubscenePane.getChildren().add(cell.getCellImage());
            }
        }
        HardCodeSimulation = new GoL(myListOfList);
    }

//    public void createHardCodedSimulationForFire(){
//        List<List<Cell>> myListOfList = new ArrayList<>();
//        int row = 100;
//        int col = 100;
//        int a = 5;
//        int b = 3;
    public void createXMLSIM(){
        List<List<Cell>> myListOfList = new ArrayList<>();
        int row = simInfo.getHeight();
        int col = simInfo.getWidth();
        List<List<Integer>> initialConfig = simInfo.getInitialConfig();
        int cellWidth = 800/row;
        int cellHeight = 600/col;
        for (int i = 0; i < row; i++){
            myListOfList.add(new ArrayList<Cell>());
            for (int j=0; j< col;j++){
//                if (i == 0 || i == row-1 || j == 0 || j== col-1){
//                    int empty = 6;
//                    Cell cell = new RectCell(i, j, cellWidth, cellHeight, empty);
//                    myListOfList.get(i).add(cell);
//                    mySubscenePane.getChildren().add(cell.getCellImage());
//                }
//                else if(i == row/2 -1 && j == row/2 -1){
//                    Cell cell = new RectCell(i, j, cellWidth, cellHeight, b);
//                    myListOfList.get(i).add(cell);
//                    mySubscenePane.getChildren().add(cell.getCellImage());
//                }
//                else{
//                    //int state = a
//                    //int randomState = new Random().nextBoolean() ? a : b;
//                    Cell cell = new RectCell(i, j, cellWidth, cellHeight, a);
//                    myListOfList.get(i).add(cell);
//                    mySubscenePane.getChildren().add(cell.getCellImage());
//
//                }
//            }
//        }
//        HardCodeSimulation = new Fire(myListOfList);
//    }
//
//    private void setUpCell (int i, int j, int cellWidth, int cellHeight, int empty){
//        Cell cell = new RectCell(i, j, cellWidth, cellHeight, empty);
//        //myListOfList.get(i).add(cell);
//        mySubscenePane.getChildren().add(cell.getCellImage());
                Cell cell = new RectCell(i, j, cellWidth, cellHeight, initialConfig.get(i).get(j));
                myListOfList.get(i).add(cell);
                mySubscenePane.getChildren().add(cell.getCellImage());
            }
        }
        XMLSimulation = new GoL(myListOfList);
    }

    private void beginAnimation(){
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
    }
}

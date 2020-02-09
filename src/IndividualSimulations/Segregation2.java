package IndividualSimulations;

import Grids.RectGrid;
import cellsociety.Cell;
import cellsociety.Grid;
import cellsociety.Simulation;
import javafx.scene.layout.AnchorPane;

import java.io.FileNotFoundException;
import java.util.*;

public class Segregation2 extends Simulation {
    private int BLUE = 2;
    private int RED = 3;
    private int BLANK = 4;
    ArrayList<int[]> unsatisfied = new ArrayList<>();
    ArrayList<Integer> unusedCell = new ArrayList<>();
    //private Grid grid;

    private double satisfyRate;

    public Segregation2(int row, int col, int neighbourNumber, AnchorPane pane, double satisfied) throws FileNotFoundException {
        super(new ArrayList<>());
        grid = new RectGrid(row, col, neighbourNumber, false);
        grid.iniState(new int[]{BLUE, BLANK, RED});
        createIndices(row, col);

        grid.addToPane(pane);
        setSatisfyRate(satisfied);
    }


    private void setSatisfyRate(double satisfyRate) {
        this.satisfyRate = satisfyRate;
    }


    @Override
    public HashMap<String, Integer> frequency() {
        HashMap<String, Integer>ret = new HashMap<>();
        ret.put("RED", grid.getFreq(RED));
        ret.put("BLUE", grid.getFreq(BLUE));
        ret.put("BLANK", grid.getFreq(BLANK));
        return ret;
    }

    public void updateGrid() {
        unsatisfied = new ArrayList<>();
        for (int[] index : indices) {
            ArrayList<Integer> neighbours = grid.neighbourStatus(index);
            int next = checkSatisfy(grid.getCell(index), neighbours);
            if (next==100) unsatisfied.add(index);
            grid.changeNext(index, next);
        }
        //Collections.shuffle(unsatisfied);
        Collections.shuffle(unusedCell);
        for (int[] index2 : unsatisfied) {
            randPlace(index2);
        }
        grid.updateAll();
    }


    private int checkSatisfy(int curCell, ArrayList<Integer> neighbours) {
        if (curCell == BLANK) {
            unusedCell.add(BLANK);
            return 100;
        }
        int same = 0;
        int nonBlank = 0;
        for (int neighbour : neighbours) {
            if (neighbour == curCell) same++;
            if (neighbour != BLANK) nonBlank++;
        }
        double satisfiedRate = 0;
        if (nonBlank > 0) satisfiedRate = (double) same / nonBlank;
        if (satisfiedRate >= satisfyRate) return curCell;

        unusedCell.add(curCell);
        return 100;

    }

    private void randPlace(int[] index) {
        grid.changeNext(index, unusedCell.get(0));
        unusedCell.remove(0);
    }

    @Override
    public void checkNeighbourAndChangeNext(Cell cell, List<Cell> neighbours) {

    }


}

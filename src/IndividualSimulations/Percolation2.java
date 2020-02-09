package IndividualSimulations;

import Grids.RectGrid;
import cellsociety.Cell;
import cellsociety.Grid;
import cellsociety.Simulation;
import javafx.scene.layout.AnchorPane;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Percolation class holds the rules and update behavior for the percolation simulation
 *
 * @author Michelle Tai
 **/
public class Percolation2 extends Simulation {
  private int OPEN = 0;
  private int CLOSED = 1;
  private int PERCOLATED = 2;

  //private Grid grid;


  public Percolation2(int row, int col, int neighbourNumber, AnchorPane pane) throws FileNotFoundException {
      super(new ArrayList<>());
      grid = new RectGrid(row, col, neighbourNumber, false);
      grid.iniState(new int[]{OPEN, CLOSED});

      createIndices(row, col);

      boolean findWater = true;
      while(findWater) {
          Collections.shuffle(indices);
          int i = (indices.get(0))[0];
          int j = (indices.get(0))[1];
          if(!(j==col-1)){
              grid.changeNext(new int[]{i,j}, PERCOLATED);
              findWater = false;
          }
      }


      grid.updateAll();
      grid.addToPane(pane);
  }


    @Override
    public HashMap<String, Integer> frequency() {
        HashMap<String, Integer>ret = new HashMap<>();
        ret.put("OPEN", grid.getFreq(OPEN));
        ret.put("PERCOLATED", grid.getFreq(PERCOLATED));
        ret.put("CLOSED", grid.getFreq(CLOSED));
        return ret;
    }

    public void updateGrid(){
      for(int[]index:indices) {
          ArrayList<Integer> neighbours = grid.neighbourStatus(index);
          int next = checkPercolate(grid.getCell(index), neighbours);
          grid.changeNext(index, next);
      }
      grid.updateAll();
  }


  private int checkPercolate(int curCell, ArrayList<Integer> neighbours){
      if(curCell==PERCOLATED || curCell==CLOSED) return curCell;
      for(int neighbour:neighbours){
          if(neighbour==PERCOLATED) return PERCOLATED;
      }
      return curCell;
  }

  @Override
  public void checkNeighbourAndChangeNext(Cell cell, List<Cell> neighbours){

  }

}

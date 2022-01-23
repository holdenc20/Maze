import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import javalib.impworld.*;
import javalib.worldimages.AboveImage;
import javalib.worldimages.BesideImage;
import javalib.worldimages.EmptyImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;
import tester.Tester;

public class Maze extends World {
  int dims;
  ArrayList<ArrayList<ACell>> cells = new ArrayList<ArrayList<ACell>>();
  Random r = new Random();

  Maze(int dims) {
    this.dims = dims;
    this.generateMaze();
  }

  private void generateMaze() {
    for (int row = 0; row < this.dims; row++) {
      ArrayList<ACell> temp = new ArrayList<ACell>();
      for (int col = 0; col < this.dims; col++) {
        temp.add(col, new Cell());
      }
      cells.add(temp);
    }

    //to add links to cells
    for (int row = 0; row < this.dims; row++) {
      for (int col = 0; col < this.dims; col++) {
        ACell top = new Wall();
        ACell bot = new Wall();
        ACell left = new Wall();
        ACell right = new Wall();
        //top
        if (row != 0) {
          top = cells.get(row - 1).get(col);
        }
        //left
        if (col != 0) {
          left = cells.get(row).get(col - 1);
        }

        //bot
        if (row != this.dims - 1) {
          bot = cells.get(row + 1).get(col);
        }

        //right
        if (col != this.dims - 1) {
          right = cells.get(row).get(col + 1);
        }
        cells.get(row).get(col).configureSides(top, bot, left, right);
      }
    }

    boolean notFound = true;
    int count = 0;
    while (notFound && count != 5000) {
      count += 1;
      int side = r.nextInt(4);
      int row = r.nextInt(this.dims);
      int col = r.nextInt(this.dims);
      //int paths = this.countPaths();
      //
      //      if (paths == 1) {
      //        notFound = false;
      //      }

      cells.get(row).get(col).addWall(side);

      //unconfirmed all
      for (int r = 0; r < dims; r++) {
        for (int c = 0; c < dims; c++) {
          cells.get(r).get(c).confirm(true);
        }
      }
      cells.get(0).get(0).confirm(false);
      cells.get(dims - 1).get(dims - 1).confirm(false);

      if (!this.allCellsConfirmed()) {
        if (side == 0 && row > 0) {
          cells.get(row).get(col).setSide(cells.get(row - 1).get(col), side);
        }
        if (side == 1 && row < this.dims - 1) {
          cells.get(row).get(col).setSide(cells.get(row + 1).get(col), side);
        }
        if (side == 2 && col > 0) {
          cells.get(row).get(col).setSide(cells.get(row).get(col - 1), side);
        }
        if (side == 3 && col < this.dims - 1) {
          cells.get(row).get(col).setSide(cells.get(row).get(col + 1), side);
        }
      }
    }
  }

  //on space the path will show up on the maze
  public void onKeyEvent(String s) {
    if (s.equals(" ")) {
      this.colorPath();
    }
  }

  private void colorPath() {
    ArrayList<ACell> seen = new ArrayList<ACell>();
    cells.get(0).get(0).findEnd(cells.get(dims - 1).get(dims - 1), seen);

    //this contains all the seen elements when iterating through the maze
    //by removing all the edges besides the first and last cells then we should just have the shortest path
    //this works because when constructing the maze it is actually a tree because it has no cycles
    //so we are just removing the branches that are not in the final path
    for (int x = 0; x < 100; x++) {
      for (int c = 0; c < seen.size(); c++) {
        if (!seen.get(c).hasMoreThanOneEdge(seen, cells.get(0).get(0),
            cells.get(dims - 1).get(dims - 1))) {
          seen.remove(seen.get(c));
          c--;
        }
      }
    }
    for (ACell c : seen) {
      c.paintPath();
    }

  }


  //determines if all the cells are valid
  private boolean allCellsConfirmed() {
    boolean part1;
    //determines if the cells are valid to the bottom right
    for (int r = 0; r < dims; r++) {
      for (int c = 0; c < dims; c++) {
        cells.get(r).get(c).confirm(true);
      }
    }
    cells.get(dims - 1).get(dims - 1).confirm(false);

    cells.get(dims - 1).get(dims - 2).confirmAll();
    cells.get(dims - 2).get(dims - 1).confirmAll();
    for (int row = 0; row < dims; row++) {
      for (int col = 0; col < dims; col++) {
        if (!cells.get(row).get(col).isConfirmed()) {
          return false;
        }
      }
    }
    part1 = true;

    //determines if the cells are valid to the bottom right
    for (int r = 0; r < dims; r++) {
      for (int c = 0; c < dims; c++) {
        cells.get(r).get(c).confirm(true);
      }
    }
    cells.get(0).get(0).confirm(false);

    cells.get(1).get(0).confirmAll();
    cells.get(0).get(1).confirmAll();
    for (int row = 0; row < dims; row++) {
      for (int col = 0; col < dims; col++) {
        if (!cells.get(row).get(col).isConfirmed()) {
          return false;
        }
      }
    }
    return part1;
  }

  public WorldScene makeScene() {
    WorldImage grid = new EmptyImage();
    WorldScene s = this.getEmptyScene();

    for (ArrayList<ACell> row : cells) {
      WorldImage tempImage = new EmptyImage();
      for (ACell cell : row) {
        tempImage = new BesideImage(tempImage, cell.drawCell());
      }
      grid = new AboveImage(grid, tempImage);
    }
    s.placeImageXY(grid, 20 * 2 / 3 * dims, 20 * 2 / 3 * dims);
    return s;
  }

}
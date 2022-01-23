import java.awt.Color;
import java.util.ArrayList;

import javalib.worldimages.AboveImage;
import javalib.worldimages.BesideImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

//represents a cell in a maze
class Cell extends ACell {
  ACell top;
  ACell bot;
  ACell left;
  ACell right;
  boolean isConfirmed = false;
  Color c = Color.white;

  Cell() {
    top = new Wall();
    bot = new Wall();
    left = new Wall();
    right = new Wall();
  }


  Cell(ACell top, ACell bot, ACell left, ACell right) {
    this.top = top;
    this.bot = bot;
    this.left = left;
    this.right = right;
  }

  //determines if this cell has more than 1 edge in seen 
  //unless it is the first or last cell (the start and the finish of the maze)
  public boolean hasMoreThanOneEdge(ArrayList<ACell> seen, ACell first, ACell last) {
    int count = 0;
    if(this.equals(last) || this.equals(first)) {
      return true;
    }
    
    for (ACell c : seen) {
      if (this.top.equals(c)) {
        count++;
      }
      if (this.bot.equals(c)) {
        count++;
      }
      if (this.left.equals(c)) {
        count++;
      }
      if (this.right.equals(c)) {
        count++;
      }
    }
    return count > 1;
  }

  //changes the color of the cell to show the solution
  public void paintPath() {
    this.c = Color.orange;
  }

  //validates all the cells connected to the original cell
  public void confirmAll() {
    if (!this.isConfirmed) {
      this.isConfirmed = true;
      this.top.confirmAll();
      this.bot.confirmAll();
      this.left.confirmAll();
      this.right.confirmAll();
    }

  }

  public void confirm(boolean unconfirm) {
    this.isConfirmed = !unconfirm;
  }

  public boolean isConfirmed() {
    return this.isConfirmed;
  }

  public void addWall(int side) {
    if (side == 0) {
      this.top.setWall(1);
      this.top = new Wall();
    }
    if (side == 1) {
      this.bot.setWall(0);
      this.bot = new Wall();
    }
    if (side == 2) {
      this.left.setWall(3);
      this.left = new Wall();
    }
    if (side == 3) {
      this.right.setWall(2);
      this.right = new Wall();
    }
  }

  public void setWall(int side) {
    if (side == 0) {
      this.top = new Wall();
    }
    if (side == 1) {
      this.bot = new Wall();
    }
    if (side == 2) {
      this.left = new Wall();
    }
    if (side == 3) {
      this.right = new Wall();
    }
  }

  public void setSide(ACell aCell, int side) {
    if (side == 0) {
      aCell.setSide2(this, 1);
      this.top = aCell;
    }
    if (side == 1) {
      aCell.setSide2(this, 0);
      this.bot = aCell;
    }
    if (side == 2) {
      aCell.setSide2(this, 3);
      this.left = aCell;
    }
    if (side == 3) {
      aCell.setSide2(this, 2);
      this.right = aCell;
    }
  }

  public void setSide2(ACell aCell, int side) {
    if (side == 0) {
      this.top = aCell;
    }
    if (side == 1) {
      this.bot = aCell;
    }
    if (side == 2) {
      this.left = aCell;
    }
    if (side == 3) {
      this.right = aCell;
    }
  }

  public boolean findEnd(ACell target, ArrayList<ACell> alreadyVisited) {
    if (this.equals(target)) {
      alreadyVisited.add(this);
      return true;
    }
    else if (alreadyVisited.contains(this)) {
      return false;
    }
    else {
      alreadyVisited.add(this);
      return this.top.findEnd(target, alreadyVisited) || this.bot.findEnd(target, alreadyVisited)
          || this.left.findEnd(target, alreadyVisited)
          || this.right.findEnd(target, alreadyVisited);
    }
  }

  public void configureSides(ACell top, ACell bot, ACell left, ACell right) {
    this.top = top;
    this.bot = bot;
    this.left = left;
    this.right = right;
  }

  public WorldImage drawCell() {
    WorldImage cellImage = new RectangleImage(20, 20, OutlineMode.SOLID, this.c);
    if (top.isWall()) {
      cellImage = new AboveImage(new RectangleImage(20, 1, OutlineMode.SOLID, Color.red),
          cellImage);
    }
    else {
      cellImage = new AboveImage(new RectangleImage(20, 1, OutlineMode.SOLID, this.c), cellImage);
    }
    if (bot.isWall()) {
      cellImage = new AboveImage(cellImage,
          new RectangleImage(20, 1, OutlineMode.SOLID, Color.red));
    }
    else {
      cellImage = new AboveImage(cellImage, new RectangleImage(20, 1, OutlineMode.SOLID, this.c));
    }
    if (left.isWall()) {
      cellImage = new BesideImage(new RectangleImage(1, 20, OutlineMode.SOLID, Color.red),
          cellImage);
    }
    else {
      cellImage = new BesideImage(new RectangleImage(1, 20, OutlineMode.SOLID, this.c), cellImage);
    }
    if (right.isWall()) {
      cellImage = new BesideImage(cellImage,
          new RectangleImage(1, 20, OutlineMode.SOLID, Color.red));
    }
    else {
      cellImage = new BesideImage(cellImage, new RectangleImage(1, 20, OutlineMode.SOLID, this.c));
    }
    return cellImage;
  }
}

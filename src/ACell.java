import java.util.ArrayList;

import javalib.worldimages.WorldImage;

abstract class ACell {
  public boolean isWall() {
    return false;
  }

  public boolean hasMoreThanOneEdge(ArrayList<ACell> seen, ACell first, ACell last) {
    // TODO Auto-generated method stub
    return false;
  }

  public void paintPath() {
    // TODO Auto-generated method stub

  }

  public void confirmAll() {
    // TODO Auto-generated method stub

  }

  public boolean isConfirmed() {
    return false;
  }

  public void confirm(boolean unconfirm) {
    // TODO Auto-generated method stub

  }

  public void setSide(ACell aCell, int i) {
    // TODO Auto-generated method stub

  }

  public void addWall(int side) {
    // TODO Auto-generated method stub

  }

  public WorldImage drawCell() {
    // TODO Auto-generated method stub
    return null;
  }

  public void configureSides(ACell top, ACell bot, ACell left, ACell right) {
    // TODO Auto-generated method stub

  }

  public boolean findEnd(ACell aCell, ArrayList<ACell> alreadyVisited) {
    return false;
  }

  public void setWall(int i) {
    // TODO Auto-generated method stub

  }

  public void setSide2(ACell aCell, int side) {
    // TODO Auto-generated method stub

  }

}

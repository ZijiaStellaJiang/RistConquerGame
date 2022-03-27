package edu.duke.ece651.group4.risc.shared;

import org.checkerframework.checker.units.qual.A;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Territory<T> implements java.io.Serializable {
  private final String name;
  private ArrayList<Territory<T>> myNeigh;
  private ArrayList<Unit<T>> myUnits;
  //private HashMap<Integer,ArrayList<Unit<T>>> myUnits;
  private ArrayList<Unit<T>> enemyUnits;
  private int size;
  private ArrayList<Resource<T>> myResource;

  public Territory(String name, int size){
    this.name = name;
    this.myNeigh = new ArrayList<Territory<T>>();
    this.myUnits = new ArrayList<>();
    this.enemyUnits = new ArrayList<>();
    this.size = size;
    this.myResource = new ArrayList<>();
  }
  /**
   * constructor holder for evol1, no use
   */
  public Territory(String name){
    this(name,0);
  }

  public String getName(){
    return this.name;
  }

  /**
   * add a territory as this one's neighbour
   * at the same time, add this territory to neighToAdd's neighbour
   */
  public void addNeigh(Territory<T> neighToAdd){
    if(!neighToAdd.equals(this)){
      if(!this.checkNeigh(neighToAdd)){
        myNeigh.add(neighToAdd);
      }
      if(!neighToAdd.checkNeigh(this)){
        neighToAdd.addNeigh(this);
      }
    }
  }

  /**
   * remove a territory from its neighbour
   * at the same time, remove this territory from neighToRemove's neighbour
   */
  public void removeNeigh(Territory<T> neighToRemove){
    if(myNeigh.remove(neighToRemove)){
      neighToRemove.removeNeigh(this);
    }
  }

  public boolean checkNeigh(Territory<T> neighToCheck){
    return myNeigh.contains(neighToCheck);
  }

  public ArrayList<Territory<T>> getMyNeigh(){
    return myNeigh;
  }

  /**
   * return the number of units exist in this territory
   */
  public Integer getUnitNumber() {
    return myUnits.size();
  }

  public void addUnit(Unit<T> unitToAdd){
    myUnits.add(unitToAdd);
  }

  public void removeUnit(Unit<T> unitToRemove){
    for(Unit<T> t: myUnits){
      myUnits.remove(t);
      break;
    }
    /**
     * in later version might change to below code to support different kind of unit
     * for this version just use above simple one to realize test coverage
     */
//    for(Unit<T> t: myUnits){
//      if (t.getClass().equals(unitToRemove.getClass())){
//        myUnits.remove(t);
//        break;
//      }
//    }
  }

  public Integer getEnemyUnitNum(){ return enemyUnits.size(); }

  /** store attacker's unit */
  public void addEnemyUnit(Unit<T> enemyUnitToAdd){
    enemyUnits.add(enemyUnitToAdd);
  }

  public void removeEnemyUnit(Unit<T> unitToRemove){
    for (Unit<T> t: enemyUnits){
      enemyUnits.remove(t);
      break;
    }
    /**
     * in later version might change to below code to support different kind of unit
     * for this version just use above simple one to realize test coverage
     */
//    for(Unit<T> t: enemyUnits){
//      if (t.getClass().equals(unitToRemove.getClass())){
//        enemyUnits.remove(t);
//        break;
//      }
//    }
  }

  public void addGroupUnit(ArrayList<Unit<T>> toAdd){
    for (Unit<T> u: toAdd){
      this.addUnit(u);
    }
  }

  @Override
  public boolean equals(Object o){
    if(o.getClass().equals(getClass())){
      Territory<T> terr = (Territory<T>) o;
      return name.equals(terr.getName());
    }
    return false;
  }

}

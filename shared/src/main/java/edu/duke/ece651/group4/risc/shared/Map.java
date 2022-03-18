package edu.duke.ece651.group4.risc.shared;

import java.util.ArrayList;

public class Map<T> implements java.io.Serializable {
  private ArrayList<Player<T>> myPlayers;
  private ArrayList<Territory<T>> myTerritories;
  public Map(){
    myPlayers = new ArrayList<>();
    myTerritories = new ArrayList<>();
  }
  /**
   * add a player to this map
   */
  public void addPlayer(Player<T> playerToAdd){
    if(!myPlayers.contains(playerToAdd)){
      myPlayers.add(playerToAdd);
    }
  }

  /**
   * add a territory to the current map
   */
  public void addTerritory(Territory<T> toAdd){
    if(!myTerritories.contains(toAdd)){
      myTerritories.add(toAdd);
    }
  }

  /**
   * check if a territory is in the map
   */
  public Boolean checkTerritoryExists(Territory<T> toCheck){
    for (Territory<T> t: myTerritories){
      if(toCheck.equals(t)){
        return true;
      }
    }
    return false;
  }

  public ArrayList<Player<T>> getMyPlayers(){
    return myPlayers;
  }

  public ArrayList<Territory<T>> getMyTerritories(){
    return myTerritories;
  }

  public String getPlayerName(int id) {
    if (id >= myPlayers.size()) return null;
    return myPlayers.get(id).getName();
  }
  public Player<T> getPlayer(int id) {
    if (id >= myPlayers.size()) return null;
    return myPlayers.get(id);
  }

  public Player<T> findPlayer(Territory<T> toFind) {
    for (Player<T> p: myPlayers){
      if(p.checkMyTerritory(toFind)){
        return p;
      }
    }
    return null;
  }

  /**
   * at the end of each round, call this function to add a new unit for each territory
   */
  public void receive_new_units(){
    for (Territory<T> t: myTerritories){
      t.addUnit(new SimpleUnit<>());
    }
  }

  /**
   * @return the loser's id if the game ends
   * otherwise return null
   */
  public Integer getLoserId(){
    for (int i = 0; i<myPlayers.size(); i++){
      if(myPlayers.get(i).checkLose()){
        return i;
      }
    }
    return null;
  }

  public void reset(){
    for (Player<T> p: myPlayers){
      p.resetLastRoundChange();
    }
  }
}
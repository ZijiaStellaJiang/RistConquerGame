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
}
/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.group4.risc.client;

import edu.duke.ece651.group4.risc.shared.*;
import javafx.application.Application;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Client {
  private Map<Character> map;
  private Socket player_skd;
  private ObjectOutputStream player_out;
  private ObjectInputStream player_in;
  private int player_id;
  private BufferedReader inputReader;
  private PrintStream output;
  private Action<Character> move_myself;
  private Action<Character> move_enemy;
  private Action<Character> attack;
  private Action<Character> update;
  private ArrayList<ActionParser> order_list;
  private MapTextView displayInfo;


  public Client(String serverName, int port, BufferedReader input, PrintStream outputStream) throws RuntimeException{
    inputReader = input;
    output = outputStream;
    // connection to Server
    player_skd = connectServer(serverName, port);
    player_id = -1;
    move_myself = new MoveAction<>(true);
    move_enemy = new MoveAction<>(false);
    attack = new AttackAction<>();
    update = new UpdateAction<>();
    order_list =  new ArrayList<ActionParser>();
    displayInfo = null;
    try {
      player_out = new ObjectOutputStream(player_skd.getOutputStream());
      player_in = new ObjectInputStream(new BufferedInputStream(player_skd.getInputStream()));
    } catch (IOException e) {
      throw new RuntimeException("cannot connect to server");
    }
  }

  public ArrayList<ActionParser> getOrder_list() {
    return order_list;
  }

  public int getPlayerId(){
    return this.player_id;
  }

  public Map<Character> getMap(){
    return this.map;
  }
  public Socket getSocket(){
    return player_skd;
  }
  public static Socket connectServer(String serverName, int port) {
    try {
      System.out.println("Connecting to " + serverName + " on port " + port);
      Socket client_skd = new Socket(serverName, port);
      System.out.println("Just connected to " + client_skd.getRemoteSocketAddress());
      return client_skd;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  public void initializeGame() {
    try {
      // receive an object from server
      Object obj_map = player_in.readObject();
      Object obj_id = player_in.readObject();
      // display the initial map and player_id
      if (obj_map != null && obj_id != null) {
        // update the map and player_id
        map = (Map<Character>) obj_map;
        player_id = (Integer) obj_id;
        MapTextView displayInfo = new MapTextView(map, System.out);
        // display the map
        displayInfo.displayCurrentMap();
        // display player id
        displayInfo.displayPlayerMsg(player_id);
      }
    } catch (IOException e) {
      //e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
  public void send_to_server(Object obj) throws IOException{
    try {
      player_out.reset();
      player_out.writeObject(obj);
      player_out.flush();
    } catch(IOException e) {
      throw e;
    }
  }
  public Object recv_from_server() throws IOException{
    Object obj = null;
    try{
      obj = player_in.readObject();
    } catch (IOException e) {
      //e.printStackTrace();
      throw e;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return obj;
  }

  public String revcOrderStr() throws IOException{
    String orderStr = null;
    // read an input from client
    String str = inputReader.readLine();
    if (str == null) throw new EOFException("END");
    // check done
    str = str.toUpperCase();
    if (!str.equals("DONE")) orderStr = str;
    return orderStr;
  }
  public ActionParser parseOrder(String orderStr) {
    ActionParser order = null;
    try {
      order = new ActionParser(orderStr);
    } catch (IllegalArgumentException e) {
      output.println(e.getMessage());
    }
    return order;
  }
  public void oneRoundBegin() {
    order_list = new ArrayList<ActionParser>();
  }
  public void oneRoundEnd() throws IOException {
    output.println("-----------Sending message to server--------");
    // send order list to server
    send_to_server(order_list);
  }
  public void oneRoundUpdate() throws IOException {
    output.println("-----------Receving message from server--------");
    map = null;
    map = (Map<Character>)recv_from_server();
  }
  public void mapDisplay() {
    output.println("-----------showing the map--------");
    displayInfo = new MapTextView(map, output);
    displayInfo.displayCurrentMap();
    displayInfo.displayPlayerMsg(player_id);
  }
  public boolean checkGameOver() {
    Integer id = map.getLoserId();
    if (id != null) {
      displayInfo.displayVictoryMsg(id);
      return true;
    }
    return false;
  }
  public String addOrder(ActionParser order) {
      Player<Character> player = map.getPlayer(player_id);
      String result = null;
      if (order.getType().equals("MOVE")) {
        result = move_myself.doAction(order, map, player);
      } else if (order.getType().equals("ATTACK")) {
        result = move_enemy.doAction(order, map, player);
      } else if (order.getType().equals("UPDATE")) {
        result = update.doAction(order, map, player);
      } else {
        result = "WRONG TYPE ERROR!";
      }
      if (result != null) {
        return result;
      }
      // add to order list
      order_list.add(order);
      return result;
  }
  public boolean playOneRound() throws IOException {
    oneRoundBegin();
    while (true) {
      // recv an input String
      String orderStr = revcOrderStr();
      if (orderStr == null) break;
      // parse the input to order
      ActionParser order = parseOrder(orderStr);
      // add order to orderlist
      if (order != null) addOrder(order);
    }
    // send orderlist to server
    oneRoundEnd();
    // receive new update map
    oneRoundUpdate();
    // display new update map
    mapDisplay();
    // make sure if the game is over
    return checkGameOver();
  }



  public void close_connection() {
    try {
      player_out.close();
      player_in.close();
      player_skd.close();
    } catch(IOException e) {
      e.printStackTrace();
    }
  }


  public static void main(String[] args) throws IOException {
    Application.launch(StartGame.class, args);
    /*System.out.println("Enter server's ip:");
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    String str = reader.readLine();
    Client client = new Client(str, 6066, new BufferedReader(new InputStreamReader(System.in)), System.out);
    // receive initial map and id
    client.initializeGame();
    // play game
    while (true) {
      if (client.playOneRound()) break;
    }
    // close connection
    client.close_connection();*/
  }


}


package trading_game;

import java.util.List;

public class Test {

  public static void main(String[] args) {
    Exchange curr = new Exchange(100000);
    curr.runCommandLoop();
    System.out.println("done");
  }
}

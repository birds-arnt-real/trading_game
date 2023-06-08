package trading_game;

import java.util.ArrayList;
import java.util.HashMap;

public class Portfolio {


  private double current_amount;
  private HashMap<String, Asset> assets = new HashMap<>();

  public Portfolio(Double _starting_amount){
    this.current_amount = _starting_amount;
  }

  /**
   * This function completes a buy transaction
   * @param to_add
   * @param amount
   */
  public void buy(Asset to_add, int amount){

    if(current_amount - (amount * to_add.price) >= 0){
        Asset sale = new Asset(to_add.ticker,to_add.name,to_add.price,amount);
        this.current_amount -= (amount * to_add.price);
        assets.put(to_add.ticker,sale);
    }
    else{
      System.out.println("You can not afford " + String.valueOf(amount) + to_add.ticker + ".");
    }
  }


}

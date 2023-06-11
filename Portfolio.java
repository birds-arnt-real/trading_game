package trading_game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Portfolio {

  protected double current_amount;
  protected double starting_amount;
  protected double overall_profit_loss;
  protected double most_recent_profit_loss_change;
  protected HashMap<String, Asset> assets = new HashMap<>();

  public Portfolio(Double _starting_amount) {
    this.current_amount = _starting_amount;
    this.starting_amount = _starting_amount;
    this.overall_profit_loss = 0;
    this.most_recent_profit_loss_change = 0;
  }

  /**
   * This function completes a buy transaction
   *
   * @param to_add
   * @param amount
   */
  public boolean buy(Asset to_add, int amount) {

    if (current_amount - (amount * to_add.price) >= 0) {
      Asset sale = new Asset(to_add.ticker, to_add.name, to_add.price, amount);
      this.current_amount -= (amount * to_add.price);
      assets.put(to_add.ticker, sale);
      return true;
    } else {
      System.out.println("You can not afford " + String.valueOf(amount) + to_add.ticker + ".");
      return false;
    }
  }

  /**
   * This function completes a sale and records the profit
   *
   * @param to_sell
   * @param amount
   */
  public boolean sell(String to_sell, int amount, double current_price) {

    if (assets.containsKey(to_sell) && (assets.get(to_sell).amount <= amount)) {
      this.current_amount += (amount * current_amount);
      //        this.overall_profit_loss += ((amount*assets.get());
    }
    return false;
  }


  @Override public String toString() {
    String output ="\nCurrent Portfolio:";
    for (Map.Entry<String, Asset> entry : this.assets.entrySet()) {
      String key = entry.getKey();
      Asset value = entry.getValue();
      output += ("Key: " + key + ", Value: " + value);
    }
    return output;
  }

}




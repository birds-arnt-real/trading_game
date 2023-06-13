package trading_game;

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
      to_add.amount = amount;
      this.current_amount -= (amount * to_add.price);
      to_add.buy_price = to_add.price;
      assets.put(to_add.ticker, to_add);
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
  public boolean sell(String to_sell, int amount) {

    if (this.assets.containsKey(to_sell) && (this.assets.get(to_sell).amount <= amount)) {
        double curr_value = this.assets.get(to_sell).amount * this.assets.get(to_sell).buy_price;
        double buy_value = this.assets.get(to_sell).amount * this.assets.get(to_sell).price;
        this.current_amount += curr_value;
        this.overall_profit_loss += (curr_value - buy_value);
        return true;
    }
    return false;
  }

  public double profit_loss_turn(){
    double pl = 0.0;
    // trying to update the profit loss
    for (Map.Entry<String, Asset> entry : this.assets.entrySet()) {
      String key = entry.getKey();


      int price_history_list_end = this.assets.get(key).price_history.size() - 2 > 0 ?
          this.assets.get(key).price_history.size() - 2 : 0;

      pl = (this.assets.get(key).amount * this.assets.get(key).price) -
          (this.assets.get(key).amount  * this.assets.get(key).price_history.get(price_history_list_end));

    }
    return pl;
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




package trading_game;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
   */
  public boolean buy(Asset to_add, int amount) {

    if (current_amount - (amount * to_add.price) >= 0) {
      to_add.amount += amount;
      this.current_amount -= (amount * to_add.price);
      if (this.assets.containsKey(to_add.ticker)) {
        to_add.buy_price = average_buy_price(to_add, to_add.price, amount);
      }
      else {
        to_add.buy_price = to_add.price;
      }

      assets.put(to_add.ticker, to_add);
      return true;
    } else {
      System.out.println("You can not afford " + amount + to_add.ticker + ".");
      return false;
    }
  }

  public double average_buy_price(Asset asset, double price, int amount){
    return ((asset.buy_price * asset.amount) + (price * amount)) / (asset.amount + amount);
  }

  /**
   * This function completes a sale and records the profit
   *  @param to_sell
   * @param amount
   */
  public void sell(String to_sell, int amount) {

    if (this.assets.containsKey(to_sell) && (this.assets.get(to_sell).amount >= amount)) {
        double curr_value = amount * this.assets.get(to_sell).buy_price;
        double buy_value = amount * this.assets.get(to_sell).price;
        this.current_amount += curr_value;
        this.overall_profit_loss += (curr_value - buy_value);
        this.assets.get(to_sell).amount -= amount;

        System.out.println(amount + " shares of " + to_sell + "sold for a profit of "
            + (buy_value-curr_value));


        //reset asset and remove it from the portfolio if we sell all of it
        if(this.assets.get(to_sell).amount == 0){
          this.assets.get(to_sell).buy_price = 0;
          this.assets.get(to_sell).profit_loss = 0;
          this.assets.remove(to_sell);
        }


    }
  }

  public double profit_loss_overall_per_turn(){
    double pl = 0.0;
    // trying to update the profit loss
    for (Map.Entry<String, Asset> entry : this.assets.entrySet()) {
      String key = entry.getKey();

      int price_history_list_end = Math.max(this.assets.get(key).price_history.size() - 2, 0);

      pl += (this.assets.get(key).amount * this.assets.get(key).price) -
          (this.assets.get(key).amount  * this.assets.get(key).price_history.get(price_history_list_end));
    }
    return pl;
  }

  public void profit_loss_individual_asset_per_turn(Asset asset){

    // trying to update the profit loss

      int price_history_list_end = Math.max(asset.price_history.size() - 2, 0);

      double pl = (asset.amount * asset.price) -
          (asset.amount * asset.price_history.get(price_history_list_end));

    asset.profit_loss += pl;
  }

  /**
   * Takes in a list of assets and formats output with left justification and a line break
   *   made out of equal signs
   * @return List of asset strings with appropriate and relative spacing
   */
  public String toString() {
    // Split each string into sections
    List<Asset> assets = new ArrayList<>(this.assets.values());
    List<List<String>> stringSections = new ArrayList<>();
    List<String> output = new ArrayList<>();

    DecimalFormat decimalFormat = new DecimalFormat("#.00");
    // Find the maximum length for each section
    int[] max_lengths = new int[7];
    for(Asset asset : assets){
      String[] curr_asset = asset.toArray();
      max_lengths[0] = Math.max(6,curr_asset[0].length());
      max_lengths[1] = Math.max(max_lengths[1],curr_asset[1].length());
      max_lengths[2] = Math.max(6,curr_asset[2].length());
      max_lengths[3] = Math.max(max_lengths[3], curr_asset[3].length());
      max_lengths[4] = Math.max(6,curr_asset[4].length());
      max_lengths[5] = Math.max(11,curr_asset[5].length());
      max_lengths[6] = Math.max(6,curr_asset[6].length());
    }

    for(Asset asset: assets){
      String[] curr_asset = asset.toArray();

      String formatted_output = String.format("%-" + max_lengths[0] + "s" + "      "  +
              "%-" + max_lengths[1] + "s" + "   "     +
              "%-" + max_lengths[2] + "s" + "       " +
              "%-" + max_lengths[6] + "s" + "       " +
              "%-" + max_lengths[3] + "s" + "       " +
              "%-" + max_lengths[4] + "s" + "       " +
              "%-" + max_lengths[5] + "s" + "       " ,
          curr_asset[0],
          curr_asset[1],
          decimalFormat.format(Double.parseDouble(curr_asset[2])),
          curr_asset[6],
          curr_asset[3],
          decimalFormat.format(Double.parseDouble(curr_asset[4])),
          decimalFormat.format(Double.parseDouble(curr_asset[5])));
      output.add(formatted_output);
    }

    // add header and display line
    String ticker_name_space = " ".repeat(6);
    String name_price_space = " ".repeat(3);
    String price_highlow_space = " ".repeat(7);
    String highlow_buy_space = " ".repeat(7);
    String buy_pl_space = " ".repeat(4);
    String line_break = "=".repeat(100);

    String col_labels = String.format("%-" + max_lengths[0] + "s" + ticker_name_space  +
            "%-" + max_lengths[1] + "s" + name_price_space     +
            "%-" + max_lengths[2] + "s" + price_highlow_space +
            "%-" + max_lengths[6] + "s" + price_highlow_space +
            "%-" + max_lengths[3] + "s" + highlow_buy_space +
            "%-" + max_lengths[4] + "s" + buy_pl_space +
            "%-" + max_lengths[5] + "s",
        "Ticker",
        "Name",
        "Price",
        "Amount",
        "(low, high)",
        "Buy Price",
        "PL");

    output.add(0,col_labels);
    output.add(1,line_break);

    StringBuilder display = new StringBuilder();

    for(String asset: output){
      display.append(asset).append("\n");
    }

    return display.toString();
  }

}




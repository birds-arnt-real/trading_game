package trading_game;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Models an individual asset
 */
public class Asset {

  protected String ticker;
  protected String sector;
  protected String name;
  protected double price;
  protected ArrayList<Double> price_history = new ArrayList<>();
  protected int trend_direction;
  protected double volatility_factor;
  protected int amount;
  protected double buy_price;
  protected double profit_loss;

  /**
   * This is the constructor that gets added into the market
   */
  public Asset(String _ticker, String _sector, String _name, double _price, int _trend_direction,
      double _volatility_factor){

    this.ticker = _ticker;
    this.sector = _sector;
    this.name = _name;
    this.price = _price;
    this.trend_direction = _trend_direction;
    this.volatility_factor = _volatility_factor;
    this.price_history.add(_price);
    this.amount = 0;
    this.profit_loss = 0;
  }

  /**
   * This is only for testing, ill write a better one for actual game play
   */
  @Override public String toString() {
//    return "Asset{" + "ticker='" + ticker + '\'' + ", sector='" + sector + '\'' + ", name='" + name
//        + '\'' + ", price=" + price + ", price_history=" + price_history + ", trend_direction="
//        + trend_direction + ", volatility_factor=" + volatility_factor + "}\n";

    return ticker + " ("+name+"): $" + price + "  (" +
        Collections.min(price_history) + ", " + Collections.max(price_history)+")";
  }

  public String[] toArray(){

    DecimalFormat decimalFormat = new DecimalFormat("#.00");

    String[] output = new String[7];
    output[0] = ticker;
    output[1] = name;
    output[2] = String.valueOf(decimalFormat.format(price));
    output[3] =
        "(" + decimalFormat.format(Collections.min(price_history)) +
            ", " + decimalFormat.format(Collections.max(price_history))+")";
    output[4] = String.valueOf(buy_price);
    output[5] = String.valueOf(profit_loss);
    output[6] = String.valueOf(amount);

    return output;
  }
}

package trading_game;

import java.lang.reflect.Array;
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
  protected ArrayList<Double> price_history = new ArrayList<Double>();
  protected int trend_direction;
  protected double volatility_factor;
  protected int amount;
  protected double buy_price;

  /**
   * This is the constructor that gets added into the market
   * @param _ticker
   * @param _sector
   * @param _name
   * @param _price
   * @param _trend_direction
   * @param _volatility_factor
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
  }

  /**
   * This is the asset that gets added into the portfolio
   * @param _ticker
   * @param _name
   * @param _price
   * @param amount
   */
  public Asset(String _ticker, String _name, double _price,int amount){
    this.ticker = _ticker;
    this.sector = null;
    this.name = _name;
    this.buy_price = _price;
    this.amount = amount;
  }

  /**
   * This is only for testing, ill write a better one for actual game play
   * @return
   */
  @Override public String toString() {
//    return "Asset{" + "ticker='" + ticker + '\'' + ", sector='" + sector + '\'' + ", name='" + name
//        + '\'' + ", price=" + price + ", price_history=" + price_history + ", trend_direction="
//        + trend_direction + ", volatility_factor=" + volatility_factor + "}\n";

    return ticker + " ("+name+"): $" + String.valueOf(price) + "  (" +
        Collections.min(price_history) + ", " + Collections.max(price_history)+")";
  }

  public String[] toArray(){
    String[] output = new String[4];
    output[0] = ticker;
    output[1] = name;
    output[2] = String.valueOf(price);
    output[3] = "(" + Collections.min(price_history) + ", " + Collections.max(price_history)+")";
    return output;
  }
}

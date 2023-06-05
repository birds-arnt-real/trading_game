package trading_game;

import java.util.ArrayList;

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
  protected int volatility_factor;


  public Asset(String _ticker, String _sector, String _name, double _price, int _trend_direction,
      int _volatility_factor){

    this.ticker = _ticker;
    this.sector = _sector;
    this.name = _name;
    this.price = _price;
    this.trend_direction = _trend_direction;
    this.volatility_factor = _volatility_factor;
    this.price_history.add(_price);
  }

  /**
   * This is only for testing, ill write a better one for actual game play
   * @return
   */
  @Override public String toString() {
//    return "Asset{" + "ticker='" + ticker + '\'' + ", sector='" + sector + '\'' + ", name='" + name
//        + '\'' + ", price=" + price + ", price_history=" + price_history + ", trend_direction="
//        + trend_direction + ", volatility_factor=" + volatility_factor + "}\n";

    return ticker + "("+name+"): " + String.valueOf(price) + "\n";
  }
}

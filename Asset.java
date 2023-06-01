package trading_game;

/**
 * Models an individual asset
 */
public class Asset {

  protected String ticker;
  protected String sector;
  protected String name;
  protected double price;
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
  }

}

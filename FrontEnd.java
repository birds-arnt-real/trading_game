package trading_game;
import java.util.ArrayList;

public class FrontEnd {

  private ArrayList<Asset> portfolio;
  private double account_amount;


  public FrontEnd(double _account_amount){
    this.account_amount = _account_amount;
    this.portfolio = new ArrayList<>();
    
  }


}

package trading_game;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.Port;

public class JUnit_Tests {
  private Market test_market;
  private Portfolio test_portfolio;

  @BeforeEach
  public void init_test_object(){
    this.test_market = new Market("financials.csv");
    this.test_portfolio = new Portfolio(10000.0);
  }

  //MARKET TESTS

  /**
   * Tests that if the CSV can not be loaded we throw a specific error
   */
  @Test
  public void test_init_load_success() {
    Assert.assertEquals(505, this.test_market.curr_market.size());
  }

  /**
   * Tests that the market has the correct amount of unique sectors
   */
  @Test
  public void test_sectors() {
    Assert.assertEquals(11,this.test_market.sectors.size());
  }

  /**
   * This tests that all available assets are distributed into the sectors
   */
  @Test
  public void test_correct_amount_of_total_assets_in_sectors(){

    int amount_of_assets = 0;
    for(String assets_in_sector: this.test_market.sectors){
      amount_of_assets += this.test_market.curr_market_by_sector.get(assets_in_sector).size();
    }

    Assert.assertEquals(505,amount_of_assets);

  }

  //PORTFOLIO TESTS//

  @Test
  public void test_portfolio_init(){
    Assert.assertEquals(10000.0, this.test_portfolio.starting_amount,.01);
    Assert.assertEquals(0, this.test_portfolio.assets.size());
  }

  /**
   * Tests the portfolios resting state after a simple buy is correct
   */
  @Test
  public void test_buy_success(){
    Asset to_buy = this.test_market.curr_market.get("FB");
    boolean succesful = this.test_portfolio.buy(to_buy,10);
    Assert.assertTrue(succesful);
    Assert.assertEquals(1,this.test_portfolio.assets.size());
    Assert.assertEquals(this.test_portfolio.assets.get("FB").amount, 10);
    Assert.assertEquals(this.test_portfolio.assets.get("FB").buy_price, 171.58,.01);
    Assert.assertEquals(this.test_portfolio.current_amount,8284.2,.01);
    Assert.assertEquals(this.test_portfolio.overall_profit_loss,0,.01);
  }

  /**
   * Tests the portfolios resting state after trying to buy too much of a stock
   */
  @Test
  public void test_buy_fail_too_expensive(){
    Asset to_buy = this.test_market.curr_market.get("FB");
    boolean failure = this.test_portfolio.buy(to_buy,1000);
    Assert.assertFalse(failure);
    Assert.assertEquals(0,this.test_portfolio.assets.size());
    Assert.assertEquals(this.test_portfolio.overall_profit_loss,0,.01);
  }




}


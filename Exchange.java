package trading_game;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Exchange {
  protected Portfolio current_portfolio;
  protected Scanner keyboard;
  protected Market current_market;

  /**
   * Constructor for the Front end creates an empty portfolio and a base amount of cash for the user
   * @param _account_amount the amount of money the user will start with
   */
  public Exchange(double _account_amount, int seed){
    this.current_portfolio = new Portfolio(_account_amount);
    this.keyboard = new Scanner(System.in);
    this.current_market = new Market("financials.csv", seed);
  }

  public void runCommandLoop(){

    String choice = null;

    do {
      display_current_state();
      display_main_menu();
      choice = this.keyboard.nextLine().toUpperCase();

      switch(choice){

        case "E":
          display_exchange();
          asset_transaction();
        case "A":
          this.current_market.next_turn();
          update_portfolio();
        case "P":
          System.out.println(this.current_portfolio);
      }


    } while(!choice.equalsIgnoreCase("Q"));
  }

  /**
   * Displays all Main Menu Options
   */
  public void display_main_menu(){
    String output = "Enter one of the following options:\n"
        + "[E] Exchange\n"
        + "[P] trading_game.Portfolio\n"
        + "[S] Settings\n"
        + "[F] Fast Forward\n"
        + "[C] Create Event\n"
        + "[A] Advance\n"
        + "[Q] Quit\n";

    System.out.println(output);
  }

  public void display_portfolio(){
  }

  public void display_exchange(){
    String display_choice =  null;
    System.out.println("######### EXCHANGE #########");
    System.out.println("Display Assets by: [S]ector, [P]rice, [N]ame");
    display_choice = keyboard.nextLine().toUpperCase();

    switch(display_choice){
      case "S":
        List<String> by_sector = get_assets_by_sector();

        for (int i = 0; i < by_sector.size(); i++) {
          System.out.println(by_sector.get(i));
        }

        //todo delete this
        boolean skip = true;

      case "P":
      case "N":

    }

  }

  /**
   * This will print out the current over
   */
  public void display_current_state(){
    System.out.println("Most recent P/l: " + this.current_portfolio.most_recent_profit_loss_change);
    System.out.println("Current Overall Profit/Loss: " + this.current_portfolio.overall_profit_loss);
  }

  public void display_settings(){
  }

  public void asset_transaction(){
    System.out.println("[B]uy ?");
    String asset_transaction_choice = this.keyboard.nextLine().toUpperCase();

    if(asset_transaction_choice.equalsIgnoreCase("b")){
      System.out.println("Which asset would you like to buy: ");

      String ticker_to_buy = this.keyboard.nextLine().toUpperCase();
      Asset to_buy = this.current_market.assets_in_market.get(ticker_to_buy);

      System.out.println("Amount: ");
      Integer amount_to_buy = Integer.parseInt(keyboard.nextLine());
      this.current_portfolio.buy(to_buy,amount_to_buy);
    }
  }

  public List<String> get_assets_by_sector() {

    List<String> by_sector = new ArrayList<>();
    List<String> sector_title = current_market.sectors.stream().toList();
    String display_sector = "";
    String sector_choice = "";

    for(int i = 0; i < current_market.sectors.size(); i++){
        String line = "[" + Integer.toString(i) + "] " + sector_title.get(i) +"\n";
        display_sector += line;
    }

    System.out.println("Which sector would you like displayed?");
    System.out.println(display_sector);
    sector_choice = keyboard.nextLine();

    String sector = sector_title.get(Integer.parseInt(sector_choice));
    by_sector = current_market.formatListWithPadding(current_market.assets_in_market_by_sector.get(sector));

    return by_sector;
  }

  public void update_portfolio() {

    double curr_pl = this.current_portfolio.overall_profit_loss;

    //updating the price and then adding the new one into the price history
    this.current_portfolio.assets.forEach((key, value) -> {
      current_portfolio.assets.get(key).price = this.current_market.assets_in_market.get(key).price;
    });

    this.current_portfolio.most_recent_profit_loss_change = this.current_portfolio.profit_loss();
    this.current_portfolio.overall_profit_loss += this.current_portfolio.most_recent_profit_loss_change;
  }


}

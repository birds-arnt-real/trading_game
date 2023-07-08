package trading_game;
import java.text.DecimalFormat;
import java.util.List;
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

    String choice;
    do {
      display_current_state();
      display_main_menu();
      choice = this.keyboard.nextLine().toUpperCase();

      switch (choice) {
        case "E" -> {
          display_exchange();
          asset_transaction();
        }
        case "A" -> {
          this.current_market.next_turn();
          update_portfolio();
          System.out.println("DAY : " + this.current_market.number_of_turns+"\n" +
              this.current_portfolio);
        }
        case "S" -> display_exchange_sell();
        case "P" -> System.out.println(this.current_portfolio);
        default -> System.out.println("default");
      }


    } while(!choice.equalsIgnoreCase("Q"));
  }

  /**
   * Displays all Main Menu Options
   */
  public void display_main_menu(){
    String output =
        ("Enter one of the following options:\n[E] Exchange\n[S] Sell\n[P] trading_game"
            + ".Portfolio\n[S] Settings\n[F] Fast Forward\n[C] Create Event\n[A] Advance\n[Q] "
            + "Quit\n");

    System.out.println(output);
  }

  public void display_portfolio(){
  }

  public void display_exchange(){
    String display_choice;
    System.out.println("######### EXCHANGE #########");
    System.out.println("View Assets by: [S]ector, [P]rice, [N]ame");
    display_choice = keyboard.nextLine().toUpperCase();

    switch(display_choice){
      // this displays based on sector
      case "S" -> {
        List<String> by_sector = get_assets_by_sector();

        for (String s : by_sector) {
          System.out.println(s);
        }
      }
      //case "P":

      case "N" -> {
        List<String> total = this.current_market.get_total_market();
        System.out.println("Enter search [N]ame and [Q]uantity: ");
        String[] input = this.keyboard.nextLine().split(" ");
        String ticker = input[0];
        int amount = input.length < 2 ? 5 : Integer.parseInt(input[1]);

        List<String> results = new
            StringSearch().searchSimilarStrings(total,ticker,amount,1);

        for(int i = 0; i < results.size(); i++){
          System.out.println(this.current_market.assets_in_market.get(results.get(i)));
        }
      }

      default -> {}


    }

  }

  public void display_exchange_sell(){

    System.out.println("######### Sell #########");
    System.out.println(this.current_portfolio.toString());
    System.out.println("Enter Ticker to sell and amount (ex ATT 10) or [enter] to return");

    String[] asset_sell_transaction = keyboard.nextLine().toUpperCase().split(" ");
    if(asset_sell_transaction.length <= 1) return;

    String ticker_to_sell = asset_sell_transaction[0];
    int amount_to_sell = Integer.parseInt(asset_sell_transaction[1]);
    this.current_portfolio.sell(ticker_to_sell,amount_to_sell);

    }


  /**
   * This will print out the current over
   */
  public void display_current_state(){
    DecimalFormat decimalFormat = new DecimalFormat("#.00");
    System.out.println("Most recent P/l: " +
        decimalFormat.format(this.current_portfolio.most_recent_profit_loss_change));
    System.out.println("Current Overall Profit/Loss: " +
        decimalFormat.format(this.current_portfolio.overall_profit_loss));
    System.out.println("You have $" + decimalFormat.format(this.current_portfolio.current_amount) +
        " available on your " + this.current_market.number_of_turns + " day.\n");
  }

  public void display_settings(){
  }

  public void asset_transaction(){
    System.out.println("To buy enter ticker and desired amount (ex. ATT 15) or [enter] to "
        + "return\n");
    String[] asset_transaction_choice = this.keyboard.nextLine().toUpperCase().split(" ");
    if(asset_transaction_choice.length <= 1) return;

    String ticker_to_buy = asset_transaction_choice[0];
    int amount_to_buy = Integer.parseInt(asset_transaction_choice[1]);

    Asset to_buy = this.current_market.assets_in_market.get(ticker_to_buy);
    this.current_portfolio.buy(to_buy,amount_to_buy);
    return;
    }

  public List<String> get_assets_by_sector() {

    List<String> by_sector;
    List<String> sector_title = current_market.sectors.stream().toList();
    StringBuilder display_sector = new StringBuilder();
    String sector_choice;

    for(int i = 0; i < current_market.sectors.size(); i++){
        String line = "[" + i + "] " + sector_title.get(i) +"\n";
        display_sector.append(line);
    }

    System.out.println("Which sector would you like displayed?");
    System.out.println(display_sector);
    sector_choice = keyboard.nextLine();

    String sector = sector_title.get(Integer.parseInt(sector_choice));
    by_sector = current_market.formatMarketListWithPadding(current_market.assets_in_market_by_sector.get(sector));

    return by_sector;
  }

  public void update_portfolio() {

    //double curr_pl = this.current_portfolio.overall_profit_loss;

    //updating the price and then adding the new one into the price history
    this.current_portfolio.assets.forEach((key, value) -> {
        current_portfolio.assets.get(key).price =
            this.current_market.assets_in_market.get(key).price;

        this.current_portfolio.profit_loss_individual_asset_per_turn(this.current_market.assets_in_market.get(key));
    });


    this.current_portfolio.most_recent_profit_loss_change = this.current_portfolio.profit_loss_overall_per_turn();
    this.current_portfolio.overall_profit_loss += this.current_portfolio.most_recent_profit_loss_change;
  }


}

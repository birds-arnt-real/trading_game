package trading_game;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Exchange {

  private ArrayList<Asset> portfolio;
  private double account_amount;
  protected Scanner keyboard;
  protected Market current_market;

  /**
   * Constructor for the Front end creates an empty portfolio and a base amount of cash for the user
   * @param _account_amount the amount of money the user will start with
   */
  public Exchange(double _account_amount){
    this.account_amount = _account_amount;
    this.portfolio = new ArrayList<>();
    this.keyboard = new Scanner(System.in);
    this.current_market = new Market("financials.csv");
  }

  public void runCommandLoop(){

    String choice = null;

    do {
      display_main_menu();
      choice = this.keyboard.nextLine().toUpperCase();

      switch(choice){

        case "E":
          display_exchange();



      }


    } while(!choice.equalsIgnoreCase("Q"));
  }

  /**
   * Displays all Main Menu Options
   */
  public void display_main_menu(){
    String output = "Enter one of the following options:\n"
        + "[E] Exchange\n"
        + "[P] Portfolio\n"
        + "[S] Settings\n"
        + "[F] Fast Forward\n"
        + "[C] Create Event\n"
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
        System.out.println(" Ticker       Name        Price  (low,high)");
        for (int i = 0; i < by_sector.size(); i++) {
          System.out.println(by_sector.get(i));
        }

      case "P":
      case "N":

    }

  }



  public void display_settings(){
  }

  public boolean asset_transaction(){
    return false;
  }

  public ArrayList<Asset> getPortfolio() {
    return portfolio;
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
    by_sector = current_market.formatListWithPadding(current_market.curr_market_by_sector.get(sector));
    //by_sector = current_market.curr_market_by_sector.get(sector);

    return by_sector;


  }


}

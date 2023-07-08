package trading_game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;


public class Market {

  protected Random rand_gen;
  protected Hashtable<String,Asset> assets_in_market;
  protected Hashtable<String,List<Asset>> assets_in_market_by_sector;
  protected Set<String> sectors = new HashSet<>();
  protected int number_of_turns;

  /**
   * Creates and assigns values to all assets in asset information file
   *
   * @param file_path where the asset information is coming from
   * @param seed for testing purposes
   */
  public Market(String file_path, int seed){

    this.rand_gen = seed > 0 ? new Random(42) : new Random();
    List<String[]> asset_information = read_csv("financials.csv");
    assets_in_market = new Hashtable<>();
    assets_in_market_by_sector = new Hashtable<>();

    // first we'll make the hashtable by ticker
    for (int i = 1; i < asset_information.size(); i++){

      try {
        String symbol = asset_information.get(i)[0];
        String name = asset_information.get(i)[1];
        String sector = asset_information.get(i)[2];
        Double price = Double.parseDouble(asset_information.get(i)[3]);
        int trend = rand_gen.nextInt(0, 3) - 1;
        double volatility_factor = rand_gen.nextDouble(0, 20) / 100;

        // this handles the market by ticker
        Asset curr_asset = new Asset(symbol, sector, name, price, trend, volatility_factor);
        assets_in_market.put(symbol, curr_asset);

        // this handles it by sectors
        sectors.add(sector);

        // Retrieve the list from the Hashtable
        List<Asset> asset_list = assets_in_market_by_sector.get(sector);

        // Check if the list exists or create a new one if it doesn't
        if (asset_list == null) {
          asset_list = new ArrayList<>();
        }

        // Add a new item to the list
        asset_list.add(curr_asset);

        // Update the value in the Hashtable
        assets_in_market_by_sector.put(sector, asset_list);

        // first turn so we start at 1
        this.number_of_turns = 1;

      } catch (Exception e){
          System.out.println(asset_information.get(i)[0]+" failed loading");
          e.printStackTrace();
      }
    }
  }

  /**
   * Reads in the initial CSV of asset information
   * 0 = symbol, 1 = name, 2 = sector, 3 = price, 7 = 52 week high, 8 = 52 week low
   * @param file_path name of CSV with asset information
   * @return complete list of asset info, ignore first one as it has header info
   */
  public static List<String[]> read_csv(String file_path) {
    List<String[]> data = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
      String line;
      while ((line = br.readLine()) != null) {
        // Split the line by comma
        String[] values = line.split(",");
        data.add(values);
      }
    } catch (IOException e) {
      return null;
    }
    return data;
  }

  /**
   * Takes in a list of assets and formats output with left justification and a line break
   *   made out of equal signs
   * @param assets assets to be formated and displayed
   * @return List of asset strings with appropriate and relative spacing
   */
  public List<String> formatMarketListWithPadding(List<Asset> assets) {
    // Split each string into sections
    List<List<String>> stringSections = new ArrayList<>();
    List<String> output = new ArrayList<>();

    // Find the maximum length for each section
    int[] max_lengths = new int[4];
    for(Asset asset : assets){
      String[] curr_asset = asset.toArray();
      max_lengths[0] = Math.max(6,curr_asset[0].length());
      max_lengths[1] = Math.max(max_lengths[1],curr_asset[1].length());
      max_lengths[2] = Math.max(max_lengths[2],curr_asset[2].length());
      max_lengths[3] = Math.max(max_lengths[3],curr_asset[3].length());
    }

    for(Asset asset: assets){
      String[] curr_asset = asset.toArray();

      String formatted_output = String.format("%-" + max_lengths[0] + "s" + "      "  +
                                              "%-" + max_lengths[1] + "s" + "   "     +
                                              "%-" + max_lengths[2] + "s" + "       " +
                                              "%-" + max_lengths[3] + "s",
                                              curr_asset[0],
                                              curr_asset[1],
                                              curr_asset[2],
                                              curr_asset[3]);
      output.add(formatted_output);

    }

    // add header and display line
    String ticker_name_space = " ".repeat(6);
    String name_price_space = " ".repeat(3);
    String price_highlow_space = " ".repeat(7);
    String line_break = "=".repeat(77);

    String col_labels = String.format("%-" + max_lengths[0] + "s" + ticker_name_space  +
                                      "%-" + max_lengths[1] + "s" + name_price_space     +
                                      "%-" + max_lengths[2] + "s" + price_highlow_space +
                                      "%-" + max_lengths[3] + "s",
                                      "Ticker",
                                      "Name",
                                      "Price",
                                      "(low, high)");

    output.add(0,col_labels);
    output.add(1,line_break);

    return output;
  }

  /**
   * This advances to the next turn/day.  This will update all prices of assets
   *   in the market.  This will not handle the actual logic of how much the price increases
   * @return true if everything was updated, false if not.
   */
  public boolean next_turn() {
    // first we need to update all of the assets
    try {
      this.assets_in_market.forEach((key, value) -> {
        // the initial price is already added into the price history
//        if(value.price_history.size() == 0) {
//          value.price_history.add(value.price);
//        }
        price_change(this.assets_in_market.get(key));

      });

      this.number_of_turns++;

      return true;
    } catch(Exception e){
      return false;
    }
  }

  /**
   * This will hold the logic for price updates.  There must be some sort of reason behind
   *   how much and in what direction an asset's price will go.  In a perfect world they change
   *   would follow these rules
   *   1. the direction should have a larger chance of continuing its direction than switching
   *   2. the max amount of price change is 20%
   *   3. but maybe sometimes it can be more?
   *   4.
   * @param asset_to_update
   * @return
   */
  public boolean price_change(Asset asset_to_update){

    double curr_price = asset_to_update.price;
    boolean wild_card = this.rand_gen.nextInt(100) % 11 == 0;
    double amount_to_increase = asset_to_update.price * asset_to_update.volatility_factor;

    if(wild_card){
      amount_to_increase += (curr_price * ((this.rand_gen.nextDouble(1))*
          asset_to_update.volatility_factor));
    }

    asset_to_update.trend_direction = rand_gen.nextInt(0, 3) - 1;
    asset_to_update.volatility_factor = rand_gen.nextDouble(0, .2);
    double new_price =
        asset_to_update.price += (amount_to_increase * asset_to_update.trend_direction);
    asset_to_update.price = new_price;
    asset_to_update.price_history.add(new_price);

    return true;
  }

  /**
   * Get total list of tickers in string
   * @return
   */

  public List<String> get_total_market(){
    List<String> total = new ArrayList<>(assets_in_market.keySet());
    return (List<String>) total;
    }
  }


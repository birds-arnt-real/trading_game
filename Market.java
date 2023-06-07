package trading_game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;


public class Market {

  protected Random rand_gen;
  protected Hashtable<String,Asset> curr_market;
  protected Hashtable<String,List<Asset>> curr_market_by_sector;
  protected Set<String> sectors = new HashSet<String>();

  /**
   * Creates and assigns values to all assets in asset information file
   *
   * @param file_path where the asset information is coming from
   */
  public Market(String file_path){

    this.rand_gen = new Random();
    List<String[]> asset_information = read_csv("financials.csv");
    curr_market = new Hashtable<>();
    curr_market_by_sector = new Hashtable<>();

    // first we'll make the hashtable by ticker
    for (int i = 1; i < asset_information.size(); i++){

      try {
        String symbol = asset_information.get(i)[0];
        String name = asset_information.get(i)[1];
        String sector = asset_information.get(i)[2];
        Double price = Double.parseDouble(asset_information.get(i)[3]);
        int trend = rand_gen.nextInt(0, 3) - 1;
        int volatility_factor = rand_gen.nextInt(0, 20);

        // this handles the market by ticker
        Asset curr_asset = new Asset(symbol, sector, name, price, trend, volatility_factor);
        curr_market.put(symbol, curr_asset);

        // this handles it by sectors
        sectors.add(sector);

        // Retrieve the list from the Hashtable
        List<Asset> asset_list = curr_market_by_sector.get(sector);

        // Check if the list exists or create a new one if it doesn't
        if (asset_list == null) {
          asset_list = new ArrayList<>();
        }

        // Add a new item to the list
        asset_list.add(curr_asset);

        // Update the value in the Hashtable
        curr_market_by_sector.put(sector, asset_list);

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
      e.printStackTrace();
    }
    return data;
  }

  public List<String> formatListWithPadding(List<Asset> assets) {
    // Split each string into sections
    List<List<String>> stringSections = new ArrayList<>();
    List<String> output = new ArrayList<>();

    // Find the maximum length for each section
    int[] max_lengths = new int[4];
    for(Asset asset : assets){
      String[] curr_asset = asset.toArray();
      max_lengths[0] = Math.max(max_lengths[0],curr_asset[0].length());
      max_lengths[1] = Math.max(max_lengths[1],curr_asset[1].length());
      max_lengths[2] = Math.max(max_lengths[2],curr_asset[2].length());
      max_lengths[3] = Math.max(max_lengths[3],curr_asset[3].length());
    }

    for(Asset asset: assets){
      String[] curr_asset = asset.toArray();

      String formatted_output = String.format("%-" + max_lengths[0] + "s" +
          "         %-" + max_lengths[1] + "s" + "  %-" + max_lengths[2] + "s" + "    %-" + max_lengths[3] +
              "s",
          curr_asset[0],curr_asset[1],curr_asset[2],curr_asset[3]);
      output.add(formatted_output);

    }

    return output;
  }
}

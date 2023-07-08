package trading_game;

import java.util.ArrayList;
import java.util.List;

public class StringSearch {
  public List<String> searchSimilarStrings(List<String> strings, String query, int n, double similarityPercentage) {
    List<SimilarityPair> similarityPairs = new ArrayList<>();

    // Calculate the similarity percentage for each string and the query
    for (String str : strings) {
      double similarity = calculateSimilarityPercentage(str, query);
      if (similarity >= similarityPercentage) {
        similarityPairs.add(new SimilarityPair(str, similarity));
      }
    }

    // Sort the similarity pairs based on the similarity percentage in descending order
    similarityPairs.sort((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()));

    // Extract the N most similar strings from the sorted list
    List<String> result = new ArrayList<>();
    for (int i = 0; i < Math.min(n, similarityPairs.size()); i++) {
      result.add(similarityPairs.get(i).getString());
    }

    return result;
  }

  private double calculateSimilarityPercentage(String str1, String str2) {
    int maxLength = Math.max(str1.length(), str2.length());
    int distance = calculateLevenshteinDistance(str1, str2);
    double similarity = (double) (maxLength - distance) / maxLength * 100;
    return similarity;
  }

  private int calculateLevenshteinDistance(String str1, String str2) {
    int[][] dp = new int[str1.length() + 1][str2.length() + 1];

    for (int i = 0; i <= str1.length(); i++) {
      dp[i][0] = i;
    }

    for (int j = 0; j <= str2.length(); j++) {
      dp[0][j] = j;
    }

    for (int i = 1; i <= str1.length(); i++) {
      for (int j = 1; j <= str2.length(); j++) {
        if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
          dp[i][j] = dp[i - 1][j - 1];
        } else {
          dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i][j - 1], dp[i - 1][j]));
        }
      }
    }

    return dp[str1.length()][str2.length()];
  }

  private static class SimilarityPair {
    private final String string;
    private final double similarity;

    public SimilarityPair(String string, double similarity) {
      this.string = string;
      this.similarity = similarity;
    }

    public String getString() {
      return string;
    }

    public double getSimilarity() {
      return similarity;
    }
  }
}

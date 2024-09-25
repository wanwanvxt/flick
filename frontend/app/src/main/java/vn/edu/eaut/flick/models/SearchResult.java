package vn.edu.eaut.flick.models;

import java.util.ArrayList;

public class SearchResult {
  private int currentPage;
  private boolean hasNextPage;
  private ArrayList<MovieResult> results;

  public int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public ArrayList<MovieResult> getResults() {
    return results;
  }

  public void setResults(ArrayList<MovieResult> results) {
    this.results = results;
  }

  public boolean hasNextPage() {
    return hasNextPage;
  }

  public void setHasNextPage(boolean hasNextPage) {
    this.hasNextPage = hasNextPage;
  }
}

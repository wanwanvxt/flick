package vn.edu.eaut.flick.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import java.util.ArrayList;
import vn.edu.eaut.flick.R;
import vn.edu.eaut.flick.controllers.MovieController;
import vn.edu.eaut.flick.models.MovieResult;
import vn.edu.eaut.flick.views.MovieInfoActivity;
import vn.edu.eaut.flick.views.comps.MovieCardViewAdapter;

public class HomeFragment extends Fragment {
  //
  private CircularProgressIndicator progressFetchingTrending;
  private CircularProgressIndicator progressFetchingRecentTVShows;
  private CircularProgressIndicator progressFetchingRecentMovies;
  //
  private RecyclerView recyclerViewTrending;
  private RecyclerView recyclerViewRecentTVShows;
  private RecyclerView recyclerViewRecentMovies;
  //
  private MovieController movieController;
  private ArrayList<MovieResult> trendingMovieResults;
  private ArrayList<MovieResult> recentTVShowResults;
  private ArrayList<MovieResult> recentMovieResults;
  //
  private MovieCardViewAdapter trendingResultAdapter;
  private MovieCardViewAdapter recentTVShowResultAdapter;
  private MovieCardViewAdapter recentMovieResultAdapter;

  public HomeFragment() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    initialize(view);
    displayTrendingMovies();
    displayRecentTVShows();
    displayRecentMovies();
    return view;
  }

  private void initialize(View view) {
    progressFetchingTrending = view.findViewById(R.id.progressFetchingTrending);
    progressFetchingRecentTVShows = view.findViewById(R.id.progressFetchingRecentTVShows);
    progressFetchingRecentMovies = view.findViewById(R.id.progressFetchingRecentMovies);
    //
    recyclerViewTrending = view.findViewById(R.id.recyclerViewTrending);
    recyclerViewRecentTVShows = view.findViewById(R.id.recyclerViewRecentTVShows);
    recyclerViewRecentMovies = view.findViewById(R.id.recyclerViewRecentMovies);
    //
    movieController = new MovieController(requireContext());
    trendingMovieResults = new ArrayList<>();
    recentTVShowResults = new ArrayList<>();
    recentMovieResults = new ArrayList<>();
    //
    trendingResultAdapter = new MovieCardViewAdapter(trendingMovieResults);
    trendingResultAdapter.setOnItemClickListener((v, pos) -> handleClickMovieCardView(pos, CardViewType.TRENDING));
    recyclerViewTrending.setAdapter(trendingResultAdapter);
    recyclerViewTrending.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
    recyclerViewTrending.addItemDecoration(new MovieCardViewAdapter.HorizontalItemDecoration((int) getResources().getDimension(R.dimen.movie_card_view_space)));
    //
    recentTVShowResultAdapter = new MovieCardViewAdapter(recentTVShowResults);
    recentTVShowResultAdapter.setOnItemClickListener((v, pos) -> handleClickMovieCardView(pos, CardViewType.RECENT_TVSHOW));
    recyclerViewRecentTVShows.setAdapter(recentTVShowResultAdapter);
    recyclerViewRecentTVShows.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
    recyclerViewRecentTVShows.addItemDecoration(new MovieCardViewAdapter.HorizontalItemDecoration((int) getResources().getDimension(R.dimen.movie_card_view_space)));
    //
    recentMovieResultAdapter = new MovieCardViewAdapter(recentMovieResults);
    recentMovieResultAdapter.setOnItemClickListener((v, pos) -> handleClickMovieCardView(pos, CardViewType.RECENT_MOVIE));
    recyclerViewRecentMovies.setAdapter(recentMovieResultAdapter);
    recyclerViewRecentMovies.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
    recyclerViewRecentMovies.addItemDecoration(new MovieCardViewAdapter.HorizontalItemDecoration((int) getResources().getDimension(R.dimen.movie_card_view_space)));
  }

  private void displayTrendingMovies() {
    progressFetchingTrending.setVisibility(View.VISIBLE);
    recyclerViewTrending.setVisibility(View.GONE);

    movieController.fetchTrendingMovies("movie", new MovieController.CallbackResponse<ArrayList<MovieResult>>() {
      @Override
      public void onSuccess(ArrayList<MovieResult> result) {
        int oldSize = trendingMovieResults.size();
        trendingMovieResults.clear();
        trendingResultAdapter.notifyItemRangeRemoved(0, oldSize);

        trendingMovieResults.addAll(result);
        trendingResultAdapter.notifyItemRangeInserted(0, result.size());
        progressFetchingTrending.setVisibility(View.GONE);
        recyclerViewTrending.setVisibility(View.VISIBLE);
      }

      @Override
      public void onError(String errorMsg) {
        Log.e("HomeFragment_Trending", errorMsg);
      }
    });
  }

  private void displayRecentTVShows() {
    progressFetchingRecentTVShows.setVisibility(View.VISIBLE);
    recyclerViewRecentTVShows.setVisibility(View.GONE);

    movieController.fetchRecentMovies("tvshow", new MovieController.CallbackResponse<ArrayList<MovieResult>>() {
      @Override
      public void onSuccess(ArrayList<MovieResult> result) {
        int oldSize = recentTVShowResults.size();
        recentTVShowResults.clear();
        recentTVShowResultAdapter.notifyItemRangeRemoved(0, oldSize);

        recentTVShowResults.addAll(result);
        recentTVShowResultAdapter.notifyItemRangeInserted(0, result.size());
        progressFetchingRecentTVShows.setVisibility(View.GONE);
        recyclerViewRecentTVShows.setVisibility(View.VISIBLE);
      }

      @Override
      public void onError(String errorMsg) {
        Log.e("HomeFragment_RecentTVShows", errorMsg);
      }
    });
  }

  private void displayRecentMovies() {
    progressFetchingRecentMovies.setVisibility(View.VISIBLE);
    recyclerViewRecentMovies.setVisibility(View.GONE);

    movieController.fetchRecentMovies("movie", new MovieController.CallbackResponse<ArrayList<MovieResult>>() {
      @Override
      public void onSuccess(ArrayList<MovieResult> result) {
        int oldSize = recentMovieResults.size();
        recentMovieResults.clear();
        recentMovieResultAdapter.notifyItemRangeRemoved(0, oldSize);

        recentMovieResults.addAll(result);
        recentMovieResultAdapter.notifyItemRangeInserted(0, result.size());
        progressFetchingRecentMovies.setVisibility(View.GONE);
        recyclerViewRecentMovies.setVisibility(View.VISIBLE);
      }

      @Override
      public void onError(String errorMsg) {
        Log.e("HomeFragment_RecentMovies", errorMsg);
      }
    });
  }


  private enum CardViewType {
    TRENDING, RECENT_MOVIE, RECENT_TVSHOW
  }

  private void handleClickMovieCardView(int pos, CardViewType type) {
    Intent movieInfoIntent = new Intent(requireContext(), MovieInfoActivity.class);
    MovieResult clickedCardViewResult = null;
    switch (type) {
      case TRENDING:
        clickedCardViewResult = trendingMovieResults.get(pos);
        break;
      case RECENT_MOVIE:
        clickedCardViewResult = recentMovieResults.get(pos);
        break;
      case RECENT_TVSHOW:
        clickedCardViewResult = recentTVShowResults.get(pos);
        break;
    }

    if (clickedCardViewResult != null) {
      movieInfoIntent.putExtra("MOVIE_ID", clickedCardViewResult.getId());
      startActivity(movieInfoIntent);
    }
  }
}

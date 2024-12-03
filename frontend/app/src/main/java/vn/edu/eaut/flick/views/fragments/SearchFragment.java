package vn.edu.eaut.flick.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import java.util.ArrayList;
import java.util.Objects;
import vn.edu.eaut.flick.R;
import vn.edu.eaut.flick.controllers.MovieController;
import vn.edu.eaut.flick.models.MovieResult;
import vn.edu.eaut.flick.models.SearchResult;
import vn.edu.eaut.flick.views.MovieInfoActivity;
import vn.edu.eaut.flick.views.comps.MovieCardViewAdapter;

public class SearchFragment extends Fragment {
  private SearchView searchView;
  private CircularProgressIndicator progressSearching;
  private RecyclerView recyclerViewSearchResult;
  private TextView noResultsTextView;
  //
  private MovieController movieController;
  private ArrayList<MovieResult> searchMovieResults;
  //
  private MovieCardViewAdapter searchResultAdapter;
  //
  private String searchQuery = "";
  private boolean hasNextPage = false;
  private int currentPage = 1;
  private boolean isFetchingNextPage = false;

  public SearchFragment() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_search, container, false);
    initialize(view);
    return view;
  }

  private void initialize(View view) {
    searchView = view.findViewById(R.id.searchView);
    progressSearching = view.findViewById(R.id.progressSearching);
    recyclerViewSearchResult = view.findViewById(R.id.recyclerViewSearchResult);
    noResultsTextView = view.findViewById(R.id.noResultsTextView);
    //
    movieController = new MovieController(requireContext());
    searchMovieResults = new ArrayList<>();
    searchResultAdapter = new MovieCardViewAdapter(searchMovieResults);
    searchResultAdapter.setOnItemClickListener((v, pos) -> handleClickMovieCardView(pos));
    recyclerViewSearchResult.setAdapter(searchResultAdapter);
    FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(requireContext());
    flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
    flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
    flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
    recyclerViewSearchResult.setLayoutManager(flexboxLayoutManager);
    recyclerViewSearchResult.addItemDecoration(new MovieCardViewAdapter.FlexboxItemDecoration((int) getResources().getDimension(R.dimen.movie_card_view_space)));
    recyclerViewSearchResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        handleRecyclerScrollToEnd();
      }
    });

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String s) {
        searchView.clearFocus();
        String query = (Objects.toString(searchView.getQuery(), "")).trim();
        if (!query.isEmpty()) {
          searchQuery = query;
          displaySearchResult(true);
          return true;
        }
        return false;
      }

      @Override
      public boolean onQueryTextChange(String s) {
        return false;
      }
    });
  }

  private void handleClickMovieCardView(int pos) {
    Intent movieInfoIntent = new Intent(requireContext(), MovieInfoActivity.class);
    MovieResult clickedCardViewResult = searchMovieResults.get(pos);
    movieInfoIntent.putExtra("MOVIE_ID", clickedCardViewResult.getId());
    startActivity(movieInfoIntent);
  }

  private void displaySearchResult(boolean isFirstSearch) {
    if (isFirstSearch) {
      currentPage = 1;
      progressSearching.setVisibility(View.VISIBLE);
      recyclerViewSearchResult.setVisibility(View.GONE);
      noResultsTextView.setVisibility(View.GONE);
      recyclerViewSearchResult.scrollToPosition(0);

      movieController.search(searchQuery, 1, new MovieController.CallbackResponse<SearchResult>() {
        @Override
        public void onSuccess(SearchResult result) {
          int oldSize = searchMovieResults.size();

          if (result.getResults().isEmpty()){
            progressSearching.setVisibility(View.GONE);
            recyclerViewSearchResult.setVisibility(View.GONE);
            noResultsTextView.setVisibility(View.VISIBLE);
          }

          searchMovieResults.clear();
          searchResultAdapter.notifyItemRangeRemoved(0, oldSize);

          searchMovieResults.addAll(result.getResults());
          searchResultAdapter.notifyItemRangeInserted(0, result.getResults().size());
          hasNextPage = result.hasNextPage();
          progressSearching.setVisibility(View.GONE);
          recyclerViewSearchResult.setVisibility(View.VISIBLE);
        }

        @Override
        public void onError(String errorMsg) {
          Log.e("SearchFragment", errorMsg);
        }
      });
    } else {
      if (isFetchingNextPage) return;

      currentPage += 1;
      isFetchingNextPage = true;
      movieController.search(searchQuery, currentPage, new MovieController.CallbackResponse<SearchResult>() {
        @Override
        public void onSuccess(SearchResult result) {
          int oldSize = searchMovieResults.size();

          if (result.getResults().isEmpty()){
            isFetchingNextPage = false;
            return;
          }

          searchMovieResults.addAll(result.getResults());
          hasNextPage = result.hasNextPage();
          isFetchingNextPage = false;
          searchResultAdapter.notifyItemRangeInserted(oldSize, result.getResults().size());
        }

        @Override
        public void onError(String errorMsg) {
          Log.e("SearchFragment", errorMsg);
        }
      });
    }
  }

  private void handleRecyclerScrollToEnd() {
    if (hasNextPage && !isFetchingNextPage) {
      FlexboxLayoutManager flexboxLayoutManager = (FlexboxLayoutManager) recyclerViewSearchResult.getLayoutManager();
      if (flexboxLayoutManager != null && flexboxLayoutManager.findLastCompletelyVisibleItemPosition() >= searchMovieResults.size() - 1) {
        displaySearchResult(false);
      }
    }
  }
}

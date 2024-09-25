package vn.edu.eaut.flick.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import java.util.ArrayList;
import vn.edu.eaut.flick.R;
import vn.edu.eaut.flick.controllers.MovieController;
import vn.edu.eaut.flick.models.MovieResult;
import vn.edu.eaut.flick.models.SearchResult;
import vn.edu.eaut.flick.views.comps.MovieCardViewAdapter;

public class SearchByGenreActivity extends AppCompatActivity {
  private TextView labelHeadingGenre;
  private CircularProgressIndicator progressSearching;
  private RecyclerView recyclerViewSearchResult;
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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_search_by_genre);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    initialize();
    displayMovieByGenre(true);
  }

  private void initialize() {
    labelHeadingGenre = findViewById(R.id.labelHeadingGenre);
    progressSearching = findViewById(R.id.progressSearching);
    recyclerViewSearchResult = findViewById(R.id.recyclerViewSearchResult);
    //
    movieController = new MovieController(this);
    searchMovieResults = new ArrayList<>();
    searchResultAdapter = new MovieCardViewAdapter(searchMovieResults);
    searchResultAdapter.setOnItemClickListener((v, pos) -> handleClickMovieCardView(pos));
    recyclerViewSearchResult.setAdapter(searchResultAdapter);
    FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
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

    searchQuery = getIntent().getStringExtra("GENRE");
    labelHeadingGenre.setText(
      HtmlCompat.fromHtml("Genre: <i>" + searchQuery + "</i>",
        HtmlCompat.FROM_HTML_MODE_LEGACY)
    );

    getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
      @Override
      public void handleOnBackPressed() {
        finish();
      }
    });
  }

  private void handleRecyclerScrollToEnd() {
    if (hasNextPage && !isFetchingNextPage) {
      FlexboxLayoutManager flexboxLayoutManager = (FlexboxLayoutManager) recyclerViewSearchResult.getLayoutManager();
      if (flexboxLayoutManager != null && flexboxLayoutManager.findLastCompletelyVisibleItemPosition() >= searchMovieResults.size() - 1) {
        displayMovieByGenre(false);
      }
    }
  }

  private void handleClickMovieCardView(int pos) {
    Intent movieInfoIntent = new Intent(this, MovieInfoActivity.class);
    MovieResult clickedCardViewResult = searchMovieResults.get(pos);
    movieInfoIntent.putExtra("MOVIE_ID", clickedCardViewResult.getId());
    startActivity(movieInfoIntent);
  }

  private void displayMovieByGenre(boolean isFirstSearch) {
    if (isFirstSearch) {
      currentPage = 1;
      progressSearching.setVisibility(View.VISIBLE);
      recyclerViewSearchResult.setVisibility(View.GONE);
      recyclerViewSearchResult.scrollToPosition(0);

      movieController.searchByGenre(searchQuery, 1, new MovieController.CallbackResponse<SearchResult>() {
        @Override
        public void onSuccess(SearchResult result) {
          int oldSize = searchMovieResults.size();
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
      movieController.searchByGenre(searchQuery, currentPage, new MovieController.CallbackResponse<SearchResult>() {
        @Override
        public void onSuccess(SearchResult result) {
          int oldSize = searchMovieResults.size();
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
}

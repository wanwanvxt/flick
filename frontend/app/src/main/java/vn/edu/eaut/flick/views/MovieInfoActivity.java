package vn.edu.eaut.flick.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.squareup.picasso.Picasso;
import java.text.MessageFormat;
import java.util.ArrayList;
import vn.edu.eaut.flick.R;
import vn.edu.eaut.flick.controllers.MovieController;
import vn.edu.eaut.flick.helpers.BookmarksDbHelper;
import vn.edu.eaut.flick.models.MovieInfo;
import vn.edu.eaut.flick.views.comps.TagViewAdapter;

public class MovieInfoActivity extends AppCompatActivity {
  private CircularProgressIndicator progressFetchingInfo;
  private LinearLayout layoutMovieInfo;
  private ImageView imageMovie;
  private ImageView imageMovie2;
  private TextView labelName;
  private TextView labelOverview;
  private TextView labelCasts;
  private TextView labelProduction;
  private TextView labelDescription;
  private MaterialButton buttonWatch;
  private MaterialButton buttonBookmark;
  private RecyclerView recyclerViewMovieGenres;
  //
  private MovieController movieController;
  private MovieInfo movieInfo;
  private ArrayList<String> movieGenres;
  private TagViewAdapter genreTagViewAdapter;
  //
  private BookmarksDbHelper bookmarksDbHelper;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_movie_info);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    initialize();
    displayMovieInfo(getIntent().getStringExtra("MOVIE_ID"));
  }

  private void initialize() {
    progressFetchingInfo = findViewById(R.id.progressFetchingInfo);
    layoutMovieInfo = findViewById(R.id.layoutMovieInfo);
    imageMovie = findViewById(R.id.imageMovie);
    imageMovie2 = findViewById(R.id.imageMovie2);
    labelName = findViewById(R.id.labelName);
    labelOverview = findViewById(R.id.labelOverview);
    labelCasts = findViewById(R.id.labelCasts);
    labelProduction = findViewById(R.id.labelProduction);
    labelDescription = findViewById(R.id.labelDescription);
    recyclerViewMovieGenres = findViewById(R.id.recyclerViewMovieGenres);
    buttonWatch = findViewById(R.id.buttonWatch);
    buttonBookmark = findViewById(R.id.buttonBookmark);
    recyclerViewMovieGenres = findViewById(R.id.recyclerViewMovieGenres);
    //
    movieController = new MovieController(this);
    movieGenres = new ArrayList<>();
    genreTagViewAdapter = new TagViewAdapter(movieGenres);
    genreTagViewAdapter.setOnItemClickListener((v, pos) -> handleClickGenreTagView(pos));
    recyclerViewMovieGenres.setAdapter(genreTagViewAdapter);
    //
    bookmarksDbHelper = new BookmarksDbHelper(this);

    FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
    layoutManager.setFlexDirection(FlexDirection.ROW);
    layoutManager.setFlexWrap(FlexWrap.WRAP);
    layoutManager.setJustifyContent(JustifyContent.FLEX_START);
    recyclerViewMovieGenres.setLayoutManager(layoutManager);

    FlexboxItemDecoration itemDecoration = new FlexboxItemDecoration(this);
    itemDecoration.setOrientation(FlexboxItemDecoration.BOTH);
    itemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.tag_divider_space, null));
    recyclerViewMovieGenres.addItemDecoration(itemDecoration);

    setButtonBookmarkIcon(
      bookmarksDbHelper.existMovie(getIntent().getStringExtra("MOVIE_ID"))
    );

    buttonWatch.setOnClickListener(v -> watch());
    buttonBookmark.setOnClickListener(v -> addRemoveBookmark());
    getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
      @Override
      public void handleOnBackPressed() {
        finish();
      }
    });
  }

  private void setButtonBookmarkIcon(boolean isAdded) {
    if (isAdded) {
      buttonBookmark.setContentDescription(getString(R.string.button_bookmark_added));
      buttonBookmark.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_round_bookmark_added_24));
    } else {
      buttonBookmark.setContentDescription(getString(R.string.button_bookmark_added));
      buttonBookmark.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_round_bookmark_add_24));
    }
  }

  private void handleClickGenreTagView(int pos) {
    Intent genreIntent = new Intent(this, SearchByGenreActivity.class);
    String clickedGenre = movieGenres.get(pos);
    genreIntent.putExtra("GENRE", clickedGenre);
    startActivity(genreIntent);
  }

  private void displayMovieInfo(String movieId) {
    buttonBookmark.setEnabled(false);
    buttonWatch.setEnabled(false);
    progressFetchingInfo.setVisibility(View.VISIBLE);
    layoutMovieInfo.setVisibility(View.GONE);

    movieController.fetchMovieInfo(movieId, new MovieController.CallbackResponse<MovieInfo>() {
      @Override
      public void onSuccess(MovieInfo result) {
        movieInfo = result;

        int oldSize = movieGenres.size();
        movieGenres.clear();
        genreTagViewAdapter.notifyItemRangeRemoved(0, oldSize);

        movieGenres.addAll(result.getGenres());
        genreTagViewAdapter.notifyItemRangeInserted(0, result.getGenres().size());

        buttonBookmark.setEnabled(true);
        buttonWatch.setEnabled(true);
        progressFetchingInfo.setVisibility(View.GONE);
        layoutMovieInfo.setVisibility(View.VISIBLE);

        Picasso.get()
          .load(result.getImage())
          .placeholder(R.drawable.img_movie_placeholder)
          .error(R.drawable.img_movie_placeholder)
          .into(imageMovie);
        imageMovie.setContentDescription(result.getTitle());
        Picasso.get()
          .load(result.getImage())
          .placeholder(R.drawable.img_movie_placeholder)
          .error(R.drawable.img_movie_placeholder)
          .into(imageMovie2);
        imageMovie2.setContentDescription(result.getTitle());
        labelName.setText(result.getTitle());
        labelOverview.setText(MessageFormat.format("{0} | {1} | {2}", result.getRating(), result.getDuration(), result.getReleaseDate()));
        labelCasts.setText(
          HtmlCompat.fromHtml(
            MessageFormat.format("<b>Casts:</b> <i>{0}</i>", String.join(", ", result.getCasts())),
            HtmlCompat.FROM_HTML_MODE_LEGACY)
        );
        labelProduction.setText(
          HtmlCompat.fromHtml(
            MessageFormat.format("<b>Production:</b> <i>{0}</i>", result.getProduction()),
            HtmlCompat.FROM_HTML_MODE_LEGACY
          )
        );
        labelDescription.setText(
          HtmlCompat.fromHtml(
            MessageFormat.format("<b>Description:</b> <i>{0}</i>", result.getDescription()),
            HtmlCompat.FROM_HTML_MODE_LEGACY
          )
        );
      }

      @Override
      public void onError(String errorMsg) {
        Log.e("MovieInfoActivity", errorMsg);
      }
    });
  }

  private void addRemoveBookmark() {
    boolean isAdded = bookmarksDbHelper.existMovie(movieInfo.getId());
    if (isAdded) {
      if (bookmarksDbHelper.removeMovie(movieInfo.getId())) {
        setButtonBookmarkIcon(false);
        Toast.makeText(this, "Removed to bookmarks successfully", Toast.LENGTH_SHORT).show();
      }
    } else {
      if (bookmarksDbHelper.insertMovie(movieInfo.getId(), movieInfo.getTitle(), movieInfo.getImage())) {
        setButtonBookmarkIcon(true);
        Toast.makeText(this, "Added from bookmarks successfully", Toast.LENGTH_SHORT).show();
      }
    }
  }

  private void watch() {
    Intent watchIntent = new Intent(this, WatchActivity.class);
    watchIntent.putExtra("MOVIE_ID", movieInfo.getId());
    watchIntent.putParcelableArrayListExtra("EPISODES", movieInfo.getEpisodes());
//    startActivity(watchIntent);
  }
}

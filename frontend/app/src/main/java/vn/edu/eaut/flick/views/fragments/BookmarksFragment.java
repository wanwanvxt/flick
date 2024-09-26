package vn.edu.eaut.flick.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import java.util.ArrayList;
import vn.edu.eaut.flick.R;
import vn.edu.eaut.flick.helpers.BookmarksDbHelper;
import vn.edu.eaut.flick.models.MovieResult;
import vn.edu.eaut.flick.views.MovieInfoActivity;
import vn.edu.eaut.flick.views.comps.MovieCardViewAdapter;

public class BookmarksFragment extends Fragment {
  private CircularProgressIndicator progressSearching;
  private LinearLayout layoutLabelOops;
  private RecyclerView recyclerViewBookmarks;
  //
  private BookmarksDbHelper bookmarksDbHelper;
  //
  private ArrayList<MovieResult> bookmarks;
  private MovieCardViewAdapter bookmarksAdapter;

  public BookmarksFragment() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
    displayBookmarks();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
    initialize(view);
    displayBookmarks();
    return view;
  }

  private void initialize(View view) {
    progressSearching = view.findViewById(R.id.progressSearching);
    layoutLabelOops = view.findViewById(R.id.layoutLabelOops);
    recyclerViewBookmarks = view.findViewById(R.id.recyclerViewBookmarks);
    //
    bookmarksDbHelper = new BookmarksDbHelper(requireContext());
    //
    bookmarks = new ArrayList<>();
    bookmarksAdapter = new MovieCardViewAdapter(bookmarks);
    bookmarksAdapter.setOnItemClickListener((v, pos) -> handleClickMovieCardView(pos));
    recyclerViewBookmarks.setAdapter(bookmarksAdapter);
    FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(requireContext());
    flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
    flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
    flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
    recyclerViewBookmarks.setLayoutManager(flexboxLayoutManager);
    recyclerViewBookmarks.addItemDecoration(new MovieCardViewAdapter.FlexboxItemDecoration((int) getResources().getDimension(R.dimen.movie_card_view_space)));
  }

  private void displayBookmarks() {
    progressSearching.setVisibility(View.VISIBLE);
    layoutLabelOops.setVisibility(View.GONE);
    recyclerViewBookmarks.setVisibility(View.GONE);

    bookmarksDbHelper.getAllMoviesAsync(requireActivity(), result -> {
      if (!result.isEmpty()) {
        progressSearching.setVisibility(View.GONE);
        layoutLabelOops.setVisibility(View.GONE);
        recyclerViewBookmarks.setVisibility(View.VISIBLE);

        int oldSize = bookmarks.size();
        bookmarks.clear();
        bookmarksAdapter.notifyItemRangeRemoved(0, oldSize);

        bookmarks.addAll(result);
        bookmarksAdapter.notifyItemRangeInserted(0, result.size());
      } else {
        progressSearching.setVisibility(View.GONE);
        layoutLabelOops.setVisibility(View.VISIBLE);
        recyclerViewBookmarks.setVisibility(View.GONE);
      }
    });
  }

  private void handleClickMovieCardView(int pos) {
    Intent movieInfoIntent = new Intent(requireContext(), MovieInfoActivity.class);
    MovieResult clickedCardViewResult = bookmarks.get(pos);
    movieInfoIntent.putExtra("MOVIE_ID", clickedCardViewResult.getId());
    startActivity(movieInfoIntent);
  }
}

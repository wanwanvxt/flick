package vn.edu.eaut.flick.views;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.hls.HlsMediaSource;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import vn.edu.eaut.flick.R;
import vn.edu.eaut.flick.controllers.MovieController;
import vn.edu.eaut.flick.models.Episode;
import vn.edu.eaut.flick.models.Source;
import vn.edu.eaut.flick.models.WatchResult;
import vn.edu.eaut.flick.views.comps.EpisodeCardViewAdapter;

public class WatchActivity extends AppCompatActivity {
  private PlayerView playerView;
  private RecyclerView recyclerViewEpisodes;
  private CircularProgressIndicator progressLoading;
  //
  private ExoPlayer player;
  private EpisodeCardViewAdapter episodeCardViewAdapter;
  private MovieController movieController;
  private String movieId;
  private ArrayList<Episode> episodes;
  private Source currentSource;
  private boolean isFetchingSource = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_watch);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    initialize();
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (player != null) {
      player.pause();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (player != null) {
      player.play();
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (player != null) {
      player.release();
    }
  }

  private void initialize() {
    playerView = findViewById(R.id.playerView);
    recyclerViewEpisodes = findViewById(R.id.recyclerViewEpisodes);
    progressLoading = findViewById(R.id.progressLoading);
    //
    player = new ExoPlayer.Builder(this).build();
    player.addListener(new ExoPlayer.Listener() {
      @Override
      public void onPlayerError(@NonNull PlaybackException error) {
        Log.e("WatchActivity", "", error);
        Snackbar.make(findViewById(android.R.id.content), R.string.source_unavailable, Snackbar.LENGTH_SHORT).show();
      }
    });
    playerView.setPlayer(player);
    playerView.setLayoutParams(
      new FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        (int) getResources().getDimension(R.dimen.player_view_non_video_height)
      )
    );
    movieController = new MovieController(this);
    movieId = getIntent().getStringExtra("MOVIE_ID");
    episodes = getIntent().getParcelableArrayListExtra("EPISODES");
    //
    episodeCardViewAdapter = new EpisodeCardViewAdapter(episodes);
    episodeCardViewAdapter.setOnItemClickListener((v, pos) -> handleClickEpisodeCardView(pos));
    recyclerViewEpisodes.setAdapter(episodeCardViewAdapter);
    recyclerViewEpisodes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
  }

  private void fetchSourceByEpisode(String episodeId) {
    if (isFetchingSource) return;

    playerView.setEnabled(false);
    progressLoading.setVisibility(View.VISIBLE);

    movieController.fetchWatchSources(movieId, episodeId, new MovieController.CallbackResponse<WatchResult>() {
      @Override
      public void onSuccess(WatchResult result) {
        playerView.setEnabled(true);
        playerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        progressLoading.setVisibility(View.GONE);
        currentSource = result.getSources().get(0);
        isFetchingSource = false;
        loadSourceToPlayer(currentSource.getUrl());
      }

      @Override
      public void onError(String errorMsg) {
        Log.e("WatchActivity", errorMsg);
      }
    });
  }

  @OptIn(markerClass = UnstableApi.class)
  private void loadSourceToPlayer(String url) {
    DefaultHttpDataSource.Factory hlsDataSourceFactory = new DefaultHttpDataSource.Factory();
    Uri uri = Uri.parse(url);
    MediaItem mediaItem = new MediaItem.Builder().setUri(uri).build();
    HlsMediaSource mediaSource = new HlsMediaSource.Factory(hlsDataSourceFactory)
      .createMediaSource(mediaItem);
    player.setMediaSource(mediaSource);
    player.prepare();
    player.play();
  }

  private void handleClickEpisodeCardView(int pos) {
    Episode clickedEpisode = episodes.get(pos);
    fetchSourceByEpisode(clickedEpisode.getId());
  }
}

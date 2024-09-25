package vn.edu.eaut.flick.views;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import java.util.ArrayList;
import java.util.List;
import vn.edu.eaut.flick.R;
import vn.edu.eaut.flick.models.Episode;

public class WatchActivity extends AppCompatActivity {
  private PlayerView playerView;
  private ExoPlayer player;
  private String movieId;
  private ArrayList<Episode> episodes;

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

  private void initialize() {
    playerView = findViewById(R.id.playerView);
    player = new ExoPlayer.Builder(this).build();
    playerView.setPlayer(player);
    movieId = getIntent().getStringExtra("MOVIE_ID");
    episodes = getIntent().getParcelableArrayListExtra("EPISODES");
  }
}

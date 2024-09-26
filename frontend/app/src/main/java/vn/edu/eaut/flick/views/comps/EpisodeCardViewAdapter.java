package vn.edu.eaut.flick.views.comps;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import vn.edu.eaut.flick.R;
import vn.edu.eaut.flick.models.Episode;

public class EpisodeCardViewAdapter extends RecyclerView.Adapter<EpisodeCardViewAdapter.ViewHolder> {
  private final ArrayList<Episode> episodes;
  private OnItemClickListener listener;
  private int clickedPosition = -1;
  private int previousClickedPosition = -1;

  public EpisodeCardViewAdapter(ArrayList<Episode> episodes) {
    this.episodes = episodes;
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.item_episode_card_view, parent, false);
    return new ViewHolder(view, listener);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Episode episode = episodes.get(position);
    holder.buttonEpisode.setText(episode.getTitle());

    if (position == clickedPosition) {
      holder.buttonEpisode.setIcon(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_round_play_arrow_24));
      holder.buttonEpisode.setTypeface(holder.buttonEpisode.getTypeface(), Typeface.ITALIC);
    } else if (position == previousClickedPosition) {
      holder.buttonEpisode.setIcon(null);
      holder.buttonEpisode.setTypeface(holder.buttonEpisode.getTypeface(), Typeface.NORMAL);
    }

    holder.buttonEpisode.setOnClickListener(v -> {
      if (listener != null) {
        int currentPosition = holder.getAdapterPosition();
        if (currentPosition != RecyclerView.NO_POSITION) {
          previousClickedPosition = clickedPosition;
          clickedPosition = currentPosition;

          if (previousClickedPosition != -1) {
            notifyItemChanged(previousClickedPosition);
          }
          notifyItemChanged(clickedPosition);

          listener.onItemClick(v, currentPosition);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return episodes.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    MaterialButton buttonEpisode;

    public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
      super(itemView);
      buttonEpisode = itemView.findViewById(R.id.buttonEpisode);
    }
  }

  public interface OnItemClickListener {
    void onItemClick(View view, int position);
  }
}

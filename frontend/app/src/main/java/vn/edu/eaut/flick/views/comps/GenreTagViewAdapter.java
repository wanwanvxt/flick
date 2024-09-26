package vn.edu.eaut.flick.views.comps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import vn.edu.eaut.flick.R;

public class GenreTagViewAdapter extends RecyclerView.Adapter<GenreTagViewAdapter.ViewHolder> {
  private final ArrayList<String> genres;
  private OnItemClickListener listener;

  public GenreTagViewAdapter(ArrayList<String> genres) {
    this.genres = genres;
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.item_genre_tag_view, parent, false);
    return new ViewHolder(view, listener);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    String genre = genres.get(position);
    holder.labelTag.setText(genre);
  }

  @Override
  public int getItemCount() {
    return genres.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView labelTag;

    public ViewHolder(View itemView, OnItemClickListener listener) {
      super(itemView);
      labelTag = itemView.findViewById(R.id.labelTag);

      itemView.setOnClickListener(v -> {
        if (listener != null) {
          int position = getAdapterPosition();
          if (position != RecyclerView.NO_POSITION) {
            listener.onItemClick(v, position);
          }
        }
      });
    }
  }

  public interface OnItemClickListener {
    void onItemClick(View view, int position);
  }
}

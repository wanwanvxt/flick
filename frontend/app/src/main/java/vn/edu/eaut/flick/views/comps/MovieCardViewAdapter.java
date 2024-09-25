package vn.edu.eaut.flick.views.comps;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import vn.edu.eaut.flick.R;
import vn.edu.eaut.flick.models.MovieResult;

public class MovieCardViewAdapter extends RecyclerView.Adapter<MovieCardViewAdapter.ViewHolder> {
  private final ArrayList<MovieResult> movieResults;
  private OnItemClickListener listener;

  public MovieCardViewAdapter(ArrayList<MovieResult> movieResults) {
    this.movieResults = movieResults;
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.item_movie_card_view, parent, false);
    return new ViewHolder(view, listener);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    MovieResult movieResult = movieResults.get(position);
    holder.imageMovie.setContentDescription(movieResult.getTitle());
    Picasso.get()
      .load(movieResult.getImage())
      .placeholder(R.drawable.img_movie_placeholder)
      .error(R.drawable.img_movie_placeholder)
      .into(holder.imageMovie);
  }

  @Override
  public int getItemCount() {
    return movieResults.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    ImageView imageMovie;

    public ViewHolder(View itemView, OnItemClickListener listener) {
      super(itemView);
      imageMovie = itemView.findViewById(R.id.imageMovie);

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

  public static class HorizontalItemDecoration extends RecyclerView.ItemDecoration {
    private final int space;

    public HorizontalItemDecoration(int space) {
      this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
      outRect.left = space;
      outRect.right = space;
    }
  }

  public static class FlexboxItemDecoration extends RecyclerView.ItemDecoration {
    private final int space;

    public FlexboxItemDecoration(int space) {
      this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
      outRect.left = space;
      outRect.top = space;
      outRect.right = space;
      outRect.bottom = space;
    }
  }
}

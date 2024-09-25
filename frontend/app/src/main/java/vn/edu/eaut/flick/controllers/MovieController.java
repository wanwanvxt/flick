package vn.edu.eaut.flick.controllers;

import android.content.Context;
import androidx.annotation.NonNull;
import java.text.MessageFormat;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.eaut.flick.models.MovieInfo;
import vn.edu.eaut.flick.models.MovieResult;
import vn.edu.eaut.flick.models.SearchResult;
import vn.edu.eaut.flick.models.WatchResult;
import vn.edu.eaut.flick.services.MovieService;
import vn.edu.eaut.flick.services.RetrofitClient;

public class MovieController {
  private final MovieService movieService;

  private <T> void handleResponse(Call<T> call, CallbackResponse<T> callback) {
    call.enqueue(new Callback<T>() {
      @Override
      public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.isSuccessful() && response.body() != null) {
          callback.onSuccess(response.body());
        } else {
          callback.onError(MessageFormat.format("{0} - {1}", response.code(), response.message()));
        }
      }

      @Override
      public void onFailure(@NonNull Call<T> call, @NonNull Throwable throwable) {
        callback.onError(throwable.getMessage());
      }
    });
  }

  public interface CallbackResponse<T> {
    void onSuccess(T result);

    void onError(String errorMsg);
  }

  public MovieController(Context context) {
    movieService = RetrofitClient.getRetrofit(context).create(MovieService.class);
  }

  public void search(String query, int page, CallbackResponse<SearchResult> callback) {
    handleResponse(movieService.search(query, page), callback);
  }

  public void fetchMovieInfo(String id, CallbackResponse<MovieInfo> callback) {
    handleResponse(movieService.fetchMovieInfo(id), callback);
  }

  public void fetchWatchSources(String mediaID, String episodeID, CallbackResponse<WatchResult> callback) {
    handleResponse(movieService.fetchWatchSources(mediaID, episodeID), callback);
  }

  public void fetchRecentMovies(String type, CallbackResponse<ArrayList<MovieResult>> callback) {
    handleResponse(movieService.fetchRecentMovies(type), callback);
  }

  public void fetchTrendingMovies(String type, CallbackResponse<ArrayList<MovieResult>> callback) {
    handleResponse(movieService.fetchTrendingMovies(type), callback);
  }

  public void searchByGenre(String genre, int page, CallbackResponse<SearchResult> callback) {
    handleResponse(movieService.searchByGenre(genre, page), callback);
  }
}

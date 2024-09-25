package vn.edu.eaut.flick.services;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vn.edu.eaut.flick.models.MovieInfo;
import vn.edu.eaut.flick.models.MovieResult;
import vn.edu.eaut.flick.models.SearchResult;
import vn.edu.eaut.flick.models.WatchResult;

public interface MovieService {
  @GET("/")
  Call<SearchResult> search(
    @Query("query") String query,
    @Query("page") int page
  );

  @GET("/info")
  Call<MovieInfo> fetchMovieInfo(@Query("id") String id);

  @GET("/watch")
  Call<WatchResult> fetchWatchSources(
    @Query("mediaID") String mediaID,
    @Query("episodeID") String episodeID
  );

  @GET("/recent")
  Call<ArrayList<MovieResult>> fetchRecentMovies(@Query("type") String type);

  @GET("/trending")
  Call<ArrayList<MovieResult>> fetchTrendingMovies(@Query("type") String type);

  @GET("/genre")
  Call<SearchResult> searchByGenre(
    @Query("genre") String genre,
    @Query("page") int page
  );
}

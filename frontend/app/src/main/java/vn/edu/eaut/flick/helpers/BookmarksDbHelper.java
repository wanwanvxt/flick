package vn.edu.eaut.flick.helpers;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import vn.edu.eaut.flick.models.MovieResult;

public class BookmarksDbHelper extends SQLiteOpenHelper {
  private static final String DATABASE_NAME = "bookmarks.db";
  private static final int DATABASE_VERSION = 1;

  public BookmarksDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    String sql = "CREATE TABLE IF NOT EXISTS bookmarks (id TEXT PRIMARY KEY, title TEXT, image TEXT);";
    db.execSQL(sql);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    String sql = "DROP TABLE IF EXISTS bookmarks;";
    db.execSQL(sql);
    onCreate(db);
  }

  public boolean insertMovie(String id, String title, String image) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("id", id);
    values.put("title", title);
    values.put("image", image);
    long result = db.insert("bookmarks", null, values);
    db.close();
    return result != -1;
  }
  
  public boolean insertMovie(MovieResult movie) {
    return insertMovie(movie.getId(), movie.getTitle(), movie.getImage());
  }

  public boolean removeMovie(String id) {
    SQLiteDatabase db = getWritableDatabase();
    int result = db.delete("bookmarks", "id=?", new String[]{id});
    db.close();
    return result != 0;
  }

  public boolean existMovie(String id) {
    SQLiteDatabase db = getReadableDatabase();
    String sql = "SELECT 1 FROM bookmarks WHERE id = ? LIMIT 1;";
    Cursor cursor = db.rawQuery(sql, new String[]{id});
    boolean exist = cursor.getCount() > 0;
    cursor.close();
    return exist;
  }

  public ArrayList<MovieResult> getAllMovies() {
    SQLiteDatabase db = getReadableDatabase();
    ArrayList<MovieResult> result = new ArrayList<>();
    String sql = "SELECT * FROM bookmarks;";
    Cursor cursor = db.rawQuery(sql, null);
    if (cursor.moveToFirst()) {
      do {
        String id = cursor.getString(0);
        String title = cursor.getString(1);
        String image = cursor.getString(2);

        MovieResult movieResult = new MovieResult();
        movieResult.setId(id);
        movieResult.setTitle(title);
        movieResult.setImage(image);

        result.add(movieResult);
      } while (cursor.moveToNext());
    }
    cursor.close();
    db.close();
    return result;
  }

  public void getAllMoviesAsync(Activity activity, Callback callback) {
    new Thread(() -> {
      SQLiteDatabase db = null;
      Cursor cursor = null;

      ArrayList<MovieResult> result = new ArrayList<>();
      String sql = "SELECT * FROM bookmarks;";

      try {
        db = getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
          do {
            String id = cursor.getString(0);
            String title = cursor.getString(1);
            String image = cursor.getString(2);

            MovieResult movieResult = new MovieResult();
            movieResult.setId(id);
            movieResult.setTitle(title);
            movieResult.setImage(image);

            result.add(movieResult);
          } while (cursor.moveToNext());
        }
      } catch (Exception e) {
        Log.e("BookmarksDbHelper", e.getMessage(), e);
      } finally {
        if (cursor != null) {
          cursor.close();
        }
        if (db != null && db.isOpen()) {
          db.close();
        }
      }

      activity.runOnUiThread(() -> callback.onResult(result));
    }).start();
  }

  public interface Callback {
    void onResult(ArrayList<MovieResult> result);
  }
}

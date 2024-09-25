package vn.edu.eaut.flick.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class Episode implements Parcelable {
  private String id;
  private String title;
  private int number;
  private int season;
  private String url;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public int getSeason() {
    return season;
  }

  public void setSeason(int season) {
    this.season = season;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  //
  protected Episode(Parcel in) {
    id = in.readString();
    title = in.readString();
    number = in.readInt();
    season = in.readInt();
    url = in.readString();
  }

  public static final Creator<Episode> CREATOR = new Creator<Episode>() {
    @Override
    public Episode createFromParcel(Parcel in) {
      return new Episode(in);
    }

    @Override
    public Episode[] newArray(int size) {
      return new Episode[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(@NonNull Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(title);
    dest.writeInt(number);
    dest.writeInt(season);
    dest.writeString(url);
  }
  //
}

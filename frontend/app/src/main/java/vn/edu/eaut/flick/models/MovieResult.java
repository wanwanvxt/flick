package vn.edu.eaut.flick.models;

public class MovieResult {
  private String id;
  private String title;
  private String url;
  private String image;
  private String type;
  private String season;
  private String latestEpisode;

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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSeason() {
    return season;
  }

  public void setSeason(String season) {
    this.season = season;
  }

  public String getLatestEpisode() {
    return latestEpisode;
  }

  public void setLatestEpisode(String latestEpisode) {
    this.latestEpisode = latestEpisode;
  }
}

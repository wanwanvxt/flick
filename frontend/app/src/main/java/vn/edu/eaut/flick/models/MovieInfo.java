package vn.edu.eaut.flick.models;

import java.util.ArrayList;
import java.util.List;

public class MovieInfo {
  private String id;
  private String title;
  private String url;
  private String image;
  private String description;
  private String type;
  private String releaseDate;
  private ArrayList<String> genres;
  private ArrayList<String> casts;
  private ArrayList<String> tags;
  private String production;
  private String duration;
  private float rating;
  private ArrayList<Episode> episodes;

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  public  ArrayList<String> getGenres() {
    return genres;
  }

  public void setGenres(ArrayList<String> genres) {
    this.genres = genres;
  }

  public  ArrayList<String> getCasts() {
    return casts;
  }

  public void setCasts(ArrayList<String> casts) {
    this.casts = casts;
  }

  public  ArrayList<String> getTags() {
    return tags;
  }

  public void setTags(ArrayList<String> tags) {
    this.tags = tags;
  }

  public String getProduction() {
    return production;
  }

  public void setProduction(String production) {
    this.production = production;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public float getRating() {
    return rating;
  }

  public void setRating(float rating) {
    this.rating = rating;
  }

  public  ArrayList<Episode> getEpisodes() {
    return episodes;
  }

  public void setEpisodes(ArrayList<Episode> episodes) {
    this.episodes = episodes;
  }
}

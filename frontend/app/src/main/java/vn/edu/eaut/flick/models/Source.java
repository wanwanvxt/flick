package vn.edu.eaut.flick.models;

public class Source {
  private String url;
  private boolean isM3U8;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public boolean isM3U8() {
    return isM3U8;
  }

  public void setM3U8(boolean m3U8) {
    isM3U8 = m3U8;
  }
}

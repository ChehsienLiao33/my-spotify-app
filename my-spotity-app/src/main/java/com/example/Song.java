package com.example;

public class Song {

  private String name;
  private String artist;
  private String fileName;
  private String year;
  private String genre;
  private boolean isFavorite = false;

  // serializes attributes into a string
  public String toString() {
    String s;

    // since the object is complex, we return a JSON formatted string
    s = "{ ";
    s += "name: " + name;
    s += ", ";
    s += "artist: " + artist;
    s += ", ";
    s += "year: " + year;
    s += ", ";
    s += "genre: " + genre;
    s += ", ";
    s += "fileName: " + fileName;
    s += " }";

    return s;
  }

  // getters
  public String name() {
    return this.name;
  }

  public String artist() {
    return this.artist;
  }

  public String fileName() {
    return this.fileName;
  }

  public String year() {
    return this.year;
  }

  public String genre() {
    return this.genre;
  }

  public void setFavorite() {
    if (this.isFavorite) {
      this.isFavorite = false;
    } else {
      this.isFavorite = true;
    }

  }

  public boolean isFavorite() {
    return this.isFavorite;
  }
}

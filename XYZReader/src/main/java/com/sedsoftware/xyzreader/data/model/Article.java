package com.sedsoftware.xyzreader.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Article {
  public abstract int id();
  public abstract String title();
  public abstract String author();
  public abstract String body();
  public abstract String thumb();
  public abstract String photo();
  public abstract double aspect_ratio();
  public abstract String published_date();

  public static Builder builder() {
    return new AutoValue_Article.Builder();
  }

  public static JsonAdapter<Article> jsonAdapter(Moshi moshi) {
    return new AutoValue_Article.MoshiJsonAdapter(moshi);
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder id(int id);
    abstract Builder title(String title);
    abstract Builder author(String author);
    abstract Builder body(String body);
    abstract Builder thumb(String thumb);
    abstract Builder photo(String photo);
    abstract Builder aspect_ratio(double aspect_ratio);
    abstract Builder published_date(String published_date);

    public abstract Article build();
  }
}

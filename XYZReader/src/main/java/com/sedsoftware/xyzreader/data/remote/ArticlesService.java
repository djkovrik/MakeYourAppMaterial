package com.sedsoftware.xyzreader.data.remote;

import com.sedsoftware.xyzreader.data.model.Article;
import io.reactivex.Observable;
import java.util.List;
import retrofit2.http.GET;

public interface ArticlesService {

  String ENDPOINT = "https://go.udacity.com";

  @GET("/xyz-reader-json")
  Observable<List<Article>> loadArticlesFromServer();
}

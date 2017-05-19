package com.sedsoftware.xyzreader.data;

import com.sedsoftware.xyzreader.data.local.DatabaseHelper;
import com.sedsoftware.xyzreader.data.model.Article;
import com.sedsoftware.xyzreader.data.remote.ArticlesService;
import io.reactivex.Observable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {

  private final ArticlesService articlesService;
  private final DatabaseHelper databaseHelper;

  @Inject
  public DataManager(ArticlesService articlesService, DatabaseHelper databaseHelper) {
    this.articlesService = articlesService;
    this.databaseHelper = databaseHelper;
  }

  public Observable<Article> loadArticlesRemote() {
    return articlesService
        .loadArticles()
        .flatMap(Observable::fromIterable);
  }
}

package com.sedsoftware.xyzreader.data;

import com.sedsoftware.xyzreader.data.local.DatabaseHelper;
import com.sedsoftware.xyzreader.data.local.PreferencesHelper;
import com.sedsoftware.xyzreader.data.model.Article;
import com.sedsoftware.xyzreader.data.remote.ArticlesService;
import io.reactivex.Observable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {

  private final ArticlesService articlesService;
  private final DatabaseHelper databaseHelper;
  private final PreferencesHelper preferencesHelper;

  @Inject
  public DataManager(ArticlesService articlesService, DatabaseHelper databaseHelper,
      PreferencesHelper preferencesHelper) {

    this.articlesService = articlesService;
    this.databaseHelper = databaseHelper;
    this.preferencesHelper = preferencesHelper;
  }

  public Observable<Article> loadArticlesRemote() {
    return articlesService
        .loadArticles()
        .flatMap(Observable::fromIterable);
  }
}

package com.sedsoftware.xyzreader.data;

import com.sedsoftware.xyzreader.data.local.DatabaseHelper;
import com.sedsoftware.xyzreader.data.model.Article;
import com.sedsoftware.xyzreader.data.remote.ArticlesService;
import com.sedsoftware.xyzreader.utils.RxUtils;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
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

  public Completable syncArticles() {
    return articlesService
        .loadArticlesFromServer()
        .compose(RxUtils.applySchedulers())
        .flatMapCompletable(databaseHelper::saveArticlesToDatabase);
  }

  public Observable<Article> getArticlesObservableStream() {
    return databaseHelper
        .getArticlesFromDatabase()
        .flatMap(Observable::fromIterable)
        .distinct();
  }

  public Single<Article> getArticleSingle(int id) {
    return databaseHelper
        .getSingleArticleFromDatabase(id)
        .firstOrError();
  }
}

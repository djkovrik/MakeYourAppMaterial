package com.sedsoftware.xyzreader.data;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.sedsoftware.xyzreader.data.local.DatabaseHelper;
import com.sedsoftware.xyzreader.data.model.Article;
import com.sedsoftware.xyzreader.data.remote.ArticlesService;
import com.sedsoftware.xyzreader.utils.RxUtils;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {

  private final ArticlesService articlesService;
  private final DatabaseHelper databaseHelper;
  private final BehaviorRelay<Integer> requestState;

  @Inject
  public DataManager(ArticlesService articlesService, DatabaseHelper databaseHelper,
      BehaviorRelay<Integer> requestState) {

    this.articlesService = articlesService;
    this.databaseHelper = databaseHelper;
    this.requestState = requestState;
  }

  private void publishRequestState(@RequestState.State int state) {
    Observable.just(state)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(requestState);
  }

  public Completable syncArticles() {
    return articlesService
        .loadArticlesFromServer()
        .compose(RxUtils.applySchedulers())
        .doOnSubscribe(disposable -> publishRequestState(RequestState.LOADING))
        .doOnError(throwable -> publishRequestState(RequestState.ERROR))
        .doOnComplete(() -> publishRequestState(RequestState.COMPLETED))
        .flatMapCompletable(databaseHelper::saveArticlesToDatabase);
  }

  public Observable<Article> getArticlesObservableStream() {
    return databaseHelper
        .getArticlesFromDatabase()
        .compose(RxUtils.applySchedulers())
        .flatMap(Observable::fromIterable)
        .distinct();
  }

  public Single<Article> getArticleSingle(int id) {
    return databaseHelper
        .getSingleArticleFromDatabase(id)
        .firstOrError();
  }

  public BehaviorRelay<Integer> getRequestState() {
    return requestState;
  }
}

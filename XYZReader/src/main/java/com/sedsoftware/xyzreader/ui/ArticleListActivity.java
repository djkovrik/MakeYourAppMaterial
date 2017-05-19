package com.sedsoftware.xyzreader.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.sedsoftware.xyzreader.data.DataManager;
import com.sedsoftware.xyzreader.data.model.Article;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import javax.inject.Inject;
import timber.log.Timber;

public class ArticleListActivity extends BaseActivity {

  @Inject
  DataManager dataManager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getActivityComponent().inject(this);

    Completable sync = dataManager.syncArticles()
        .doOnError(throwable -> Timber.d("Sync - error!"))
        .doOnComplete(() -> Timber.d("Sync completed!"))
        .doOnSubscribe(disposable -> Timber.d("Sync started!"));

    Observable<Article> articlesStream = dataManager.getArticlesObservableStream()
        .doOnSubscribe(disposable -> Timber.d("Articles from db - subscribed."))
        .doOnComplete(() -> Timber.d("Articles from db - loading completed!"))
        .doOnError(throwable -> Timber.d("Articles from db - error!"));

    Single<Article> articleSingle = dataManager.getArticleSingle(3)
        .doOnSubscribe(disposable -> Timber.d("Single article from db - subscribed."))
        .doOnSuccess(article -> Timber.d("Single article - loading completed!"))
        .doOnError(throwable -> Timber.d("Single article - error: " + throwable.getMessage()));

    sync.subscribe();

    articlesStream
        .subscribe(article -> Timber.d("Articles from db: " + article.title()));

    articleSingle
        .subscribe(article -> Timber.d("Single article: " + article.title()));
  }
}

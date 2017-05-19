package com.sedsoftware.xyzreader.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.sedsoftware.xyzreader.data.DataManager;
import com.sedsoftware.xyzreader.utils.RxUtils;
import javax.inject.Inject;
import timber.log.Timber;

public class ArticleListActivity extends BaseActivity {

  @Inject
  DataManager dataManager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getActivityComponent().inject(this);

    dataManager.loadArticlesRemote()
        .compose(RxUtils.applySchedulers())
        .doOnSubscribe(disposable -> Timber.d("Start loading..."))
        .doOnComplete(() -> Timber.d("Completed!"))
        .doOnError(throwable -> Timber.d("Error!"))
        .subscribe(article -> Timber.d("Article: " + article.title()));
  }
}

package com.sedsoftware.xyzreader.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.sedsoftware.xyzreader.R;
import com.sedsoftware.xyzreader.data.DataManager;
import com.sedsoftware.xyzreader.data.RequestState;
import com.sedsoftware.xyzreader.ui.ArticlesAdapter.OnArticleClickListener;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;
import timber.log.Timber;

@SuppressWarnings("ConstantConditions")
public class ArticleListActivity extends BaseActivity implements
    OnArticleClickListener, OnRefreshListener {

  @Inject
  DataManager dataManager;

  private Completable articlesSync;
  private Disposable articlesSubscribtion;

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.app_bar)
  AppBarLayout appbar;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;

  private ArticlesAdapter adapter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getActivityComponent().inject(this);

    setContentView(R.layout.activity_article_list);
    ButterKnife.bind(this);

    articlesSync = dataManager
        .syncArticles()
        .doOnSubscribe(disposable -> Timber.d("Sync started..."))
        .doOnComplete(() -> Timber.d("Sync completed"));

    if (savedInstanceState == null && articlesSubscribtion == null) {
      articlesSubscribtion = articlesSync.subscribe();
    }

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    handleLoadingIndicator(swipeRefreshLayout);

    int columnsCount = getResources().getInteger(R.integer.list_column_count);

    StaggeredGridLayoutManager sglm =
        new StaggeredGridLayoutManager(columnsCount, StaggeredGridLayoutManager.VERTICAL);

    adapter = new ArticlesAdapter(this);
    adapter.setHasStableIds(true);

    recyclerView.setLayoutManager(sglm);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);

    swipeRefreshLayout.setOnRefreshListener(this);

    dataManager.getArticlesObservableStream()
        .doOnSubscribe(disposable -> adapter.clearList())
        .doOnNext(article -> Timber.d("Fetch article from db: " + article.title()))
        .subscribe(article -> adapter.addArticle(article));
  }

  @Override
  public void articleClicked(int id) {
    Toast.makeText(this, "Article: " + String.valueOf(id), Toast.LENGTH_SHORT).show();
  }

  private void handleLoadingIndicator(SwipeRefreshLayout layout) {
    dataManager.getRequestState().subscribe(state -> {
      switch (state) {
        case RequestState.IDLE:
          break;
        case RequestState.LOADING:
          layout.setRefreshing(true);
          break;
        case RequestState.COMPLETED:
          layout.setRefreshing(false);
          break;
        case RequestState.ERROR:
          break;
      }
    });
  }

  @Override
  public void onRefresh() {
      if (articlesSubscribtion != null && !articlesSubscribtion.isDisposed()) {
        articlesSubscribtion.dispose();
      }
    articlesSubscribtion = articlesSync.subscribe();
  }
}

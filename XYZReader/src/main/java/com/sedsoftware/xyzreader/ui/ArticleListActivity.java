package com.sedsoftware.xyzreader.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.sedsoftware.xyzreader.R;
import com.sedsoftware.xyzreader.data.DataManager;
import com.sedsoftware.xyzreader.data.RequestState;
import com.sedsoftware.xyzreader.ui.ArticlesAdapter.OnArticleClickListener;
import com.sedsoftware.xyzreader.utils.NetworkUtils;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;
import timber.log.Timber;

@SuppressWarnings("ConstantConditions")
public class ArticleListActivity extends BaseActivity implements
    OnArticleClickListener, OnRefreshListener {

  private Disposable syncDisposable;
  private Disposable streamDisposable;

  @Inject
  DataManager dataManager;

  @BindInt(R.integer.list_column_count)
  int columnsCount;

  @BindString(R.string.error_msg)
  String errorMessage;

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

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    StaggeredGridLayoutManager sglm =
        new StaggeredGridLayoutManager(columnsCount, StaggeredGridLayoutManager.VERTICAL);

    adapter = new ArticlesAdapter(this);
    adapter.setHasStableIds(true);

    recyclerView.setLayoutManager(sglm);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);

    swipeRefreshLayout.setOnRefreshListener(this);

    subscribeToDbStream();
    handleLoadingIndicator();

    if (savedInstanceState == null) {
      syncData();
    }
  }

  @Override
  public void onRefresh() {
    syncData();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (syncDisposable != null) {
      syncDisposable.dispose();
    }
    if (streamDisposable != null) {
      streamDisposable.dispose();
    }
  }

  @Override
  public void articleClicked(int id) {
    Intent intent = ArticleDetailActivity.prepareIntent(this, id);

    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      startActivity(intent,
          ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    } else {
      startActivity(intent);
    }
  }

  private void subscribeToDbStream() {
    if (streamDisposable == null) {
      streamDisposable = dataManager.getArticlesObservableStream()
          .doOnSubscribe(disposable -> adapter.clearList())
          .doOnNext(article -> Timber.d("Fetch article from db: " + article.title()))
          .subscribe(article -> adapter.addArticle(article));
    }
  }

  private void handleLoadingIndicator() {
    dataManager.getRequestState().subscribe(state -> {
      switch (state) {
        case RequestState.IDLE:
          break;
        case RequestState.LOADING:
          swipeRefreshLayout.setRefreshing(true);
          break;
        case RequestState.COMPLETED:
          swipeRefreshLayout.setRefreshing(false);
          break;
        case RequestState.ERROR:
          swipeRefreshLayout.setRefreshing(false);
          break;
      }
    });
  }

  private void syncData() {
    // Very simple network check, mb not the most elegant solution
    if (!NetworkUtils.isNetworkConnected(this)) {
      Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
      swipeRefreshLayout.setRefreshing(false);
      return;
    }

    if (syncDisposable != null && !syncDisposable.isDisposed()) {
      syncDisposable.dispose();
    }

    syncDisposable = dataManager
        .syncArticles()
        .doOnSubscribe(disposable -> Timber.d("Sync started..."))
        .subscribe(() -> Timber.d("Sync finished..."),
            t -> Timber.d("Sync failed! Error: " + t.getMessage()));
  }
}

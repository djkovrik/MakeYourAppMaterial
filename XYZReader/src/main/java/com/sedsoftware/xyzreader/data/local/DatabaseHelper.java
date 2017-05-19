package com.sedsoftware.xyzreader.data.local;

import android.database.sqlite.SQLiteDatabase;
import com.sedsoftware.xyzreader.data.local.ArticlesDatabase.Table;
import com.sedsoftware.xyzreader.data.model.Article;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Completable;
import io.reactivex.Observable;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.schedulers.Schedulers;

@Singleton
public class DatabaseHelper {

  private final BriteDatabase briteDatabase;

  @Inject
  public DatabaseHelper(DbOpenHelper dbOpenHelper) {
    SqlBrite sqlBrite = new SqlBrite.Builder().build();
    briteDatabase = sqlBrite.wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
  }

  public Completable saveArticlesToDatabase(Collection<Article> articles) {

    return Completable.create(e -> {

      BriteDatabase.Transaction transaction = briteDatabase.newTransaction();

      try {
        briteDatabase.delete(Table.TABLE_NAME, null);

        for (Article article : articles) {
          long result = briteDatabase.insert(
              Table.TABLE_NAME,
              ArticlesDatabase.toContentValues(article),
              SQLiteDatabase.CONFLICT_REPLACE
          );

          if (result < 0) {
            e.onError(new Throwable("Can't insert values into db."));
          }
        }

        transaction.markSuccessful();
        e.onComplete();
      } finally {
        transaction.end();
      }
    });
  }

  public Observable<List<Article>> getArticlesFromDatabase() {

    // rxJava1 Observable
    rx.Observable<List<Article>> listObservable =
        briteDatabase
            .createQuery(Table.TABLE_NAME, "SELECT * FROM " + Table.TABLE_NAME)
            .mapToList(ArticlesDatabase::parseCursor);

    // Convert to rxJava2
    return RxJavaInterop.toV2Observable(listObservable);
  }

  public Observable<Article> getSingleArticleFromDatabase(int id) {

    rx.Observable<Article> articleObservable = briteDatabase
        .createQuery(Table.TABLE_NAME,
            "SELECT * FROM " + Table.TABLE_NAME + " WHERE server_id = ?",
            String.valueOf(id))
        .mapToOne(ArticlesDatabase::parseCursor);

    return RxJavaInterop.toV2Observable(articleObservable);
  }
}

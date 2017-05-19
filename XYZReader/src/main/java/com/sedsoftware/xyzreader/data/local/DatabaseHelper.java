package com.sedsoftware.xyzreader.data.local;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
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

  public BriteDatabase getBriteDatabase() {
    return briteDatabase;
  }
}

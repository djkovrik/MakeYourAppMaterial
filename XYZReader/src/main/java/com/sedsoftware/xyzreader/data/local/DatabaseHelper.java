package com.sedsoftware.xyzreader.data.local;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import rx.schedulers.Schedulers;

public class DatabaseHelper {

  private final BriteDatabase briteDb;

  public DatabaseHelper(DbOpenHelper dbOpenHelper) {
    SqlBrite sqlBrite = new SqlBrite.Builder().build();
    briteDb = sqlBrite.wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
  }

  public BriteDatabase getBriteDb() {
    return briteDb;
  }
}

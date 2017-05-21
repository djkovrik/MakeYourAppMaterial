package com.sedsoftware.xyzreader.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.sedsoftware.xyzreader.data.local.ArticlesDatabase.Table;
import com.sedsoftware.xyzreader.injection.ApplicationContext;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DbOpenHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "xyzreader.db";
  private static final int DATABASE_VERSION = 1;

  @Inject
  DbOpenHelper(@ApplicationContext Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {

    db.beginTransaction();

    try {
      db.execSQL(Table.CREATE);
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + Table.TABLE_NAME);
    onCreate(db);
  }
}

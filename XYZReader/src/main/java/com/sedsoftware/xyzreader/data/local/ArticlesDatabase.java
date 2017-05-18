package com.sedsoftware.xyzreader.data.local;

public class ArticlesDatabase {

  public abstract static class Table {

    public static final String TABLE_NAME = "articles";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SERVER_ID = "server_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_THUMB_URL = "thumb_url";
    public static final String COLUMN_PHOTO_URL = "photo_url";
    public static final String COLUMN_ASPECT_RATIO = "aspect_ratio";
    public static final String COLUMN_PUBLISHED_DATE = "published_date";

    public static final String CREATE =
        "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SERVER_ID + " TEXT,"
            + COLUMN_TITLE + " TEXT NOT NULL,"
            + COLUMN_AUTHOR + " TEXT NOT NULL,"
            + COLUMN_BODY + " TEXT NOT NULL,"
            + COLUMN_THUMB_URL + " TEXT NOT NULL,"
            + COLUMN_PHOTO_URL + " TEXT NOT NULL,"
            + COLUMN_ASPECT_RATIO + " REAL NOT NULL DEFAULT 1.5,"
            + COLUMN_PUBLISHED_DATE + " TEXT NOT NULL"
            + ")";
  }
}

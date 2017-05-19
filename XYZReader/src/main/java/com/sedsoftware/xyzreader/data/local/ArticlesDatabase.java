package com.sedsoftware.xyzreader.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import com.sedsoftware.xyzreader.data.model.Article;

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
            + COLUMN_SERVER_ID + " INTEGER,"
            + COLUMN_TITLE + " TEXT NOT NULL,"
            + COLUMN_AUTHOR + " TEXT NOT NULL,"
            + COLUMN_BODY + " TEXT NOT NULL,"
            + COLUMN_THUMB_URL + " TEXT NOT NULL,"
            + COLUMN_PHOTO_URL + " TEXT NOT NULL,"
            + COLUMN_ASPECT_RATIO + " REAL NOT NULL DEFAULT 1.5,"
            + COLUMN_PUBLISHED_DATE + " TEXT NOT NULL"
            + ")";
  }

  static ContentValues toContentValues(Article article) {

    ContentValues cv = new ContentValues();

    cv.put(Table.COLUMN_SERVER_ID, article.id());
    cv.put(Table.COLUMN_TITLE, article.title());
    cv.put(Table.COLUMN_AUTHOR, article.author());
    cv.put(Table.COLUMN_BODY, article.body());
    cv.put(Table.COLUMN_THUMB_URL, article.thumb());
    cv.put(Table.COLUMN_PHOTO_URL, article.photo());
    cv.put(Table.COLUMN_ASPECT_RATIO, article.aspect_ratio());
    cv.put(Table.COLUMN_PUBLISHED_DATE, article.published_date());

    return cv;
  }

  static Article parseCursor(Cursor cursor) {

    int articleId = cursor.getInt(cursor.getColumnIndexOrThrow(Table.COLUMN_SERVER_ID));
    String articleTitle = cursor.getString(cursor.getColumnIndexOrThrow(Table.COLUMN_TITLE));
    String articleAuthor = cursor.getString(cursor.getColumnIndexOrThrow(Table.COLUMN_AUTHOR));
    String articleBody = cursor.getString(cursor.getColumnIndexOrThrow(Table.COLUMN_BODY));
    String articleThumb = cursor.getString(cursor.getColumnIndexOrThrow(Table.COLUMN_THUMB_URL));
    String articlePhoto = cursor.getString(cursor.getColumnIndexOrThrow(Table.COLUMN_PHOTO_URL));
    double articleRatio = cursor.getDouble(cursor.getColumnIndexOrThrow(Table.COLUMN_ASPECT_RATIO));
    String articleDate = cursor.getString(cursor.getColumnIndexOrThrow(Table.COLUMN_PUBLISHED_DATE));

    return Article.builder()
        .id(articleId)
        .title(articleTitle)
        .author(articleAuthor)
        .body(articleBody)
        .thumb(articleThumb)
        .photo(articlePhoto)
        .aspect_ratio(articleRatio)
        .published_date(articleDate)
        .build();
  }
}

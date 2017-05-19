package com.sedsoftware.xyzreader.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import com.sedsoftware.xyzreader.injection.ApplicationContext;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {

  public static final String FILE_NAME = "xyz_preferences";

  private final SharedPreferences preferences;

  @Inject
  public PreferencesHelper(@ApplicationContext Context context) {
    preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
  }
}

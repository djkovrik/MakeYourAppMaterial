package com.sedsoftware.xyzreader.injection.modules;

import android.app.Activity;
import android.content.Context;
import com.sedsoftware.xyzreader.injection.ActivityContext;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

  private Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides
  @ActivityContext
  Context provideContext() {
    return activity;
  }

  @Provides
  Activity provideActivity() {
    return activity;
  }
}

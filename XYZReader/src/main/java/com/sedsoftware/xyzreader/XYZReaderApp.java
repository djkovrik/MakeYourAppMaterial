package com.sedsoftware.xyzreader;

import android.app.Application;
import android.content.Context;
import com.facebook.stetho.Stetho;
import com.sedsoftware.xyzreader.injection.components.ApplicationComponent;
import com.sedsoftware.xyzreader.injection.components.DaggerApplicationComponent;
import com.sedsoftware.xyzreader.injection.modules.ApplicationModule;
import timber.log.Timber;

public class XYZReaderApp extends Application {

  private ApplicationComponent applicationComponent;

  @Override
  public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      Timber.uprootAll();
      Timber.plant(new Timber.DebugTree());

      Stetho.initializeWithDefaults(this);
    }

    applicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(this))
        .build();

    applicationComponent.inject(this);
  }

  public static XYZReaderApp get(Context context) {
    return (XYZReaderApp) context.getApplicationContext();
  }

  public ApplicationComponent getComponent() {
    return applicationComponent;
  }
}

package com.sedsoftware.xyzreader.injection.components;

import android.app.Application;
import android.content.Context;
import com.sedsoftware.xyzreader.XYZReaderApp;
import com.sedsoftware.xyzreader.data.DataManager;
import com.sedsoftware.xyzreader.data.local.DatabaseHelper;
import com.sedsoftware.xyzreader.data.local.PreferencesHelper;
import com.sedsoftware.xyzreader.injection.ApplicationContext;
import com.sedsoftware.xyzreader.injection.modules.ApplicationModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

  void inject(XYZReaderApp xyzReaderApp);

  @ApplicationContext
  Context getContext();

  Application getApplication();

  DataManager getDataManager();

  DatabaseHelper getDatabaseHelper();

  PreferencesHelper getPreferencesHelper();
}

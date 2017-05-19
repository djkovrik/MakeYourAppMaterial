package com.sedsoftware.xyzreader.injection.modules;

import android.app.Application;
import android.content.Context;
import com.sedsoftware.xyzreader.data.remote.ArticlesService;
import com.sedsoftware.xyzreader.data.remote.ServiceFactory;
import com.sedsoftware.xyzreader.injection.ApplicationContext;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class ApplicationModule {

  private final Application application;

  public ApplicationModule(Application application) {
    this.application = application;
  }

  @Provides
  @ApplicationContext
  Context provideContext() {
    return application;
  }

  @Provides
  Application provideApplication() {
    return application;
  }

  @Provides
  @Singleton
  ArticlesService provideArticlesService() {
    return ServiceFactory.createFrom(ArticlesService.class, ArticlesService.ENDPOINT);
  }
}

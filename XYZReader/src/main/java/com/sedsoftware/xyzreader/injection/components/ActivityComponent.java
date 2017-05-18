package com.sedsoftware.xyzreader.injection.components;

import com.sedsoftware.xyzreader.injection.PerActivity;
import com.sedsoftware.xyzreader.injection.modules.ActivityModule;
import com.sedsoftware.xyzreader.ui.ArticleListActivity;
import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

  void inject(ArticleListActivity articleListActivity);
}
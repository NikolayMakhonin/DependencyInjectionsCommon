package com.github.nikolaymakhonin.dependency_injections_common.modules.app;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import javax.inject.Named;

import com.github.nikolaymakhonin.dependency_injections_common.scopes.PerApplication;

import dagger.Module;
import dagger.Provides;

@Module(includes = { SplashScreenModule.class })
public class AppModuleBase {

    private Class<? extends Activity> _startActivityClass;

    public AppModuleBase(@NonNull Class<? extends Activity> startActivityClass) {
        _startActivityClass = startActivityClass;
    }

    @Provides
    @PerApplication
    @Named("StartActivityClass")
    public Class<? extends Activity> getStartActivityClass() {
        return _startActivityClass;
    }

    @Provides
    @PerApplication
    public LayoutInflater getLayoutInflater(Context context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}

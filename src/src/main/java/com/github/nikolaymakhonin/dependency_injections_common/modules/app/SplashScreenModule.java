package com.github.nikolaymakhonin.dependency_injections_common.modules.app;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import mika.controls.views.SplashScreenView;

import com.github.nikolaymakhonin.dependency_injections_common.classes.SplashWindowManager;
import com.github.nikolaymakhonin.dependency_injections_common.scopes.PerApplication;

@Module
public class SplashScreenModule {

    @Provides
    @PerApplication
    public SplashWindowManager getSplashWindowManager(Context context, LayoutInflater layoutInflater) {
        return new SplashWindowManager(context, layoutInflater);
    }


}

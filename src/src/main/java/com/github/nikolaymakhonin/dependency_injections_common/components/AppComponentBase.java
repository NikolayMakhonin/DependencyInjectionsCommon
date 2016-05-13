package com.github.nikolaymakhonin.dependency_injections_common.components;

import android.app.Activity;
import android.app.Dialog;

import com.github.nikolaymakhonin.dependency_injections_common.classes.SplashWindowManager;
import com.github.nikolaymakhonin.dependency_injections_common.modules.app.AppModuleBase;
import com.github.nikolaymakhonin.dependency_injections_common.scopes.PerApplication;

import javax.inject.Named;

import dagger.Component;

@Component(dependencies = {ServiceComponentBase.class}, modules = { AppModuleBase.class })
@PerApplication
public interface AppComponentBase extends ServiceComponentBase {
    SplashWindowManager getSplashWindowManager();
    /** Запускается из SplashScreenActivity */
    @Named("StartActivityClass")
    Class<? extends Activity> getStartActivityClass();
}


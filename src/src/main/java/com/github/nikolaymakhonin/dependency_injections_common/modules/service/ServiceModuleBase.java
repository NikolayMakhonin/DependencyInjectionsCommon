package com.github.nikolaymakhonin.dependency_injections_common.modules.service;

import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;

import com.github.nikolaymakhonin.dependency_injections_common.scopes.PerService;

import dagger.Module;
import dagger.Provides;
import mika.utils.ControlUtils;

@Module(includes = { FabricModule.class })
public class ServiceModuleBase {

    private Context _appContext;

    public ServiceModuleBase(@NonNull Context appContext) {
        _appContext = ControlUtils.GetApplicationContext(appContext);
    }

    @Provides
    @PerService
    public Context getAppContext() {
        return _appContext;
    }

    @Provides
    @PerService
    public NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}

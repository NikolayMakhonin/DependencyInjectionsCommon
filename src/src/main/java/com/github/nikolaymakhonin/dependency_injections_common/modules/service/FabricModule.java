package com.github.nikolaymakhonin.dependency_injections_common.modules.service;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.core.CrashlyticsCore;
import com.github.nikolaymakhonin.dependency_injections_common.scopes.PerService;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.fabric.sdk.android.Fabric;
import mika.logger.Log;
import mika.logger.LogEventArgs;
import mika.logger.LogEventInfo;
import mika.logger.LogPriority;
import mika.utils.DeviceUtils;
import mika.utils.Events.IEventListener;

@Module
public class FabricModule {

    @Provides
    @PerService
    @Named("isEnabled")
    public static boolean isEnabled() {
        return !DeviceUtils.isEmulator();
    }

    @Provides
    @PerService
    public static Fabric getInstance(
        Context context,
        @Named("isEnabled") boolean enabled,
        @Named("FabricLogEventListener") IEventListener<LogEventArgs> logEventListener)
    {
        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(!enabled).build();
        Fabric instance = Fabric.with(context, new Crashlytics.Builder().core(core).build());

        if (enabled) {
            Log.LogEvent().add(logEventListener);
        }
        return instance;
    }

    @Provides
    @PerService
    @Named("FabricLogEventListener")
    public static IEventListener<LogEventArgs> getFabricLogEventListener() {
        IEventListener<LogEventArgs>[] logEventListener = new IEventListener[1];
        logEventListener[0] = new IEventListener<LogEventArgs>() {
            @Override
            public boolean onEvent(Object o, LogEventArgs e) {
                Log.LogEvent().remove(logEventListener[0]);
                try {
                    LogEventInfo logInfo = e.logInfo;

                    if (logInfo.exception instanceof ConnectException
                        || logInfo.exception instanceof UnknownHostException)
                    {
                        return true; //отсутствие доступа к интернет - не ошибка
                    }

                    if (logInfo.exception != null) {
                        Crashlytics.setString("tag", logInfo.tag);
                        //Crashlytics.setString("ApplicationMode", _isBackgroundProcess ? "Background" : "Foreground");
                        Crashlytics.setString("message", logInfo.message);
                        Crashlytics.setInt("priority", logInfo.priority);
                        //Crashlytics.setDouble("Application duration (sec)", getApplicationRunnedDuration().TotalSeconds());
                        Crashlytics.logException(logInfo.exception);
                        if (logInfo.terminateApplication) {
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //if (!_isBackgroundProcess && !_disposed) {
                                        //    _activity.moveTaskToBack(true);
                                        //}
                                        Crashlytics.getInstance().core.getFabric().getExecutorService()
                                            .awaitTermination(60, TimeUnit.SECONDS);
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    } finally {
                                        System.exit(1);
                                    }
                                }
                            });
                            thread.start();
                        }
                    } else {
                        //Crashlytics.setDouble("Application duration (sec)", getApplicationRunnedDuration().TotalSeconds());
                        Crashlytics.log(logInfo.priority, logInfo.tag, logInfo.message);
                    }

                    //                _googleAnalyticsTracker.send(new HitBuilders.EventBuilder().setCategory("Logger").setAction("LogEvent")
                    //                        .setLabel(LogPriority.toString(logInfo.priority)).build());

                    Answers.getInstance().logCustom(
                        new CustomEvent("LogEvent").putCustomAttribute("priority", LogPriority.toString(logInfo.priority)));

                } catch (Exception exception) {
                    Log.e("FragmenterController", null, exception);
                } finally {
                    Log.LogEvent().add(logEventListener[0]);
                }
                return true;
            }
        };
        return logEventListener[0];
    }
}

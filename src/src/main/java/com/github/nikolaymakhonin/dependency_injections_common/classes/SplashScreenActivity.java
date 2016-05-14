package com.github.nikolaymakhonin.dependency_injections_common.classes;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.crashlytics.android.Crashlytics;
import com.github.nikolaymakhonin.dependency_injections_common.components.AppComponentBase;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import mika.controls.views.SplashScreenView;
import mika.logger.Log;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class SplashScreenActivity extends RxAppCompatActivity {

    private Subject<Boolean, Boolean> _startMainActivitySubject = PublishSubject.create();

    private static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    private AppComponentBase _appComponent;

    private SplashScreenView _splashScreenView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("SplashScreenActivity", "onCreate");

        if (false) {
            // Fabric plugin to Android Studio requires this line in launching activity.
            // But Fabric initialized in other place - in Dagger module.
            Fabric.with(this, new Crashlytics());
        }

        _appComponent = ((IHasAppComponentBase)getApplication()).getAppComponent();

        // Android 6.0 requires additional permissions to display overlay windows, that need to prompt the user for
        boolean mustAskPermissions = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this);
        if (mustAskPermissions) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        }

        //Theme for activity must be clear all predefined backgrounds in order to avoid unnecessary expenditure of RAM.
        setTheme(SplashScreenView.getSplashScreenActivityTheme());

        setContentView(SplashScreenView.getSplashScreenLayoutId());
        _splashScreenView = (SplashScreenView)findViewById(SplashScreenView.getSplashScreenViewId());

        if (!mustAskPermissions) {
            _appComponent.getSplashWindowManager().show();
            _splashScreenView.DrawObservable()
                .first()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(o -> {
                    _startMainActivitySubject.onNext(true);
                });
        }

        // Run main Activity after first draw SplashScreenActivity. If you not wait the first draw,
        // then will be empty screen during all main activity loading, and then SplashScreen will blink,
        // if it have time to.
        _startMainActivitySubject
            .first()
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .delay(100, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(o -> {
                Intent mainIntent = new Intent(SplashScreenActivity.this.getApplicationContext(),
                    _appComponent.getStartActivityClass());
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                finish();
            }, e -> {
                Log.e("SplashScreenActivity", null, (Throwable) e);
                finish();
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Show splash screen window only if the appropriate permissions are obtained.
        boolean mustShowSplashWindow =
            requestCode == OVERLAY_PERMISSION_REQ_CODE
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && Settings.canDrawOverlays(this);

        if (mustShowSplashWindow) {
            _appComponent.getSplashWindowManager().show();
        }
        _splashScreenView.DrawObservable()
            .first()
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe(o -> {
                _startMainActivitySubject.onNext(true);
            });
    }
}

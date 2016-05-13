package com.github.nikolaymakhonin.dependency_injections_common.classes;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import mika.controls.views.SplashScreenView;
import mika.logger.Log;
import mika.utils.ControlUtils;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.github.nikolaymakhonin.dependency_injections_common.R;
import com.github.nikolaymakhonin.dependency_injections_common.components.AppComponentBase;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

public class SplashScreenActivity extends RxAppCompatActivity {

    private static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    private AppComponentBase _appComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("SplashScreenActivity", "onCreate");

        if (false) {
            //Плагин к Android Studio требует наличия этой строчки в первом запускаемом Activity
            Fabric.with(this, new Crashlytics());
        }

        _appComponent = ((IHasAppComponentBase)getApplication()).getAppComponent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            _appComponent.getSplashWindowManager().show();
        }

        setTheme(SplashScreenView.getSplashScreenActivityTheme());
        setContentView(SplashScreenView.getSplashScreenLayoutId());
        SplashScreenView splashScreenView = (SplashScreenView)findViewById(SplashScreenView.getSplashScreenViewId());
//        SplashScreenView splashScreenView = new SplashScreenView(this);
//        setContentView(splashScreenView);

        // После первой отрисовки SplashScreen запускаем главное Activity. Если не дожидаться отрисовки,
        // то получим пустой экран на всем протяжении загрузки приложения, а потом мигнет (если успеет) SplashScreen
        splashScreenView.DrawObservable()
            .first()
            .compose(bindToLifecycle())
            .delay(100, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(o -> {}, e -> {
                Log.e("SplashScreenActivity", null, (Throwable) e);
                finish();
            }, () -> {
                Intent mainIntent = new Intent(SplashScreenActivity.this.getApplicationContext(), _appComponent.getStartActivityClass());
                startActivity(mainIntent);
                finish();
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                _appComponent.getSplashWindowManager().show();
            }
        }
    }
}

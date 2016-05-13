package com.github.nikolaymakhonin.dependency_injections_common.classes;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;

import mika.controls.views.SplashScreenView;

public class SplashWindowManager {

    private Context _context;
    private LayoutInflater _layoutInflater;
    private Dialog _splashWindow;

    public SplashWindowManager(Context context, LayoutInflater layoutInflater) {
        _context = context;
        _layoutInflater = layoutInflater;
    }

    public void show() {
        if (_splashWindow == null) {
            _splashWindow = SplashScreenView.createSplashWindow(_context, _layoutInflater);
        }
        _splashWindow.show();
    }

    public void hide() {
        if (_splashWindow != null) {
            _splashWindow.hide();
            _splashWindow = null;
        }
    }

    public void dismiss() {
        if (_splashWindow != null) {
            _splashWindow.dismiss();
            _splashWindow = null;
            System.gc();
        }
    }
}

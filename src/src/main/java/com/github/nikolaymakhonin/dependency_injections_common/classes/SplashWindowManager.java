package com.github.nikolaymakhonin.dependency_injections_common.classes;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;

import mika.controls.views.SplashScreenView;

/** Overlay window using as loading screen during start application */
public class SplashWindowManager {

    private Context _context;
    private LayoutInflater _layoutInflater;
    private Dialog _splashWindow;

    public SplashWindowManager(Context context, LayoutInflater layoutInflater) {
        _context = context.getApplicationContext();
        _layoutInflater = layoutInflater;
    }

    /** Show hidden or create new and show */
    public void show() {
        if (_splashWindow == null) {
            _splashWindow = SplashScreenView.createSplashWindow(_context, _layoutInflater);
        }
        _splashWindow.show();
    }

    /** Set invisible */
    public void hide() {
        if (_splashWindow != null) {
            _splashWindow.hide();
        }
    }

    /** Close splash screen and releases the used memory */
    public void dismiss() {
        if (_splashWindow != null) {
            _splashWindow.dismiss();
            _splashWindow = null;
            // Splash window could contain the big picture, it is better at once to remove from memory
            System.gc();
        }
    }
}

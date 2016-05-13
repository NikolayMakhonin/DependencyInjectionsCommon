//package com.github.nikolaymakhonin.dependency_injections_common.factories;
//
//import android.app.Activity;
//import android.content.Context;
//import android.support.annotation.NonNull;
//
//import com.github.nikolaymakhonin.dependency_injections_common.components.AppComponentBase;
//import com.github.nikolaymakhonin.dependency_injections_common.components.DaggerAppComponentBase;
//import com.github.nikolaymakhonin.dependency_injections_common.components.DaggerServiceComponentBase;
//import com.github.nikolaymakhonin.dependency_injections_common.components.ServiceComponentBase;
//import com.github.nikolaymakhonin.dependency_injections_common.modules.app.AppModuleBase;
//import com.github.nikolaymakhonin.dependency_injections_common.modules.ServiceModuleBase;
//
//public final class AppComponentBaseFactory {
//
//    public static AppComponentBase buildAppComponentBase(@NonNull ServiceComponentBase serviceComponentBase, @NonNull Class<? extends Activity> startActivityClass) {
//        AppComponentBase appComponentBase = DaggerAppComponentBase.builder()
//            .serviceComponentBase(serviceComponentBase)
//            .appModuleBase(new AppModuleBase(startActivityClass))
//            .build();
//
//        return appComponentBase;
//    }
//
//    public static ServiceComponentBase buildServiceComponentBase(@NonNull Context appContext) {
//        ServiceComponentBase serviceComponentBase = DaggerServiceComponentBase.builder()
//            .serviceModuleBase(new ServiceModuleBase(appContext))
//            .build();
//
//        return serviceComponentBase;
//    }
//}

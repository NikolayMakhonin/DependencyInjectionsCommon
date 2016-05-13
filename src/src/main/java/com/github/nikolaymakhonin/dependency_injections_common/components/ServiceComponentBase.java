package com.github.nikolaymakhonin.dependency_injections_common.components;

import android.content.Context;

import com.github.nikolaymakhonin.dependency_injections_common.modules.service.ServiceModuleBase;
import com.github.nikolaymakhonin.dependency_injections_common.scopes.PerService;

import dagger.Component;
import io.fabric.sdk.android.Fabric;

@Component(modules = { ServiceModuleBase.class })
@PerService
public interface ServiceComponentBase {
    Context getContext();
    Fabric initFabric();
}


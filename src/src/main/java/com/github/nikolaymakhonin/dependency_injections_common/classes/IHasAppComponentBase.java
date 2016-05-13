package com.github.nikolaymakhonin.dependency_injections_common.classes;

import com.github.nikolaymakhonin.dependency_injections_common.components.AppComponentBase;

public interface IHasAppComponentBase<T extends AppComponentBase> {

    T getAppComponent();

}

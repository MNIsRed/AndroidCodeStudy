package com.mole.androidcodestudy.data;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AutoValueTestBean {
    abstract String stringProperty();
    abstract int intProperty();

    public static AutoValueTestBean create(String name, int numberOfLegs) {
        //AutoValue是注释生成，所以需要build后才有AutoValue_AutoValueTestBean类
        return new AutoValue_AutoValueTestBean(name, numberOfLegs);
    }
}

package com.mole.androidcodestudy.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LombokTestBean {
    private String stringProperty;
    private int intProperty;

    public LombokTestBean(String stringProperty, int intProperty) {
        this.stringProperty = stringProperty;
        this.intProperty = intProperty;
    }

}
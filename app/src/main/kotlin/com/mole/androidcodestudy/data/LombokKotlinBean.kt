package com.mole.androidcodestudy.data

import lombok.Getter
import lombok.Setter
import lombok.ToString

/**
 * kotlin好像没用
 */
@Setter
@Getter
@ToString
class LombokKotlinBean(name:String,id:Int) {
    private var name: String? = null
    private var id:Int? = null

    init {
        this.name = name
        this.id = id
    }
}
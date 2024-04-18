package com.mole.androidcodestudy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/04/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class LiveDateViewModel : ViewModel() {
    private val _chili: MutableLiveData<ChiliPeppers?> = MutableLiveData()
    var isShowAll : MutableLiveData<Boolean> = MutableLiveData(false)
    /**
     * map可以快速从其他LiveData获取新的LiveData数据
     */
    //val showText = _chili.map { it?.short }

    /**
     * switchMap是在某个LiveData数据变化时，根据新的数据获取新的LiveData数据
     */
    val showText = isShowAll.switchMap {
        if (it) {
            _chili.map { it?.introduction }
        } else {
            _chili.map { it?.short }
        }
    }

    fun generateNewContent(key: String) {
        _chili.value = list.find { it.name == key }
    }

    fun changeShowStatus() {
        isShowAll.value = !(isShowAll.value?:false)
    }

    data class ChiliPeppers(
        val name: String,
        val short: String,
        val introduction: String,
    )

    companion object {
        private val list = listOf(
            ChiliPeppers(
                "小米椒",
                "灌木状辣椒（学名：Capsicum frutescens）又名小米椒、鸡嘴椒[2]、辣虎[3]等，为茄科辣椒属的植物。分布在印度、欧洲、南美以及中国大陆的云南等地。[4]",
                "小米辣是低矮的灌木，最高可达两米。它们一开始像草，但是随着年龄的增长可以非常木质化。对于小米辣来说非常典型的是向上生长的花和果实。一根茎上有多颗花。花呈喇叭状，或者甚至筒状。花萼的端部呈齿形，与中国辣椒不同的是它的底部没有变粗的地方。花瓣白色或绿色。花瓣向外张开。花粉囊蓝色或紫色，偶尔黄色，雄蕊超出花粉囊至少1.5毫米。比起人工栽培的品种它的果实成熟的速度比较慢，容易掉落。果实成熟后一般变成红色[6]。"
            ),
            ChiliPeppers(
                "魔鬼椒",
                "断魂椒（英语：Bhut Jolokia；ghost pepper），又称魔鬼椒，其名称中的“鬼”来自梵语भूत（音译为富单那）；",
                "断魂椒是盛产于印度东北部阿萨姆邦的一种辣椒，在2007年的《吉尼斯世界纪录大全》中被确认为世界最辣的辣椒，实验最高辣度为1,041,427 SHU（史高维尔指标）。然而其纪录现在已经由另一种新培育的辣椒品种特立尼达蝎子壮汉T于2011年3月以146万度超越。目前纪录保持品种为X辣椒的269万度。\n" +
                        "\n" +
                        "印度鬼椒是阿萨姆邦和印度东北其他地区辣椒的杂交品种，基因分析显示印度鬼椒的基因大都来自黄灯笼椒，亦有部分基因来自辣椒。根据现有的证据，这种辣椒在印度已有数百年历史。印度鬼椒以前被用来做成田间和村落的藩篱，以防止象群闯入居民区。如今，这种辣椒已经成为阿萨姆地区食品业主要原料来源之一，只需少量就可以达到大量普通辣椒的效果[1]。\n" +
                        "\n" +
                        "依据史高维尔指标，世界第四辣的墨西哥魔鬼椒的辣度是577,000 SHU，印度鬼椒的辣度几乎是魔鬼椒的两倍。而普通辣椒辣度一般只有10000 SHU左右，中华地区的餐厅常运用到最辣的鸡心椒、朝天椒辣度大约是270,000 SHU。如此之高的辣度事实上已经很可能对食用者的身体健康造成严重危害，甚至可以导致死亡[2]。因此印度鬼椒在现实生活中更多是用于军事领域，它是催泪弹的重要原料之一。少数情况下，魔鬼椒会被少量掺入普通辣椒中，用于大幅增加菜品辣度。"
            )
        )
    }
}
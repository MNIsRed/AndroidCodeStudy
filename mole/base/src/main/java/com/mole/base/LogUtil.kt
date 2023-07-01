package com.mole.base

import com.orhanobut.logger.Logger

/**
 * @Description: log封装工具类
 * @author: zhang
 * @date: 2022/9/21
 */
class LogUtil {
    companion object {
        @JvmStatic
        fun d(vararg messages: Any?) {
            Logger.d(messages)
        }

        @JvmStatic
        fun e(vararg messages: Any?) {
            if (messages.isEmpty()) {
                Logger.e("give wrong error message")
            } else if (messages.size == 1 || messages[0] == null) {
                Logger.e("no error key", messages)
            } else {
                Logger.e(messages[0].toString(), messages.copyOfRange(1,messages.size))
            }
        }
    }
}
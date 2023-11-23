package com.mole.androidcodestudy.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
/**
 * 所有Fragment的基类
 * 当前功能
 * 1.打印生命周期日志
 */
private const val TAG = "BaseFragment"
open class BaseFragment : Fragment(){
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG,"${this.javaClass.simpleName}.onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG,"${this.javaClass.simpleName}.onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG,"${this.javaClass.simpleName}.onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG,"${this.javaClass.simpleName}.onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG,"${this.javaClass.simpleName}.onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG,"${this.javaClass.simpleName}.onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"${this.javaClass.simpleName}.onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG,"${this.javaClass.simpleName}.onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG,"${this.javaClass.simpleName}.onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(TAG,"${this.javaClass.simpleName}.onDetach")
    }
}

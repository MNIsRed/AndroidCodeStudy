package com.mole.androidcodestudy.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import com.mole.androidcodestudy.databinding.ActivityLocationBinding
import com.mole.androidcodestudy.extension.viewBinding
import java.util.Locale

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/11/01
 *     desc   : 测试Android各种获取地理位置的方案
 *     version: 1.0
 * </pre>
 */
class LocationActivity : BaseActivity(),OnRequestPermissionsResultCallback{
    private val binding by viewBinding(ActivityLocationBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(
                this@LocationActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(  Manifest.permission.ACCESS_COARSE_LOCATION),0)
            return
        }
        getLocation()
    }

    /**
     * 调用前已经检查过权限了
     */
    @SuppressLint("MissingPermission")
    private fun getLocation(){

        (getSystemService(Context.LOCATION_SERVICE) as? LocationManager)?.apply {
            val criteria = Criteria().apply{
                accuracy = Criteria.ACCURACY_COARSE
                isAltitudeRequired = false
                isBearingRequired = false
                isCostAllowed = true
                powerRequirement = Criteria.POWER_LOW
            }
            getBestProvider(criteria,true)?.also {
                this@LocationActivity.binding.tvLocationInfoProviderType.text = "定位的Provider：$it"
                //模拟器返回null，不确定，感觉应该会有一部分机器会返回null，通过这个方式获取定位并不可靠。
                getLastKnownLocation(it)?.let {location->
                    renderLocation(location)
                }
            }

        }
    }

    private fun renderLocation(location:Location){
        this@LocationActivity.binding.tvLocationInfo.text = "经度：${location.longitude}，维度${location.latitude}"
        val geocoder = Geocoder(this@LocationActivity, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(location.latitude,location.longitude,1,object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    runOnUiThread {
                        this@LocationActivity.binding.tvLocationInfoAddress.text = "地址是：${addresses[0].locality}"
                    }
                }
            })
        }else{
            geocoder.getFromLocation(location.latitude,location.longitude,1)?.let { address->
                this@LocationActivity.binding.tvLocationInfoAddress.text = "地址是：${address[0].locality}"
            }
        }
    }

    /**
     * getBestProvider的方式可能已经不推荐做了，可以通过getProviders获取所有信息来源方式，然后自行选择
     */
    @SuppressLint("MissingPermission")
    private fun getLocationInNative(context: Context?) {
        context?.apply {
            (getSystemService(Context.LOCATION_SERVICE) as? LocationManager)?.apply {
                getProviders(true).firstOrNull {
                    it == LocationManager.GPS_PROVIDER || it == LocationManager.NETWORK_PROVIDER
                }?.let { provider ->
                    getLastKnownLocation(provider)?.let { location ->
                        renderLocation(location)
                    }
                }
            }
        }
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getLocation()
    }
}
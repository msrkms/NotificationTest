package com.sajidur.notificationtest

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {

    private val push_token="push_token"

    var mContext: Context? = null
    private var sharedPreferences: SharedPreferences

    var editor: SharedPreferences.Editor? = null

    init {
        mContext = context
        sharedPreferences   = mContext!!.getSharedPreferences("doctor_pref", Context.MODE_PRIVATE)
    }



    fun setPushToken(token:String){
        editor = sharedPreferences.edit()
        editor!!.putString(push_token, token)
        editor!!.commit()
        editor!!.apply()
    }

    fun getPushToken(): String {
        return sharedPreferences.getString(push_token, "").toString()
    }


}
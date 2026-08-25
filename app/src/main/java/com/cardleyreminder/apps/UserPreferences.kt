package com.cardleyreminder.apps

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.Preferences
import com.cardleyreminder.apps.data.model.auth.User
import com.google.gson.Gson

val gson = Gson()

class UserPreferences(
    context : Context
) {

    private val applicationContext = context.applicationContext
    //private val dataStore : DataStore<Preferences>
    private val preferenses : SharedPreferences

    private val Context._dataStore: DataStore<Preferences> by preferencesDataStore("app_pref")

    private val dataStore : DataStore<Preferences> = context._dataStore

    init {
        preferenses = applicationContext.getSharedPreferences("driver_pref",Context.MODE_PRIVATE)
        /*dataStore = applicationContext.createDataStore(
            name = "driver_pref"
        )*/
    }

    val token : String?
        get() = preferenses.getString(KEY_TOKEN,BASE_TOKEN)
    val isLoggedIn : Boolean
        get() = preferenses.getBoolean(KEY_LOGGED_IN,false)

    val fcmToken : String?
        get() = preferenses.getString(KEY_FCM_TOKEN,"")

    val isKeyboardSoundOn : Boolean
        get() = preferenses.getBoolean(KEY_KEYBOARD_SOUND,true)
    val isCardFlipSoundOn : Boolean
        get() = preferenses.getBoolean(KEY_CARD_FLIP_SOUND,true)
    val user : User?
        get() = gson.fromJson(preferenses.getString(KEY_USER,""),User::class.java)

    fun saveUser(user: User){
        val editor = preferenses.edit()
        editor.putString(KEY_USER,gson.toJson(user))
        editor.putString(KEY_TOKEN,user.token)
        editor.putBoolean(KEY_LOGGED_IN,true)
        editor.apply()
        editor.commit()
    }

    fun saveKeybaordSound(enable: Boolean){
        val editor = preferenses.edit()
        editor.putBoolean(KEY_KEYBOARD_SOUND,enable)
        editor.apply()
        editor.commit()
    }

    fun saveCardFlipSound(enable: Boolean){
        val editor = preferenses.edit()
        editor.putBoolean(KEY_CARD_FLIP_SOUND,enable)
        editor.apply()
        editor.commit()
    }

    fun saveFcmToken(fcmToken: String){
        val editor = preferenses.edit()
        editor.putString(KEY_FCM_TOKEN,fcmToken)
        editor.apply()
        editor.commit()
    }

    suspend fun clear(){
        preferenses.edit().clear().apply()
    }

    companion object{
        private val KEY_TOKEN = "key_token"
        private val KEY_LOGGED_IN = "key_logged_in"
        private val BASE_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2NhcmRseS5wZXJzYXVzaXZlLmluIiwiaWF0IjoxNjk5MjYzODk1LCJqdGkiOjgzNjc1MywiYXBpX2tleSI6InM1NmJ5NzMyMTIzNDMyODlmZHNmaXRkc2RuZSJ9.MVALghj4JwwECkHgwSQ7PpZGTCrXVm-7DzdZ2i881fw"
        private val KEY_USER = "key_user"
        private val KEY_KEYBOARD_SOUND = "key_keyboard_sound"
        private val KEY_CARD_FLIP_SOUND = "key_card_flip_sound"
        private val KEY_FCM_TOKEN = "key_fcm_token"

    }
}
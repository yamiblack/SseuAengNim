package com.jaktongdan.android.sseuaengnim

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
    private val preference by lazy { getSharedPreferences(Preference.SETTINGS.id, Context.MODE_PRIVATE)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this,
                        if(preference.getBoolean(Settings.AUTOLOGIN.id, Settings.AUTOLOGIN.default as Boolean)
                                && kAuth.currentUser != null)
                            MainActivity::class.java
                        else {
                            kAuth.signOut()
                            LoginActivity::class.java
                        }
                ))
            finish()
        }, 1500)
    }
}
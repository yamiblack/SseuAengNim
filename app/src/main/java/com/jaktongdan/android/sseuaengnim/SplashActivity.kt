package com.jaktongdan.android.sseuaengnim

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this,
                        if(kPreference(this).getBoolean(Settings.AUTOLOGIN.id, Settings.AUTOLOGIN.default as Boolean)
                                && kAuth.currentUser != null) {
                            kPreference(this).edit().putString(Settings.NICKNAME.id, kAuth.currentUser!!.displayName).apply()
                            MainActivity::class.java
                        } else {
                            kAuth.signOut()
                            LoginActivity::class.java
                        }
                ))
            finish()
        }, 1500)
    }
}
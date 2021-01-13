package com.jaktongdan.android.sseuaengnim

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.jaktongdan.android.sseuaengnim.databinding.ActivityPrivateSettingsBinding

class PrivateSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivateSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivateSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "사용자 설정"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
                .replace(R.id.layoutPrivateSettings, SettingFragment()).commit()

        binding.buttonSettingsSignOut.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            kAuth.signOut()
            finishAffinity()
        }

        kPreference(this).registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            val value = sharedPreferences.all[key]
            when(key) {
                Settings.NICKNAME.id -> {
                    if((value as String).isBlank())
                        sharedPreferences.edit().putString(key, kAuth.currentUser!!.displayName).apply()
                    else {
                        kAuth.currentUser!!.updateProfile(userProfileChangeRequest { displayName = value })
                        kFirestore.collection(Firestore.MEMBER.name).document(kAuth.currentUser!!.uid)
                                .update("nickname", value)
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    class SettingFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.preference_private_settings)
        }
    }
}
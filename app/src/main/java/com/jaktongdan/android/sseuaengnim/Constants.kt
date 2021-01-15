package com.jaktongdan.android.sseuaengnim

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

enum class Firestore {
    MEMBER, BOARD, POST, COMMENT
}

enum class Preference(var id: String) {
    SETTINGS("settings")
}

enum class Settings(var id: String, var default: Any) {
    AUTOLOGIN("auto_login", true),
    NICKNAME("nickname", "")
}

fun kPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
fun kTimeText(time: Date): String = when (val diffTime = Date().time - time.time) {
        in 0L..1000L -> "방금 전"
        in 1000L..60*1000L -> "${diffTime / 1000}초 전"
        in 60*1000L..60*60*1000L -> "${diffTime / 60 / 1000}분 전"
        in 60*60*1000L..60*60*24*1000L -> "${diffTime / 60 / 60 / 1000}시간 전"
        in 60*60*24*1000L..60*60*24*30*1000L -> "${diffTime / 60 / 60 / 24 / 1000}일 전"
        else -> SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(time)
}

//Google Firebase
val kAuth = Firebase.auth
val kFirestore = Firebase.firestore
val kStorage = Firebase.storage.reference
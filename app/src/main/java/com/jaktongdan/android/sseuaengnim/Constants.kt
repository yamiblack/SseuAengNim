package com.jaktongdan.android.sseuaengnim

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

enum class Preference(var id: String) {
    SETTINGS("settings")
}

enum class Settings(var id: String, var default: Any) {
    AUTOLOGIN("auto_login", false)
}

//Google Firebase
val kAuth = Firebase.auth
val kFirestore = Firebase.firestore
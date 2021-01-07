package com.jaktongdan.android.sseuaengnim.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import java.util.*

data class BoardData(
        @get:Exclude var id: String = "",
        val name: String = "",
        val owner: DocumentReference? = null,
        val data: Date = Date(),
        val description: String = ""
)
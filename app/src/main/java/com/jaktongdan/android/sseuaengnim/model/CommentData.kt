package com.jaktongdan.android.sseuaengnim.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import java.util.*

data class CommentData(
        @get:Exclude var id: String = "",
        val content: String = "",
        val writer: DocumentReference? = null,
        val date: Date = Date(),
        val thumbs: List<String> = listOf()
)
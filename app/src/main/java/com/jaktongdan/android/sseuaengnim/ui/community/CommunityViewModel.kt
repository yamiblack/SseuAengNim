package com.jaktongdan.android.sseuaengnim.ui.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CommunityViewModel : ViewModel() {
    private val mText = MutableLiveData<String>().apply {
        value = "This is notifications fragment"
    }
    val text: LiveData<String>
        get() = mText
}
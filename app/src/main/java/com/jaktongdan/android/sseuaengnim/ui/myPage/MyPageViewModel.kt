package com.jaktongdan.android.sseuaengnim.ui.myPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPageViewModel : ViewModel() {
    private var mText = MutableLiveData<String>().apply {
        value = "This is my page fragment"
    }
    val text: LiveData<String>
        get() = mText
}
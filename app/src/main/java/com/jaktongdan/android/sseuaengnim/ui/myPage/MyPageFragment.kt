package com.jaktongdan.android.sseuaengnim.ui.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jaktongdan.android.sseuaengnim.R
import com.jaktongdan.android.sseuaengnim.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {
    private lateinit var myPageViewModel: MyPageViewModel
    private lateinit var binding: FragmentMyPageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMyPageBinding.inflate(inflater)
        myPageViewModel = ViewModelProvider(this).get(MyPageViewModel::class.java)

        binding.tabLayoutMyPage.apply {
            getTabAt(0)?.text = "내가 쓴 글"
            getTabAt(1)?.text = "내가 쓴 댓글"

            tabTextColors = getColorStateList(context, android.R.color.black)
        }

        return binding.root
    }
}
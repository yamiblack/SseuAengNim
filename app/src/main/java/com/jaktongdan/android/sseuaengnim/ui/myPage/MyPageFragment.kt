package com.jaktongdan.android.sseuaengnim.ui.myPage

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.jaktongdan.android.sseuaengnim.*
import com.jaktongdan.android.sseuaengnim.databinding.FragmentMyPageBinding
import java.util.*

class MyPageFragment : Fragment() {
    private lateinit var myPageViewModel: MyPageViewModel
    private lateinit var binding: FragmentMyPageBinding

    private val loadPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            kFirestore.collection(Firestore.MEMBER.name).document(kAuth.uid!!).get().addOnSuccessListener { docs ->
                docs.getString("photo")?.let { url ->
                    kStorage.child("profiles/$url").delete().addOnSuccessListener { _ ->
                        val fileName = UUID.randomUUID().toString()
                        kStorage.child("profiles/$fileName").putFile(it)
                        kFirestore.collection(Firestore.MEMBER.name).document(kAuth.uid!!).update("photo", fileName)
                    }
                }
            }
        }
    }

    private val checkPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) loadPhoto.launch("image/*")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMyPageBinding.inflate(inflater)
        myPageViewModel = ViewModelProvider(this).get(MyPageViewModel::class.java)

        loadProfile()

        binding.tabLayoutMyPage.apply {
            getTabAt(0)?.text = "내가 쓴 글"
            getTabAt(1)?.text = "내가 쓴 댓글"

            tabTextColors = getColorStateList(context, android.R.color.black)
        }

        binding.buttonMyPageSettings.setOnClickListener {
            startActivity(Intent(context, PrivateSettingsActivity::class.java))
        }

        binding.cardViewMyPageProfile.setOnClickListener {
            checkPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            //loadPhoto()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadProfile()
    }

    private fun loadProfile() {
        binding.textMyPageName.text = kAuth.currentUser!!.displayName!!
        loadProfileImage()
    }

    private fun loadProfileImage() {
        kFirestore.collection(Firestore.MEMBER.name).document(kAuth.uid!!).get()
                .addOnSuccessListener {
                    kStorage.child("profiles/${it.getString("photo")}").downloadUrl.addOnSuccessListener { uri ->
                        Glide.with(this).load(uri).into(binding.imageMyPageProfile)
                    }
                }
    }
}
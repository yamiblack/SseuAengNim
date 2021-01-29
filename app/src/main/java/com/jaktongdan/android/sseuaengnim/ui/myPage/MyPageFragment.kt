package com.jaktongdan.android.sseuaengnim.ui.myPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jaktongdan.android.sseuaengnim.*
import com.jaktongdan.android.sseuaengnim.adapter.MyPageViewPagerAdapter
import com.jaktongdan.android.sseuaengnim.databinding.FragmentMyPageBinding
import java.util.*

class MyPageFragment : Fragment() {
    private lateinit var binding: FragmentMyPageBinding

    private val loadPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            kFirestore.collection(Firestore.MEMBER.name).document(kAuth.uid!!).get().addOnSuccessListener { docs ->
                docs.getString("photo")?.let { url ->
                    kStorage.child("profiles/$url").delete().addOnCompleteListener { _ ->
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
                              savedInstanceState: Bundle?): View {
        binding = FragmentMyPageBinding.inflate(inflater)

        loadProfile()

        kFirestore.collectionGroup("post").whereEqualTo("writer", kFirestore.collection(Firestore.MEMBER.name).document(kAuth.uid!!))
                .get().addOnSuccessListener {
                    it.documents.forEach { post ->
                        binding.textMyPageThumbs.text =
                                (Integer.parseInt(binding.textMyPageThumbs.text.toString()) + ((post["thumbs"] as List<String>?)?.size ?: 0)).toString()
                    }
                }

        kFirestore.collection(Firestore.COMMENT.name).whereEqualTo("writer", kFirestore.collection(Firestore.MEMBER.name).document(kAuth.uid!!))
                .get().addOnSuccessListener {
                    it.documents.forEach { post ->
                        binding.textMyPageThumbs.text =
                                (Integer.parseInt(binding.textMyPageThumbs.text.toString()) + ((post["thumbs"] as List<String>?)?.size ?: 0)).toString()
                    }
                }

        binding.scrollViewMyPage.run {
            header = binding.tabLayoutMyPage
            stickListener = { _ -> }
            freeListener = { _ -> }
        }

        binding.viewPagerMyPage.adapter = MyPageViewPagerAdapter()
        binding.viewPagerMyPage.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPagerMyPage.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.HORIZONTAL))

        TabLayoutMediator(binding.tabLayoutMyPage, binding.viewPagerMyPage) { tab: TabLayout.Tab, i: Int ->
            tab.text = when(i) {
                0 -> "내가 쓴 글"
                else -> "내가 쓴 댓글"
            }
        }.attach()

        binding.buttonMyPageSettings.setOnClickListener {
            startActivity(Intent(context, PrivateSettingsActivity::class.java))
        }

        binding.cardViewMyPageProfile.setOnClickListener {
            checkPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
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
                        try {
                            Glide.with(requireContext()).load(uri).into(binding.imageMyPageProfile)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
    }
}
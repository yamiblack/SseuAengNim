package com.jaktongdan.android.sseuaengnim.ui.community

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jaktongdan.android.sseuaengnim.R
import com.jaktongdan.android.sseuaengnim.databinding.FragmentCommunityBinding

class CommunityFragment : Fragment() {
    private lateinit var communityViewModel: CommunityViewModel
    private lateinit var binding: FragmentCommunityBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        communityViewModel = ViewModelProvider(this).get(CommunityViewModel::class.java)
        binding = FragmentCommunityBinding.inflate(inflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_community, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.addBoard -> Toast.makeText(context, "ADD BOARD", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }
}
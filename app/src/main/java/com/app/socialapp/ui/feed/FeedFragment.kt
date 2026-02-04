package com.app.socialapp.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.socialapp.data.adapters.FeedAdapter
import com.app.socialapp.data.model.FeedPostModel
import com.app.socialapp.databinding.FragmentFeedBinding
import com.app.socialapp.R

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var feedAdapter: FeedAdapter
    private val feedList = mutableListOf<FeedPostModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        binding.rvFeed.layoutManager = LinearLayoutManager(requireContext())
        feedAdapter = FeedAdapter(feedList)
        binding.rvFeed.adapter = feedAdapter

        // Load sample data (replace with real data later)
        loadFeedData()

        binding.imgAvatar.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
    }

    private fun loadFeedData() {
        // Sample data
        feedList.add(
            FeedPostModel(
                userName = "Sarah Jenkins",
                timeLocation = "2 hours ago • Vancouver",
                title = "Golden hour at the lake",
                description = "The reflection of the mountains was absolutely stunning today. Swipe for more...",
                likes = "1.2k",
                comments = "48",
                userImage = R.drawable.man,
                postImage = R.drawable.ic_plant2
            )
        )

        feedList.add(
            FeedPostModel(
                userName = "John Doe",
                timeLocation = "5 hours ago • Toronto",
                title = "Morning vibes",
                description = "Sunrise over the city looked amazing!",
                likes = "860",
                comments = "20",
                userImage = R.drawable.man,
                postImage = R.drawable.ic_plant
            )
        )

        feedAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

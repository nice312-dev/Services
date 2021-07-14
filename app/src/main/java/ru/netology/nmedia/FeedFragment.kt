package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_feed.*
import ru.netology.nmedia.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.adapter.PostAdapterClickListener
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )
        val adapter = PostAdapter(
            object : PostAdapterClickListener {

                override fun onAttachmentClicked(post: Post) {
                    findNavController().navigate(
                        R.id.action_feedFragment_to_attach_viewer,
                        Bundle().apply {
                            textArg = post.attachment?.url
                        }
                    )
                }

                override fun onEditClicked(post: Post) {
                    findNavController().navigate(
                        R.id.action_feedFragment_to_newPostFragment,
                        Bundle().apply {
                            textArg = post.content
                        }
                    )
                    viewModel.edit(post)
                }

                override fun onRemoveClicked(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onLikeClicked(post: Post) {
                    viewModel.likeById(post)
                }

                override fun onShareClicked(post: Post) {

                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                    viewModel.shareById(post)
                }

            },
            "${BuildConfig.BASE_URL}"

        )


        binding.list.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener{
            viewModel.loadPosts()
            swipe_refresh.isRefreshing = false
        }

        viewModel.dataState.observe(viewLifecycleOwner, { state ->
            binding.errorGroup.isVisible = false
            binding.progress.isVisible = state.loading
            binding.swipeRefresh.isRefreshing = state.refreshing
            if(state.error) {
//                binding.errorGroup.isVisible = state.error
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry_loading) {viewModel.loadPosts()}
                    .show()
            }
        })

        viewModel.data.observe(viewLifecycleOwner, {state ->
            binding.emptyText.isVisible = state.empty

            if (adapter.itemCount == 0){
                adapter.submitList(state.posts)
            }

            if  (state.posts.size == adapter.itemCount){
                adapter.submitList(state.posts)
            } else{
                binding.newpostsBtn.isGone = false
                viewModel.updateNewPostsList(state.posts)
            }
        })

        viewModel.newerCount.observe(viewLifecycleOwner) { state ->

            println(state)
        }

        binding.retryButton.setOnClickListener {
            viewModel.refreshPosts()
        }

        binding.newpostsBtn.setOnClickListener {
            binding.newpostsBtn.isGone = true
            adapter.submitList(viewModel.addNewPosts())
            binding.list.postDelayed(Runnable {
                binding.list.smoothScrollToPosition(0)
            }, 700)

        }


        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
        return binding.root
    }


}
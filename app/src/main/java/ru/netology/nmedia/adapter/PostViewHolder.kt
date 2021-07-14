package ru.netology.nmedia.adapter

import android.opengl.Visibility
import android.view.View
import android.widget.PopupMenu
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ItemPostBinding
import kotlin.math.floor

class PostViewHolder(
    private val binding: ItemPostBinding,
    private val listener: PostAdapterClickListener,
    private val url: String
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            avatarIv.setImageResource(R.drawable.avatar)
            authorTv.text = post.author
            publishedTv.text = post.published
            textTv.text = post.content
            like.isChecked = post.likedByMe
            like.text = setRoundCount(post.likeCount)
            share.text = setRoundCount(post.shareCount)
            menu.visibility = if (post.ownedByMe) View.VISIBLE else View.INVISIBLE

            like.setOnClickListener {
                listener.onLikeClicked(post)
            }
            share.setOnClickListener {
                listener.onShareClicked(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    menu.setGroupVisible(R.id.owned, post.ownedByMe)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                listener.onRemoveClicked(post)
                                true
                            }
                            R.id.edit -> {
                                listener.onEditClicked(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
            attachment.setOnClickListener  {
                listener.onAttachmentClicked(post)
            }
            Glide.with(binding.avatarIv)
                .load("$url/avatars/${post.authorAvatar}")
                .placeholder(R.drawable.ic_loading_100dp)
                .error(R.drawable.ic_error_100dp)
                .timeout(10_000)
                .transform(MultiTransformation(FitCenter(), CircleCrop()))
                .into(binding.avatarIv)

            if(!post.attachment?.url.isNullOrEmpty()) {
                binding.attachment.visibility = View.VISIBLE
                Glide.with(binding.attachment)
                    .load("$url/media/${post.attachment?.url}")
                    .error(R.drawable.ic_error_100dp)
                    .timeout(10_000)
                    .into(binding.attachment)
            } else {
                binding.attachment.visibility = View.GONE
            }
        }
    }

    private fun setRoundCount(value: Int): String {
        return when (value) {
            0 -> ""
            in 1..999 -> value.toString()
            in 1000..1099 -> "1K"
            in 1100..9999 -> (floor(value.toDouble() / 1000 * 10f) / 10f).toString() + "K"
            in 10000..999_999 -> floor(value.toDouble() / 1000).toInt().toString() + "K"
            else -> (floor(value.toDouble() / 1_000_000 * 10f) / 10f).toString() + "M"
        }
    }
}
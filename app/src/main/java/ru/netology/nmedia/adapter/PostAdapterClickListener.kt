package ru.netology.nmedia.adapter

import ru.netology.nmedia.dto.Post

//typealias OnLikeListener = (post: Post) -> Unit
//typealias OnShareListener = (post: Post) -> Unit
//typealias OnRemoveListener = (post: Post) -> Unit

interface PostAdapterClickListener {
    fun onEditClicked(post: Post)
    fun onRemoveClicked(post: Post)
    fun onLikeClicked(post: Post)
    fun onShareClicked(post: Post)
    fun onAttachmentClicked(post: Post)
//    fun onVideoClicked(post: Post)
}
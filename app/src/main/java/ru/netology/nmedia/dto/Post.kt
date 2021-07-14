package ru.netology.nmedia.dto

import android.widget.ImageView
import ru.netology.nmedia.enumiration.AttachmentType

data class Post(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val likeCount: Int = 0,
    val shareCount: Int = 0,
    val attachment: Attachment? = null,
    val ownedByMe: Boolean = false,
)
data class Attachment(
    val url: String,
    val type: AttachmentType,
)


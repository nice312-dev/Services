package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostWorkEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val postId: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val likeCount: Int = 0,
    val shareCount: Int = 0,
    @Embedded
    var attachment: AttachmentEmdeddable?,
    var uri: String? = null,
) {
    fun toDto() = Post(
        postId,
        authorId,
        author,
        authorAvatar,
        content,
        published,
        likedByMe,
        likeCount,
        shareCount,
        attachment?.toDto()
    )

    companion object {
        fun fromDto(dto: Post) =
            PostWorkEntity(
                0L,
                dto.id,
                dto.authorId,
                dto.author,
                dto.authorAvatar,
                dto.content,
                dto.published,
                dto.likedByMe,
                dto.likeCount,
                dto.shareCount,
                AttachmentEmdeddable.fromDto(dto.attachment)
            )
    }
}
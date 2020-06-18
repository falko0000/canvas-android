/*
 * Copyright (C) 2017 - present Instructure, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */

package com.instructure.pandautils.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import com.instructure.canvasapi2.models.Author
import com.instructure.canvasapi2.models.BasicUser
import com.instructure.canvasapi2.models.Conversation
import com.instructure.canvasapi2.models.User
import com.instructure.pandautils.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception


object ProfileUtils {

    private val noPictureUrls = listOf(
            "images/dotted_pic.png",
            "images%2Fmessages%2Favatar-50.png",
            "images/messages/avatar-50.png",
            "images/messages/avatar-group-50.png"
    )

    @JvmStatic
    fun shouldLoadAltAvatarImage(avatarUrl: String?): Boolean {
        return avatarUrl.isNullOrBlank() || avatarUrl.indexOfAny(noPictureUrls) >= 0
    }

    @JvmStatic
    fun getUserInitials(username: String?): String {
        val name: String = username.takeUnless { it.isNullOrBlank() } ?: return "?"
        val initials = name.trim().split(Regex("\\s+")).map { it.toUpperCase()[0] }
        return if (initials.size == 2) {
            initials.joinToString("")
        } else {
            initials[0].toString()
        }
    }

    @JvmStatic
    fun loadAvatarForUser(avatar: CircleImageView, user: User) {
        loadAvatarForUser(avatar, user.name, user.avatarUrl)
    }

    @JvmStatic
    fun loadAvatarForUser(avatar: CircleImageView, user: BasicUser) {
        loadAvatarForUser(avatar, user.name, user.avatarUrl)
    }

    @JvmStatic
    fun loadAvatarForUser(avatar: CircleImageView, user: Author) {
        loadAvatarForUser(avatar, user.displayName, user.avatarImageUrl)
    }

    @JvmStatic
    fun loadAvatarForUser(avatar: CircleImageView, name: String?, url: String?) {
        val context = avatar.context
        if (shouldLoadAltAvatarImage(url)) {
            Picasso.get().cancelRequest(avatar)
            avatar.setAvatarImage(context, name)
        } else {
            Picasso.get()
                    .load(url)
                    .fit()
                    .placeholder(R.drawable.recipient_avatar_placeholder)
                    .centerCrop()
                    .into(avatar, object : Callback {
                        override fun onSuccess() {}
                        override fun onError(e: Exception?) {
                            avatar.setAvatarImage(context, name)
                        }
                    })
        }
    }

    /**
     * Sets up the provided [avatar] for the given [conversation].
     *
     * If there are either one or two participants in the conversation, the profile image for the first participant
     * will be loaded into the the avatar view. The [onClick] callback may be provided to take action when the
     * avatar is clicked.
     *
     * If [onClick] is non-null then accessibility behavior will be set up on the avatar as well. As such, it is
     * important to provide this callback only when tapping on the avatar would lead to an actionable outcome. For
     * example, in the Teacher app a teacher can view a student's summary, but only in the context of a course. In this
     * case a callback should not be provided if there is no course associated with the conversation.
     *
     * If there are more than two participants, a group avatar will be shown and accessibility will be disabled.
     */
    @JvmStatic
    fun configureAvatarForConversation(
        avatar: CircleImageView,
        conversation: Conversation,
        onClick: ((BasicUser) -> Unit)? = null
    ) {
        // Clear click listener and A11y
        avatar.setOnClickListener(null)
        avatar.clearAvatarA11y()
        avatar.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO

        val users = conversation.participants

        when (users.size) {
            0 -> return
            1, 2 -> {
                val firstUser = users[0]
                firstUser.avatarUrl = conversation.avatarUrl
                loadAvatarForUser(avatar, firstUser.name, firstUser.avatarUrl)

                // Set click listener and A11y
                if (onClick != null) {
                    avatar.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
                    avatar.setupAvatarA11y(firstUser.name)
                    avatar.onClick { onClick(firstUser) }
                }
            }
            else -> avatar.setImageResource(R.drawable.vd_group)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getInitialsAvatarBitMap(
            context: Context,
            username: String,
            backgroundColor: Int = Color.WHITE,
            textColor: Int = Color.GRAY,
            borderColor: Int = Color.GRAY
    ): Bitmap {
        val initials = ProfileUtils.getUserInitials(username)
        val drawable = TextDrawable.builder()
                .beginConfig()
                .height(context.resources.getDimensionPixelSize(R.dimen.avatar_size))
                .width(context.resources.getDimensionPixelSize(R.dimen.avatar_size))
                .toUpperCase()
                .textColor(textColor)
                .useFont(Typeface.DEFAULT_BOLD)
                .withBorderColor(borderColor)
                .withBorder(context.DP(1).toInt())
                .endConfig()
                .buildRound(initials, backgroundColor)

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }
}

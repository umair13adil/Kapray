package com.blackbox.apps.karay.ui.items

import android.animation.Animator
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.post.Post
import com.blackbox.apps.karay.utils.Constants
import com.blackbox.apps.karay.utils.DateTimeUtils
import com.blackbox.apps.karay.utils.Utils
import com.squareup.picasso.Picasso
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.helpers.AnimatorHelper
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFilterable
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flexibleadapter.utils.DrawableUtils
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.item_list_post.view.*

class PostItem(val post: Post) : AbstractFlexibleItem<PostItem.ParentViewHolder>(), IFilterable<String> {

    init {
        isDraggable = false
        isSwipeable = true
        isSelectable = true
    }

    /**
     * When an item is equals to another?
     * Write your own concept of equals, mandatory to implement.
     */
    override fun equals(o: Any?): Boolean {
        if (o is PostItem) {
            val inItem = o as PostItem?
            return this.post.idFeed == inItem!!.post.idFeed
        }
        return false
    }

    override fun hashCode(): Int {
        return post.idFeed.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_list_post
    }

    override fun filter(constraint: String?): Boolean {
        /*return post.originalTitle != null && post.originalTitle?.toLowerCase()?.trim()?.contains(constraint!!)!! ||
                post.overview != null && post.overview?.toLowerCase()?.trim()?.contains(constraint!!)!!*/
        return true
    }

    override fun createViewHolder(view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?): ParentViewHolder {
        return ParentViewHolder(view!!, adapter!!)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, viewHolder: ParentViewHolder, position: Int, payloads: MutableList<Any>?) {

        val dateTimeUtils = DateTimeUtils.create(viewHolder.userImage.context)

        Picasso.with(viewHolder.userImage.context)
                .load(post.photoAvatar)
                .centerCrop()
                .resize(50, 50)
                .placeholder(R.drawable.ic_user)
                .into(viewHolder.userImage)

        viewHolder.description.text = post.text
        viewHolder.postTitle.text = post.title
        viewHolder.time.text = dateTimeUtils.getFullDateTimeString(post.time!!)
        viewHolder.userName.text = "Posted By: ${post.name}"

        viewHolder.shareButton.setOnClickListener {
            val intent2 = Intent()
            intent2.action = Intent.ACTION_SEND
            intent2.type = "text/plain"
            intent2.putExtra(Intent.EXTRA_TEXT, post)
            viewHolder.shareButton.context.startActivity(Intent.createChooser(intent2, "Share via"))
        }

        val context = viewHolder.itemView.context
        val drawable: Drawable = DrawableUtils.getSelectableBackgroundCompat(
                ContextCompat.getColor(context, R.color.colorWhite),
                ContextCompat.getColor(context, R.color.colorAccent), // pressed background
                ContextCompat.getColor(context, R.color.colorPrimaryDark)) // ripple color
        DrawableUtils.setBackgroundCompat(viewHolder.frontView, drawable)
    }

    class ParentViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {

        var userImage: ImageView
        var postTitle: TextView
        var time: TextView
        var description: TextView
        var userName: TextView
        var coverPhoto: AppCompatImageView
        var shareButton: AppCompatImageView

        /*var fView: View
        var leftView: View
        var rightView: View
        var leftImage: ImageView
        var rightImage: ImageView*/

        var swiped = false

        init {
            this.postTitle = view.tv_name
            this.time = view.tv_time
            this.userName = view.tv_user
            this.description = view.tv_content
            this.userImage = view.iv_avatar
            this.coverPhoto = view.iv_feed
            this.shareButton = view.iv_share

            /*this.fView = view.front_view
            this.leftView = view.rear_left_view
            this.rightView = view.rear_right_view
            this.leftImage = view.left_image
            this.rightImage = view.right_image*/

            //Apply Fonts

        }

        override fun scrollAnimators(animators: List<Animator>, position: Int, isForward: Boolean) {
            AnimatorHelper.slideInFromTopAnimator(animators, itemView, mAdapter.recyclerView)
        }

        override fun onItemReleased(position: Int) {
            swiped = mActionState == ItemTouchHelper.ACTION_STATE_SWIPE
            super.onItemReleased(position)
        }

        override fun getActivationElevation(): Float {
            return 0f
        }

        override fun shouldActivateViewWhileSwiping(): Boolean {
            return true//default=false
        }

        override fun shouldAddSelectionInActionMode(): Boolean {
            return false//default=false
        }

        /*override fun getFrontView(): View {
            return fView
        }

        override fun getRearLeftView(): View {
            return leftView
        }

        override fun getRearRightView(): View {
            return rightView
        }*/
    }

    override fun toString(): String {
        return "Item[" + super.toString() + "]"
    }
}

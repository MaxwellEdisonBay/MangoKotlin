package com.mangoapp.expands

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.mangoapp.R
import com.mangoapp.first_launch.ClickListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import java.io.File
import java.lang.ref.WeakReference


class AddPhotoAdapter(val uri: Uri?, val clickListener: ClickListener) :
    Item<GroupieViewHolder>(), View.OnClickListener {

    private var imageView: ImageView? = null
    private var removeButton: Button? = null
    private var listenerRef: WeakReference<ClickListener>? = null
    private var position:Int = 0

    override fun getLayout(): Int {
        return R.layout.image_uploader_card
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        listenerRef = WeakReference(clickListener)
        imageView = viewHolder.itemView.findViewById(R.id.image_uploader_card_imageview)
        removeButton = viewHolder.itemView.findViewById(R.id.image_uploader_card_remove_button)
        imageView?.setOnClickListener(this)
        removeButton?.setOnClickListener(this)
        this.position = position

        if (uri == null) {
            removeButton!!.visibility = View.INVISIBLE
            imageView?.setImageResource(R.drawable.empty_photo_icon)
        }
        else {
            removeButton!!.visibility = View.VISIBLE
            Picasso.get().load(File(uri.toString())).fit().centerCrop().into(imageView)
        }
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            listenerRef?.get()?.onPositionClicked(position, p0.id);
        }


    }

}
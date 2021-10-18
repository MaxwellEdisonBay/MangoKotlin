package com.mangoapp.content_loaders

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.mangoapp.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import java.io.File


class GalleryAdapter(val imagePreview: ImagePreview, val selectedUris: MutableList<String?>) :
    Item<GroupieViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.image_preview_card

    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val imageView =
            viewHolder.itemView.findViewById<ImageView>(R.id.image_preview_card_image_view)
        val selectedIcon =
            viewHolder.itemView.findViewById<ImageView>(R.id.image_preview_card_selected_number)

        val path = imagePreview.uri.toString()
        if (path in selectedUris)
            selectedIcon.visibility = View.VISIBLE
        else
            selectedIcon.visibility = View.INVISIBLE
//        Picasso.get().load(File(path)).resize(100,100).into(imageView)
        Picasso.get().load(File(path)).fit().centerCrop().into(imageView)

//        Log.d("PICASSO", "Image loaded")
    }


}
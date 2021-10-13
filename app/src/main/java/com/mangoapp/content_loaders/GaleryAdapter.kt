package com.mangoapp.content_loaders

import android.net.Uri
import android.widget.ImageView
import com.mangoapp.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


class GaleryAdapter(val uri: Uri?, number:Int) :
    Item<GroupieViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.image_preview_card

    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val imageView = viewHolder.itemView.findViewById<ImageView>(R.id.image_preview_card_image_view)
//        val selectedIcon = viewHolder.itemView.findViewById<Button>(R.id.image_preview_card_selected_number)
        Picasso.get().load(uri).into(imageView)




    }


}
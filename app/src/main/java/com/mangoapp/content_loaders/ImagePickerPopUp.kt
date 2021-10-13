package com.mangoapp.content_loaders

import android.os.Bundle
import com.mangoapp.R

import android.widget.RelativeLayout

import android.app.Activity
import android.view.View

import android.database.Cursor
import android.net.Uri
import android.util.Log
import android.widget.Button
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import android.provider.MediaStore

import android.provider.MediaStore.MediaColumns


class ImagePickerPopUp : Activity() {


    val adapter = GroupAdapter<GroupieViewHolder>()
    var uris = mutableListOf<Uri?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker_pop_up)

        val exitField = findViewById<RelativeLayout>(R.id.image_picker_popup_exit_field)
        val panel = findViewById<SlidingUpPanelLayout>(R.id.sliding_layout)
        val recyclerView = findViewById<RecyclerView>(R.id.image_picker_popup_recycler_view)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener{ item: Item<GroupieViewHolder>, view: View ->
            val selectedFlag = view.findViewById<Button>(R.id.image_preview_card_selected_number)
            val card = item as GaleryAdapter
            if (!selectedFlag.isVisible) {
                uris.add(card.uri)
                selectedFlag.visibility = View.VISIBLE
            }
            else{
                uris.remove(card.uri)
                selectedFlag.visibility = View.INVISIBLE
            }

        }

        exitField.setOnClickListener {
            finish()
        }
        val list = getImagesPath(this)
        var num = 0
        for (i in list){
            Log.d("URI",Uri.parse(i!!).toString())
            adapter.add(GaleryAdapter(Uri.parse(i),num))
        }
        Log.d("LST", list.toString())

//        panel.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
//            override fun onPanelSlide(panel: View, slideOffset: Float) {
//                Log.i("", "onPanelSlide, offset $slideOffset")
//            }
//
//            override fun onPanelStateChanged(
//                panel: View,
//                previousState: PanelState,
//                newState: PanelState
//            ) {
//                Log.i("", "onPanelStateChanged $newState")
//            }
//        })

    }

    fun getImagesPath(activity: Activity): ArrayList<String?> {
        val listOfAllImages = ArrayList<String?>()
        val cursor: Cursor?
        val column_index_folder_name: Int
        var PathOfImage: String? = null
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        cursor = activity.contentResolver.query(
            uri, projection, null,
            null, null
        )
        val column_index_data: Int = cursor!!.getColumnIndexOrThrow(MediaColumns.DATA)
        column_index_folder_name = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(PathOfImage)
        }
        cursor.close()
        return listOfAllImages
    }


}
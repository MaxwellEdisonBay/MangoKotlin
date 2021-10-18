package com.mangoapp.content_loaders

import android.os.Bundle
import com.mangoapp.R

import android.widget.RelativeLayout

import android.app.Activity
import android.content.Intent
import android.view.View

import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import android.provider.MediaStore

import android.provider.MediaStore.MediaColumns

import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.mangoapp.statics.Constants.ImagePopUp.DYNAMIC_LOAD_BUNCH_SIZE
import com.mangoapp.statics.Constants.ImagePopUp.INITIAL_LOAD_BUNCH_SIZE
import com.mangoapp.statics.Constants.ImagePopUp.MAX_ROW_ELEMENTS


class ImagePickerPopUp : Activity() {


    private var listOfAllImages = ArrayList<String?>()
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private var panel : SlidingUpPanelLayout? = null
    private var selectedUris = ArrayList<String?>()
    private var maxImagesNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker_pop_up)

        maxImagesNumber = intent.extras!!.getInt("imagesNumber")
        val exitField = findViewById<RelativeLayout>(R.id.image_picker_popup_exit_field)
        panel = findViewById(R.id.image_picker_popup_sliding_layout)
        val recyclerView = findViewById<RecyclerView>(R.id.image_picker_popup_recycler_view)
        val addContentButton = findViewById<ImageButton>(R.id.activity_image_picker_add_content_button)
        recyclerView.adapter = adapter

        var layoutManager: GridLayoutManager
        var lastVisiblePosition: Int

        if (Build.VERSION.SDK_INT >= 23)
            recyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
                layoutManager = recyclerView.layoutManager as GridLayoutManager
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (lastVisiblePosition == adapter.itemCount - 9) {
                    Log.d("LASTPOS", "LAST POSITION IS VISIBLE NEW VERSION")
                    recyclerView.post {
                        loadBunchOfImages(DYNAMIC_LOAD_BUNCH_SIZE)
                    }
                }
            }
        else
            recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    layoutManager = recyclerView.layoutManager as GridLayoutManager
                    lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                    if (lastVisiblePosition == adapter.itemCount - (2* MAX_ROW_ELEMENTS+1)) {
                        Log.d("LASTPOS", "LAST POSITION IS VISIBLE NEW VERSION")
                        recyclerView.post {
                            loadBunchOfImages(DYNAMIC_LOAD_BUNCH_SIZE)
                        }
                    }
                }

            })

        adapter.setOnItemClickListener { item: Item<GroupieViewHolder>, view: View ->
            val selectedFlag = view.findViewById<ImageView>(R.id.image_preview_card_selected_number)
            val card = item as GalleryAdapter
            val uri = card.imagePreview.uri.toString()
            if (uri !in selectedUris){
                if (selectedUris.size != maxImagesNumber) {
                    selectedUris.add(card.imagePreview.uri.toString())
                    selectedFlag.visibility = View.VISIBLE
                    addContentButton.visibility = View.VISIBLE
                }
            } else {
                selectedUris.remove(card.imagePreview.uri.toString())
                selectedFlag.visibility = View.INVISIBLE
                if (selectedUris.isEmpty())
                    addContentButton.visibility = View.INVISIBLE
            }
            Log.d("SELECTION", selectedUris.toString())
        }

        exitField.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
        getImagesPath()
        loadBunchOfImages(INITIAL_LOAD_BUNCH_SIZE)

        addContentButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra("selectedUris", selectedUris)
            setResult(RESULT_OK, intent)
            finish()
        }

    }

    fun loadBunchOfImages(number: Int) {
        val num = 0
        val initialImageList = listOfAllImages.take(number)
//        Log.d("BUNCH LIST", initialImageList.toString())
//        Log.d("LENS", "Bunch ARr %d ".format(initialImageList.size))
        if (number <= listOfAllImages.size)
            listOfAllImages = listOfAllImages.drop(number) as ArrayList<String?>
        for (i in initialImageList) {
//            Log.d("URI", Uri.parse(i!!).toString())
            adapter.add(GalleryAdapter(ImagePreview(Uri.parse(i), num),selectedUris))
        }
//        Log.d("LST", initialImageList.toString())

    }

    private fun getImagesPath() {
        val cursor: Cursor?
        var pathOfImage: String?
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        cursor = this.contentResolver.query(
            uri, projection, null,
            null, null
        )
        val columnIndexData: Int = cursor!!.getColumnIndexOrThrow(MediaColumns.DATA)
        cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            pathOfImage = cursor.getString(columnIndexData)
            listOfAllImages.add(pathOfImage)
        }
        listOfAllImages.reverse()
        cursor.close()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(100, 100)
    }
}
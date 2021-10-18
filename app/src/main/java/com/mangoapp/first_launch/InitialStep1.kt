package com.mangoapp.first_launch

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mangoapp.R
import com.mangoapp.expands.AddPhotoAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

import com.mangoapp.content_loaders.ImagePickerPopUp
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.ItemTouchHelper.*
import com.mangoapp.statics.Constants.ImagePopUp.MAX_SELECTED_IMAGES


interface ClickListener {
    fun onPositionClicked(position: Int, itemId: Int)
    fun onLongClicked(position: Int, itemId: Int)
}

class InitialStep1 : AppCompatActivity() {

    private var selectedPhotoUris: ArrayList<String>? = null
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private var position = 0
    private var clickListener: ClickListener? = null
    private var maxReached = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_step1)
        val recyclerView = findViewById<RecyclerView>(R.id.init_add_photos_recyclerview_step1)
        recyclerView.adapter = adapter
        checkPermission()


        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    selectedPhotoUris = result.data!!.getStringArrayListExtra("selectedUris")
                    Log.d("ACTIVITY RESULT", selectedPhotoUris.toString())
                    addPhoto()
                }
            }


        val clickListener = object : ClickListener {
            override fun onPositionClicked(position: Int, itemId: Int) {
                clickListener = this
                this@InitialStep1.position = position
                val item = adapter.getItem(position)
                val card = item as AddPhotoAdapter
                val imageView = findViewById<ImageView>(R.id.image_uploader_card_imageview)
                Log.d("POSITION", "POS = $position")
                if (itemId == imageView.id) {
                    if (card.uri == null) {
                        val intent = Intent(this@InitialStep1, ImagePickerPopUp::class.java)
                        intent.putExtra("imagesNumber", MAX_SELECTED_IMAGES - adapter.itemCount + 1)
                        resultLauncher.launch(intent)
                    }
                } else {
                    if (maxReached) {
                        adapter.removeGroupAtAdapterPosition(position)
                        adapter.notifyItemRemoved(position)
                        adapter.notifyItemRangeChanged(position, adapter.itemCount)
                        adapter.add(AddPhotoAdapter(null, this))
                        maxReached = false
                    } else {
                        adapter.removeGroupAtAdapterPosition(position)
                        adapter.notifyItemRemoved(position)
                        adapter.notifyItemRangeChanged(position, adapter.itemCount)
                    }
                }
            }

            override fun onLongClicked(position: Int, itemId: Int) {
            }
        }
        adapter.add(AddPhotoAdapter(null, clickListener))


    }


    private fun requestPermission(permissionName: String, permissionRequestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permissionName), permissionRequestCode)
    }

    private fun addPhoto() {
        adapter.removeGroupAtAdapterPosition(position)
        for (i in selectedPhotoUris!!) {
            Log.d("test", "do some stuff")

            adapter.add(AddPhotoAdapter(Uri.parse(i), clickListener!!))

        }
        if (adapter.itemCount != MAX_SELECTED_IMAGES)
            adapter.add(AddPhotoAdapter(null, clickListener!!))
        else
            maxReached = true
    }

    private fun checkPermission() {
        val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
            }

            else -> {
                requestPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            REQUEST_PERMISSION_READ_EXTERNAL_STORAGE
        )
            }
        }
    }



}
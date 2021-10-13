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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mangoapp.R
import com.mangoapp.expands.AddPhotoAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

import com.mangoapp.content_loaders.ImagePickerPopUp
import android.R.string.no
import androidx.core.app.ActivityCompat


interface ClickListener {
    fun onPositionClicked(position: Int, itemId: Int)
    fun onLongClicked(position: Int, itemId: Int)
}

//class InitialStep1 : AppCompatActivity(), ImageStream.Listener {
class InitialStep1 : AppCompatActivity() {

    private var selectedPhotoUri: Uri? = null
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private var position = 0
    private var clickListener: ClickListener? = null
    private var maxReached = false
    private var maxElements = 6
    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null
//    private var imageStream: ImageStream? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_step1)
        val recyclerView = findViewById<RecyclerView>(R.id.init_add_photos_recyclerview_step1)
        recyclerView.adapter = adapter
        val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1
        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }
//        imageStream = BelvedereUi.install(this)
//        imageStream!!.addListener(this)

//        val addPhotoUri = Uri.parse("https://img2.pngio.com/add-vector-icon-add-addation-plus-png-and-vector-with-add-icon-png-1024_1024.png")
//        adapter.add(AddPhotoAdapter(addPhotoUri))

//        val btn = findViewById<Button>(R.id.init_button_select_gender_step1)
//
//        val uri = Uri.parse("https://s.ws.pho.to/img/index/ai/source.jpg")


        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data!=null) {

                    if (result.data?.clipData != null) {
                        val count = result.data!!.clipData!!.itemCount

                        for (i in 0 until count) {
                            selectedPhotoUri = result.data!!.clipData!!.getItemAt(i).uri
                            Log.d("URI", selectedPhotoUri.toString())
                            //     iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews
                        }

                    } else if (result.data?.data != null) {
                        // if single image is selected

                        selectedPhotoUri = result.data!!.data!!
                        addPhoto()
                        //                    val data: Intent? = result.data
//                    selectedPhotoUri = data?.data
                        Log.d("test", "result func")

                    }


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
                        Log.d("RECYCLERVIEW", "ADD NEW IMAGE $maxReached")
                        val intent = Intent(this@InitialStep1,ImagePickerPopUp::class.java)
                        startActivity(intent)
//                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
////                        intent.addCategory(Intent.CATEGORY_OPENABLE)
////                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        intent.action= Intent.ACTION_GET_CONTENT
////                        val intent = Intent(Intent.ACTION_PICK)
//                        intent.type = "image/*"

//                        imageStream(applicationContext)
//                            .withCameraIntent()
//                            .withDocumentIntent("*/*", true)
//                            .showPopup(this@InitialStep1)

//                        resultLauncher.launch(intent)


                    } else
                        Log.d("RECYCLERVIEW", "OLD ADDED IMAGE $maxReached")
                } else {
                    if (maxReached) {
                        Log.d("TOP CLICKER", "CLOSE $maxReached")
                        adapter.removeGroupAtAdapterPosition(position)
                        adapter.notifyItemRemoved(position)
                        adapter.notifyItemRangeChanged(position, adapter.itemCount)
                        adapter.add(AddPhotoAdapter(null, this))
                        maxReached = false
                    } else {
                        adapter.removeGroupAtAdapterPosition(position)
                        adapter.notifyItemRemoved(position)
                        adapter.notifyItemRangeChanged(position, adapter.itemCount)
                        Log.d("TOP CLICKER", "CLOSE NOT LAST $maxReached")
                    }
                }
            }

            override fun onLongClicked(position: Int, itemId: Int) {
//                            TODO("Not yet implemented")
            }
        }
        adapter.add(AddPhotoAdapter(null, clickListener))


    }


    private fun requestPermission(permissionName: String, permissionRequestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permissionName), permissionRequestCode)
    }

    private fun addPhoto(){
        if (!maxReached) {
            Log.d("test", "do some stuff")
            adapter.removeGroupAtAdapterPosition(position)
            adapter.add(AddPhotoAdapter(selectedPhotoUri, clickListener!!))
            if (adapter.itemCount != maxElements)
                adapter.add(AddPhotoAdapter(null, clickListener!!))
            else
                maxReached = true
        } else
            Log.d("BTN", "NO MORE PHOTOS")
    }

    fun check_permission (){
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }
//            shouldShowRequestPermissionRationale(...) -> {
//            // In an educational UI, explain to the user why your app requires this
//            // permission for a specific feature to behave as expected. In this UI,
//            // include a "cancel" or "no thanks" button that allows the user to
//            // continue using your app without granting the permission.
//            showInContextUI(...)
//        }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher?.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
//    override fun onDismissed() {
//    }
//
//    override fun onVisible() {
//    }
//
//    override fun onMediaSelected(mediaResults: MutableList<MediaResult>?) {
//    }
//
//    override fun onMediaDeselected(mediaResults: MutableList<MediaResult>?) {
//    }



}
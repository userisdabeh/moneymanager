package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PhotoAdapter(val photos: MutableList<Uri>, private val addPhotoClick: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_PHOTO = 0
        private const val VIEW_TYPE_ADD_PHOTO = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == photos.size) VIEW_TYPE_ADD_PHOTO else VIEW_TYPE_PHOTO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ADD_PHOTO) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_photo, parent, false)
            AddPhotoViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
            PhotoViewHolder(view)
        }
    }

    override fun getItemCount(): Int = photos.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AddPhotoViewHolder) {
            holder.button.setOnClickListener {
                addPhotoClick()
            }
        } else if (holder is PhotoViewHolder) {
            holder.imageView.setImageURI(photos[position])
        }
    }

    fun addPhoto(uri: Uri) {
        photos.add(uri)
        notifyItemInserted(photos.size - 1)
    }

    class AddPhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button : ImageButton = view.findViewById(R.id.addPhotoButton)
    }

    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView : ImageView = view.findViewById(R.id.photoImageView)
    }
}
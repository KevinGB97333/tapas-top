package es.upm.isii.tapastop.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageBitmap")
fun bindImage(imageView: ImageView, imgBitmap: Bitmap) {
	imageView.setImageBitmap(imgBitmap)
}
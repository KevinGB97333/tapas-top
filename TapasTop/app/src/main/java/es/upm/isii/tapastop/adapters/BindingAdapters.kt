package es.upm.isii.tapastop.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import es.upm.isii.tapastop.R

@BindingAdapter(value = ["imageBitmap","placeholder"], requireAll = false)
fun bindImage(imageView: ImageView, imgBitmap: Bitmap?,placeholder: Drawable) {
	if(imgBitmap == null){
		imageView.setImageDrawable(placeholder)
	}else{
		imageView.setImageBitmap(imgBitmap)
	}
}
package es.upm.isii.tapastop.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.BindingAdapter
import androidx.navigation.fragment.findNavController
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.model.restApiStatus

@BindingAdapter(value = ["imageBitmap","placeholder"], requireAll = false)
fun bindImage(imageView: ImageView, imgBitmap: Bitmap?,placeholder: Drawable) {
	if(imgBitmap == null){
		imageView.setImageDrawable(placeholder)
	}else{
		imageView.setImageBitmap(imgBitmap)
	}
}
@BindingAdapter("restApiStatus")
fun bindStatus(statusProgress :RelativeLayout, status : restApiStatus?){
	when(status){
			restApiStatus.LOADING -> statusProgress.visibility = View.VISIBLE
			else -> statusProgress.visibility = View.GONE
	}
}
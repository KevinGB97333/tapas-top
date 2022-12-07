package es.upm.isii.tapastop.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import es.upm.isii.tapastop.network.Users

/**
 * Bind [Bitmap] to [imageView]
 */
@BindingAdapter(value = ["imageBitmap", "placeholder"], requireAll = false)
fun bindImage(imageView: ImageView, imgBitmap: Bitmap?, placeholder: Drawable) {
	if (imgBitmap == null) {
		imageView.setImageDrawable(placeholder)
	} else {
		imageView.setImageBitmap(imgBitmap)
	}
}

/**
 * Number of elements in [data]
 * Used for friends request list and friends list
 */
@BindingAdapter("numberOf")
fun bindSize(textView: TextView, data: Users?) {
	if (data?.users.isNullOrEmpty()) {
		textView.text = "0"
	} else {
		textView.text = data?.users?.size.toString()
	}

}

/**
 * Used to make [Button] invisible
 */
@BindingAdapter("requested")
fun bindVisibility(button: Button, already: Boolean) {
	if (already) {
		button.visibility = View.GONE
	} else {
		button.visibility = View.VISIBLE
	}
}

/**
 * Updates the data shown in the [RecyclerView].
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: Users?) {
	val adapter = recyclerView.adapter as UsersListAdapter
	adapter.submitList(data?.users)
}

/**
 * Updates the data shown in the [RecyclerView].
 */
@BindingAdapter("requestsData")
fun bindFriendRequests(recyclerView: RecyclerView, data: Users?) {
	val adapter = recyclerView.adapter as FriendRequestListAdapter
	adapter.submitList(data?.users)
}
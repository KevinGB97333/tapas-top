package es.upm.isii.tapastop.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.ETC1.decodeImage
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.databinding.FriendRequestItemBinding
import es.upm.isii.tapastop.model.UserViewModel
import es.upm.isii.tapastop.model.restApiStatus
import es.upm.isii.tapastop.network.UserSummary

class FriendRequestListAdapter(private val sharedViewModel: UserViewModel,
							   private val lifecycleOwner: LifecycleOwner,
							   private val context : Context,
							   private val errorMsg : String):
	ListAdapter<UserSummary, FriendRequestListAdapter.FriendRequestViewHolder>(DiffCallback) {

	/**
	 * Allows the RecyclerView to determine which items have changed when the [List] of
	 * [UserSummary] has been updated.
	 */
	companion object DiffCallback : DiffUtil.ItemCallback<UserSummary>() {
		override fun areItemsTheSame(oldItem: UserSummary, newItem: UserSummary): Boolean {
			return oldItem.username == newItem.username
		}

		override fun areContentsTheSame(oldItem: UserSummary, newItem: UserSummary): Boolean {
			return oldItem.username == newItem.username
		}
	}
	class FriendRequestViewHolder(
		private var binding: FriendRequestItemBinding
	) : RecyclerView.ViewHolder(binding.root) {
		fun bind(user: UserSummary) {
			binding.userUsername.text = user.username
			binding.userProfileImg.setImageBitmap(decodeImage(user.profile_img))
			binding.executePendingBindings()
		}

	}


	/**
	 * Create new [RecyclerView] item views (invoked by the layout manager)
	 */
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): FriendRequestListAdapter.FriendRequestViewHolder {
		return FriendRequestListAdapter.FriendRequestViewHolder(
			FriendRequestItemBinding.inflate(LayoutInflater.from(parent.context))
		)
	}

	/**
	 * Replaces the contents of a view (invoked by the layout manager)
	 */
	override fun onBindViewHolder(
		holder: FriendRequestListAdapter.FriendRequestViewHolder,
		position: Int
	) {
		val user = getItem(position)
		holder.bind(user)
		val loadingLayout = holder.itemView.findViewById<RelativeLayout>(R.id.loading_layout)
		sharedViewModel.status.observe(lifecycleOwner){
			when(it){
				restApiStatus.LOADING -> loadingLayout.visibility = View.VISIBLE
				restApiStatus.ERROR -> {
					Toast.makeText(context,errorMsg,Toast.LENGTH_SHORT).show()
					loadingLayout.visibility = View.GONE
				}
				else -> {
					loadingLayout.visibility = View.GONE
				}
			}
		}
		holder.itemView.findViewById<Button>(R.id.accept_request).setOnClickListener {
			sharedViewModel.acceptRequest(user.username, position)
		}
		holder.itemView.findViewById<Button>(R.id.decline_request).setOnClickListener {
			sharedViewModel.declineRequest(user.username, position)
		}
	}

}

private fun decodeImage(imageB64: String?): Bitmap {
	val imageBytes = Base64.decode(imageB64, Base64.DEFAULT)
	return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}
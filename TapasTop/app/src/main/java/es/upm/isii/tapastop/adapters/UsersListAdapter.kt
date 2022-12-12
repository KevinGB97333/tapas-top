package es.upm.isii.tapastop.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.upm.isii.tapastop.databinding.UserViewItemBinding
import es.upm.isii.tapastop.model.UserViewModel
import es.upm.isii.tapastop.network.UserSummary

class UsersListAdapter(private var sharedViewModel: UserViewModel) :
	ListAdapter<UserSummary, UsersListAdapter.UsersViewHolder>(DiffCallback) {

	class UsersViewHolder(
		private var binding: UserViewItemBinding
	) : RecyclerView.ViewHolder(binding.root) {
		fun bind(user: UserSummary) {
			binding.userUsername.text = user.username
			binding.userProfileImg.setImageBitmap(decodeImage(user.profile_img))
			binding.executePendingBindings()
		}
	}

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

	/**
	 * Create new [RecyclerView] item views (invoked by the layout manager)
	 */
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): UsersListAdapter.UsersViewHolder {
		return UsersListAdapter.UsersViewHolder(
			UserViewItemBinding.inflate(LayoutInflater.from(parent.context))
		)
	}

	/**
	 * Replaces the contents of a view (invoked by the layout manager)
	 */
	override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
		val user = getItem(position)
		holder.bind(user)
		holder.itemView.setOnClickListener {
			sharedViewModel.getSpecificUser(user.username)
		}
	}
}

private fun decodeImage(imageB64: String?): Bitmap {
	val imageBytes = Base64.decode(imageB64, Base64.DEFAULT)
	return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}
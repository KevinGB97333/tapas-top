package es.upm.isii.tapastop.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import es.upm.isii.tapastop.databinding.TapaViewItemBinding
import es.upm.isii.tapastop.databinding.UserViewItemBinding
import es.upm.isii.tapastop.model.TapaViewModel
import es.upm.isii.tapastop.model.UserViewModel
import es.upm.isii.tapastop.network.TapaSummary

class TapaListAdapter( private val tapaSharedViewModel: TapaViewModel, private val userSharedViewModel: UserViewModel) : ListAdapter<TapaSummary,TapaListAdapter.TapaViewHolder>(DiffCallback){

	class TapaViewHolder(
		private var binding : TapaViewItemBinding
	) : RecyclerView.ViewHolder(binding.root){
		fun bind(tapa : TapaSummary){
			binding.tapaName.text = tapa.name
			binding.tapaImg.setImageBitmap(decodeImage(tapa.photo))
			binding.executePendingBindings()
		}
	}

	/**
	 * Allows the RecyclerView to determine which items have changed when the [List] of
	 * [TapaSummary] has been updated.
	 */
	companion object DiffCallback : DiffUtil.ItemCallback<TapaSummary>() {
		override fun areItemsTheSame(oldItem: TapaSummary, newItem: TapaSummary): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: TapaSummary, newItem: TapaSummary): Boolean {
			return oldItem.id == newItem.id
		}
	}

	/**
	 * Create new [RecyclerView] item views (invoked by the layout manager)
	 */
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): TapaListAdapter.TapaViewHolder {
		return TapaListAdapter.TapaViewHolder(
			TapaViewItemBinding.inflate(LayoutInflater.from(parent.context))
		)
	}

	/**
	 * Replaces the contents of a view (invoked by the layout manager)
	 */
	override fun onBindViewHolder(holder: TapaListAdapter.TapaViewHolder, position: Int) {
		val tapa = getItem(position)
		holder.bind(tapa)
		holder.itemView.setOnClickListener {
			tapaSharedViewModel.getSpecificTapa(tapa.id,userSharedViewModel.currentUser.value!!.username)
		}
	}

}
private fun decodeImage(imageB64: String?): Bitmap {
	val imageBytes = Base64.decode(imageB64, Base64.DEFAULT)
	return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}
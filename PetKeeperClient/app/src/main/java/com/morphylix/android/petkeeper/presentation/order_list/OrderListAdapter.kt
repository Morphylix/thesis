package com.morphylix.android.petkeeper.presentation.order_list

import android.content.res.Resources.Theme
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.morphylix.android.petkeeper.R
import com.morphylix.android.petkeeper.databinding.OrderListItemBinding
import com.morphylix.android.petkeeper.domain.model.domain.Order
import com.morphylix.android.petkeeper.presentation.user_profile.UserProfileFragmentDirections
import com.morphylix.android.petkeeper.util.formatDate

private const val TAG = "OrderListAdapter"

class OrderListAdapter : ListAdapter<Order, OrderViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding =
            OrderListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

}

class OrderViewHolder(private val binding: OrderListItemBinding) :
    RecyclerView.ViewHolder(binding.root), OnClickListener {

    private lateinit var order: Order

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(order: Order) {
        Log.i(TAG, "binding data... user rating is ${order.user?.rating}")
        this.order = order
        with(binding) {
            petNameTextView.text = order.pet?.name
            petTypeTextView.text = order.pet?.type
            orderDateTextView.text =
                formatDate(order.createDate) // TODO: Replace with SimpleDateFormat
            Log.i(TAG, "user from order is ${order.user}")
            userNameTextView.text = order.user?.name
            userRatingTextView.text = binding.root.context.getString(
                R.string.user_rating_votes,
                order.user?.rating,
                order.user?.totalVotes
            )
            val byteArray = order.pet?.picture
            if (byteArray != null) {
                Log.i(TAG, "creating image... for pet id ${order.pet?.id}")
                val image = BitmapDrawable(
                    binding.root.resources,
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                )
                Log.i(TAG, "pet img is ${order.pet?.picture}")
                Log.i(TAG, "BITMAP IS $image")
                petPicImageView.setImageDrawable(image)
            } else petPicImageView.setImageDrawable(binding.root.context.getDrawable(R.drawable.pet_pic_placeholder))
            // Download pet pic
        }
    }

    override fun onClick(p0: View?) {
        val action =
            OrderListFragmentDirections.actionOrderListFragmentToOrderDetailsFragment(order.orderId)
        try {
            binding.root.findNavController().navigate(action)
        } catch (e: java.lang.IllegalArgumentException) {
            val action2 = UserProfileFragmentDirections.actionUserProfileFragmentToOrderDetailsFragment(order.orderId)
            binding.root.findNavController().navigate(action2)
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }
}
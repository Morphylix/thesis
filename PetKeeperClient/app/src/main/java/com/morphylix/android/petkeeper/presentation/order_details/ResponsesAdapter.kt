package com.morphylix.android.petkeeper.presentation.order_details

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.morphylix.android.petkeeper.databinding.ResponseItemBinding
import com.morphylix.android.petkeeper.domain.model.domain.Response

private const val TAG = "ResponsesAdapter"

class ResponsesAdapter(private val responseList: List<Response>, private val responseClickListener: ResponseClickListener): RecyclerView.Adapter<ResponsesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponsesViewHolder {
        val binding = ResponseItemBinding.inflate(LayoutInflater.from(parent.context))
        return ResponsesViewHolder(binding, responseClickListener)
    }

    override fun getItemCount(): Int {
        return responseList.size
    }

    override fun onBindViewHolder(holder: ResponsesViewHolder, position: Int) {
        holder.bind(responseList[position])
    }
}


class ResponsesViewHolder(private val binding: ResponseItemBinding, private val responseClickListener: ResponseClickListener): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private var response: Response? = null
    init {
        itemView.setOnClickListener(this)
    }

    fun bind(response: Response) {
        this.response = response
        with(binding) {
            userNameTextView.text = response.userEmail
            responseBodyTextView.text = response.body
        }
    }

    override fun onClick(p0: View?) {
        responseClickListener.onResponseClicked(response!!)
    }

}

interface ResponseClickListener {
    fun onResponseClicked(response: Response)
}
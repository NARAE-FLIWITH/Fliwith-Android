package com.narae.fliwith.src.main.review

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.narae.fliwith.databinding.ItemSpotNameBinding
import com.narae.fliwith.src.main.review.models.ReviewSpotName

class ReviewSpotNameAdapter(context: Context, private var spotNameDataList: List<ReviewSpotName>) : RecyclerView.Adapter<ReviewSpotNameAdapter.ReviewSpotNameHolder>() {

    inner class ReviewSpotNameHolder(private val binding: ItemSpotNameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindInfo(spotName: ReviewSpotName) {
            binding.apply {

                binding.spotName.text = spotName.name

                itemView.setOnClickListener {
                    itemClickListener.onClick(it, layoutPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewSpotNameHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSpotNameBinding.inflate(inflater, parent, false)
        return ReviewSpotNameHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewSpotNameHolder, position: Int) {
        holder.bindInfo(spotNameDataList[position])
    }

    fun updateData(newList: List<ReviewSpotName>) {
        spotNameDataList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = spotNameDataList.size

    // Item click listener interface
    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    // Click listener declaration
    private lateinit var itemClickListener: ItemClickListener

    // Click listener registration method
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}
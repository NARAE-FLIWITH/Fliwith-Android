package com.narae.fliwith.src.main.review

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.narae.fliwith.R
import com.narae.fliwith.databinding.ItemReviewDetailImageBinding

class ReviewSliderAdapter(
    private val context: Context,
    private val reviewDetailImageList: MutableList<String>
) : RecyclerView.Adapter<ReviewSliderAdapter.MyViewHolder>() {

    private lateinit var binding: ItemReviewDetailImageBinding
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mImageView: ImageView = itemView.findViewById(R.id.review_detail_slider_image_item)
        fun bindInfo(imageURL: String) {
            Glide.with(context)
                .load(imageURL)
                .into(mImageView)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemReviewDetailImageBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {
            bindInfo(reviewDetailImageList[position])
        }
    }

    override fun getItemCount(): Int {
        return reviewDetailImageList.size
    }

    fun setImages(newImageUrls: List<String>) {
        reviewDetailImageList.clear()
        reviewDetailImageList.addAll(newImageUrls)
        notifyDataSetChanged()
    }

}



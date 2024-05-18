package com.narae.fliwith.src.main.review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.narae.fliwith.R
import com.narae.fliwith.databinding.ItemReviewBinding
import com.narae.fliwith.src.main.review.models.Review

class ReviewAdapter(private val reviewDataList: MutableList<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewHolder>(){

    private lateinit var binding: ItemReviewBinding
    inner class ReviewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindInfo(review: Review) {
            binding.apply {
                reviewDefaultImage.setImageResource(R.drawable.review_default_image)
                reviewProfileImage.setImageResource(R.drawable.profile_image)
                reviewUserName.text = review.userName
                reviewHeartCount.text = review.heartCount.toString()
                reviewDefaultImage.clipToOutline = true
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.ReviewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemReviewBinding.inflate(inflater, parent, false)
        return ReviewHolder(binding.root)
    }

    // View Holder에 실제 데이터 binding
    override fun onBindViewHolder(holder: ReviewAdapter.ReviewHolder, position: Int) {
        holder.apply {
            bindInfo(reviewDataList[position])
        }
    }

    override fun getItemCount(): Int = reviewDataList.size

}
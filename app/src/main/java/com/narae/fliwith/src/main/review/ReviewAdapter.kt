package com.narae.fliwith.src.main.review

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.narae.fliwith.R
import com.narae.fliwith.databinding.ItemReviewBinding
import com.narae.fliwith.src.main.review.models.Review
import com.narae.fliwith.util.userProfileImageConvert

class ReviewAdapter(context: Context, private var reviewDataList: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewHolder>() {

    inner class ReviewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindInfo(review: Review) {
            binding.apply {
//                reviewDefaultImage.setImageResource(R.drawable.store_default_image)
                Glide.with(binding.root)
                    .load(review.image)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.no_image)
                    .into(reviewDefaultImage)

                // HEARING, VISUAL, PHYSICAL, NONDISABLED, NONE, NOTSELECTED
                // 장애 별로 프로필 색상 변경
                userProfileImageConvert(review.disability, reviewProfileImage)

                reviewUserName.text = review.nickname
                reviewHeartCount.text = review.likes.toString()
                reviewDefaultImage.clipToOutline = true
                itemView.setOnClickListener {
                    itemClickListener.onClick(it, layoutPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewBinding.inflate(inflater, parent, false)
        return ReviewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.bindInfo(reviewDataList[position])
    }

    override fun getItemCount(): Int = reviewDataList.size

    fun updateReviews(newReviews: List<Review>) {
        reviewDataList = newReviews
        notifyDataSetChanged()
    }

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

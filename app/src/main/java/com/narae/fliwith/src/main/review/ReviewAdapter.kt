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
                itemView.setOnClickListener{
                    itemClickListner.onClick(it, layoutPosition)
                }
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

    // item click listener
    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View,  position: Int)
    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener

    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

    override fun getItemCount(): Int = reviewDataList.size

}
package com.narae.fliwith.src.main.recommend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.narae.fliwith.R
import com.narae.fliwith.databinding.ItemViewPagerReviewBinding
import com.narae.fliwith.src.main.recommend.models.ViewPagerReview

class ViewPagerReviewAdapter (private val viewPagerReviewDataList: MutableList<ViewPagerReview>) : RecyclerView.Adapter<ViewPagerReviewAdapter.ViewPagerReviewHolder>(){

    private lateinit var binding: ItemViewPagerReviewBinding
    inner class ViewPagerReviewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindInfo(review: ViewPagerReview) {
            binding.apply {
                viewPagerReviewTv.text = review.content
                viewPagerReviewIv.setImageResource(R.drawable.view_pager_review_img)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerReviewAdapter.ViewPagerReviewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemViewPagerReviewBinding.inflate(inflater, parent, false)
        return ViewPagerReviewHolder(binding.root)
    }

    // View Holder에 실제 데이터 binding
    override fun onBindViewHolder(holder: ViewPagerReviewAdapter.ViewPagerReviewHolder, position: Int) {
        holder.apply {
            bindInfo(viewPagerReviewDataList[position])
        }
    }

    override fun getItemCount(): Int = viewPagerReviewDataList.size

}
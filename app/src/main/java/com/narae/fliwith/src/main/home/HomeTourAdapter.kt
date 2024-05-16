package com.narae.fliwith.src.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.narae.fliwith.R
import com.narae.fliwith.databinding.TourItemBinding
import com.narae.fliwith.src.main.home.models.Tour

class HomeTourAdapter (private val tourDataList: MutableList<Tour>) : RecyclerView.Adapter<HomeTourAdapter.TourHolder>(){

    private lateinit var binding: TourItemBinding
    inner class TourHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindInfo(tour: Tour) {
            binding.apply {
                tourImg.setImageResource(R.drawable.tour_default_image)
                tourGo.text =">"
                tourName.text = "관광지 이름"
                tourImg.clipToOutline = true
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTourAdapter.TourHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = TourItemBinding.inflate(inflater, parent, false)
        return TourHolder(binding.root)
    }

    // View Holder에 실제 데이터 binding
    override fun onBindViewHolder(holder: HomeTourAdapter.TourHolder, position: Int) {
        holder.apply {
            bindInfo(tourDataList[position])
        }
    }

    override fun getItemCount(): Int = tourDataList.size

}
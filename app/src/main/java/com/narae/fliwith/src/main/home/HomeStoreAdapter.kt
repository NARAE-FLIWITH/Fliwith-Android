package com.narae.fliwith.src.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.narae.fliwith.R
import com.narae.fliwith.databinding.StoreItemBinding
import com.narae.fliwith.src.main.home.models.Store

class HomeStoreAdapter(private val storeDataList: MutableList<Store>) :
    RecyclerView.Adapter<HomeStoreAdapter.StoreHolder>() {

    private lateinit var binding: StoreItemBinding

    inner class StoreHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindInfo(store: Store) {
            binding.apply {
                if (store.imageUrl == -1) {
                    storeImg.setImageResource(R.drawable.store_default_image)
                } else {
                    storeImg.setImageResource(store.imageUrl)
                }
                storeName.text = store.storeName
                storeImg.clipToOutline = true
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeStoreAdapter.StoreHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = StoreItemBinding.inflate(inflater, parent, false)
        return StoreHolder(binding.root)
    }

    // View Holder에 실제 데이터 binding
    override fun onBindViewHolder(holder: HomeStoreAdapter.StoreHolder, position: Int) {
        holder.apply {
            bindInfo(storeDataList[position])
        }
    }

    override fun getItemCount(): Int = storeDataList.size

}
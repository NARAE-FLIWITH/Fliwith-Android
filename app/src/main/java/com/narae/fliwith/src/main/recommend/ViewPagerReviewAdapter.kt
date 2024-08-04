import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.narae.fliwith.R
import com.narae.fliwith.databinding.ItemViewPagerReviewBinding
import com.narae.fliwith.src.main.recommend.models.ViewPagerReview

class ViewPagerReviewAdapter(private val viewPagerReviewDataList: MutableList<ViewPagerReview>) : RecyclerView.Adapter<ViewPagerReviewAdapter.ViewPagerReviewHolder>() {

    inner class ViewPagerReviewHolder(private val binding: ItemViewPagerReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindInfo(review: ViewPagerReview) {
            binding.apply {
                viewPagerReviewTv.text = review.content
                if (review.imgUrl != null) {
                    Glide.with(viewPagerReviewIv.context)
                        .load(review.imgUrl)
                        .override(70,70)
                        .centerCrop()
                        .into(viewPagerReviewIv)
                    binding.viewPagerReviewIv.clipToOutline = true
                } else {
                    viewPagerReviewIv.setImageResource(R.drawable.no_image) // 기본 이미지 설정
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerReviewAdapter.ViewPagerReviewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemViewPagerReviewBinding.inflate(inflater, parent, false)
        return ViewPagerReviewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerReviewAdapter.ViewPagerReviewHolder, position: Int) {
        holder.bindInfo(viewPagerReviewDataList[position])
    }

    override fun getItemCount(): Int = viewPagerReviewDataList.size

    fun addData(newData: List<ViewPagerReview>) {
        val startPosition = viewPagerReviewDataList.size
        viewPagerReviewDataList.addAll(newData)
        notifyItemRangeInserted(startPosition, newData.size)
    }
}

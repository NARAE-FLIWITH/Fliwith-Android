import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.narae.fliwith.databinding.ItemReviewWriteImageBinding

class ReviewWriteImageAdapter(private val context: Context, private val imageUrls: MutableList<String>) :
    RecyclerView.Adapter<ReviewWriteImageAdapter.ReviewWriteImageHolder>() {

    inner class ReviewWriteImageHolder(private val binding: ItemReviewWriteImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindInfo(imageUrl: String) {
            Glide.with(context)
                .load(imageUrl)
                .into(binding.reviewWriteImageDefault)
            binding.reviewWriteImageDefault.clipToOutline = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewWriteImageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewWriteImageBinding.inflate(inflater, parent, false)
        return ReviewWriteImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewWriteImageHolder, position: Int) {
        holder.bindInfo(imageUrls[position])
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    fun setImages(newImageUrls: List<String>) {
        imageUrls.clear()
        imageUrls.addAll(newImageUrls)
        notifyDataSetChanged()
    }
}

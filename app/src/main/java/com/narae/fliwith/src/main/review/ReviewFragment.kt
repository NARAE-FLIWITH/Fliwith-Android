package com.narae.fliwith.src.main.review

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.narae.fliwith.databinding.FragmentReviewBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.review.models.Review

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReviewFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentReviewBinding? = null
    private val binding
        get() = _binding!!

    private var reviewList = mutableListOf<Review>()

    private lateinit var reviewAdapter : ReviewAdapter

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i in 1..20) {
            reviewList.add(Review(null, userName="유유", heartCount=2))
        }

        reviewAdapter = ReviewAdapter(reviewList)
        binding.reviewRv.adapter = reviewAdapter
        binding.reviewRv.addItemDecoration(
            GridSpacingItemDecoration(spanCount = 2, spacing = 6f.fromDpToPx())
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    internal class GridSpacingItemDecoration(
        private val spanCount: Int, // Grid의 column 수
        private val spacing: Int // 간격
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position: Int = parent.getChildAdapterPosition(view)

            if (position >= 0) {
                val column = position % spanCount // item column
                outRect.apply {
                    // spacing - column * ((1f / spanCount) * spacing)
                    left = spacing - column * spacing / spanCount
                    // (column + 1) * ((1f / spanCount) * spacing)
                    right = (column + 1) * spacing / spanCount
                    if (position < spanCount) top = spacing
                    bottom = spacing
                }
            } else {
                outRect.apply {
                    left = 0
                    right = 0
                    top = 0
                    bottom = 0
                }
            }
        }
    }

    fun Float.fromDpToPx(): Int =
        (this * Resources.getSystem().displayMetrics.density).toInt()
}
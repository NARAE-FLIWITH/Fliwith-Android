package com.narae.fliwith.src.main.review

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.narae.fliwith.R
import com.narae.fliwith.databinding.FragmentReviewWriteBinding
import com.narae.fliwith.src.main.MainActivity


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReviewWriteFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentReviewWriteBinding? = null
    private val binding
        get() = _binding!!

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
        _binding = FragmentReviewWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1 번만 나오고 안나와
        binding.reviewWriteImageFrame.setOnClickListener {
            showPopUp(binding.reviewWriteImageFrame)
        }

        // 이미지 넣어 졌으면 이제는 작은 Frame
        binding.reviewWriteImageFrameSmall.setOnClickListener {
            showPopUp(binding.reviewWriteImageFrameSmall)
        }

    }

    fun showPopUp(view: View) {
        val popupMenu = PopupMenu(requireContext(), view, 0, 0, R.style.CustomPopupMenu)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_review_write_popup, popupMenu.menu)

        try {
            val fields = popupMenu::class.java.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper = field.get(popupMenu)
                    val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                    val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.gallery_menu -> {
                    // 갤러리 선택
                }
                R.id.camera_menu -> {
                    // 사진 찍기
                }
                R.id.file_menu -> {
                    // 파일 탐색
                }
            }
            false
        }
        popupMenu.show()
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReviewWriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
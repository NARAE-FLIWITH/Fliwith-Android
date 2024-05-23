package com.narae.fliwith.src.main.review

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentReviewWriteBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.review.models.ReviewInsertRequest
import com.narae.fliwith.src.main.review.models.ReviewPresignedRequest
import com.narae.fliwith.src.main.review.models.ReviewViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "ReviewWriteFragment_싸피"
class ReviewWriteFragment : BaseFragment<FragmentReviewWriteBinding>(
    FragmentReviewWriteBinding::inflate) {

    private lateinit var mainActivity: MainActivity
    private val viewModel: ReviewViewModel by activityViewModels()

    // 변경된 pickMedia 함수
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // 사진 선택 이후 돌아왔을 때 콜백
        if (uri != null) {
            // 선택된 사진이 있을 경우
            Log.d(TAG, "photoPicker: 사진 선택 했다.")
            binding.reviewWriteImageFrameSmall.visibility = View.VISIBLE
            Glide.with(requireContext()).load(uri).into(binding.reviewWriteImageFrame)
            Log.d(TAG, "이미지 uri: $uri")

            // 선택된 사진의 확장자 가져오기
            val imageExtension = getFileExtension(requireContext(), uri)
            Log.d(TAG, "선택된 확장자: $imageExtension")

            // presigned URL 요청
            viewModel.fetchPresignedReview(ReviewPresignedRequest(imageExtension)) { success, presignedData ->
                if (success && presignedData != null) {
                    // 성공적으로 presigned URL을 받은 경우
                    // 업로드된 이미지의 URL을 ViewModel에 저장
                    // 리뷰 작성시 imageUrl 그대로 사용
                    Log.d(TAG, "presignedData: ${presignedData.imageUrl}")
                    viewModel.setImageUrl(presignedData.imageUrl)
                } else {
                    // presigned URL 요청 실패 또는 응답이 null인 경우
                    // 오류 처리
                    Log.d(TAG, "review write fragment : 갤러리 에서 사진 가져 오기 실패임 실패")
                }
            }
        } else {
            // 선택된 사진이 없을 경우
            binding.reviewWriteImageFrameSmall.visibility = View.GONE
        }
    }

    private fun getFileExtension(context: Context, uri: Uri): String {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType?.substringAfterLast('/') ?: "jpg"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.reviewWriteImageFrameSmall.visibility = View.GONE

        // 1 번만 나오고 안나와
        binding.reviewWriteImageFrame.setOnClickListener {
            showPopUp(binding.reviewWriteImageFrame)
        }

        // 이미지 넣어 졌으면 이제는 작은 Frame
        binding.reviewWriteImageFrameSmall.setOnClickListener {
            showPopUp(binding.reviewWriteImageFrameSmall)
        }

        // 지역 작성 버튼을 누르면
        binding.reviewWriteEtLayout.setOnClickListener {
            // 검색창 화면 으로 이동
            navController.navigate(R.id.action_reviewWriteFragment_to_reviewSpotNameFragment)
        }

        // 내가 선택한 spotName
        binding.reviewWriteEt.setText(viewModel.reviewSpotName.value)

        // EditText에 TextWatcher 설정
        binding.reviewWriteComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.setReviewWriteContent(s.toString())
                binding.reviewWriteCommentTv.text = "${viewModel.reviewWriteContent.value?.length}자 / 최소 20자"
            }
        })

        // ReviewWriteContent 변경 관찰
        viewModel.reviewWriteContent.observe(viewLifecycleOwner) { content ->
            if (content != null && content.length >= 20 &&
                viewModel.reviewSpotName.value != null && viewModel.uploadedImageUrl.value != null) {
                binding.reviewWriteBtn.isEnabled = true
                // 작성
                binding.reviewWriteBtn.setOnClickListener {
                    Log.d(TAG, "onViewCreated: ${viewModel.uploadedImageUrl.value }")
                    // 작성 하고 리뷰 화면 으로 다시 이동
                    val request = ReviewInsertRequest(
                        viewModel.reviewSpotContentId.value!!,
                        content!!,
                        listOf(viewModel.uploadedImageUrl.value!!))
                    postReviewData(request)
                }
            }else {
                binding.reviewWriteBtn.isEnabled = false
            }
        }
        

    }
    
    fun postReviewData(request : ReviewInsertRequest) {
        viewModel.setReviewInsertRequest(request)
        viewModel.fetchInsert(viewModel.reviewInsertRequest.value) { success ->
            if (success) {
                // 작성하고 리뷰 화면으로 다시 이동
                navController.navigate(R.id.action_reviewWriteFragment_to_menu_main_btm_nav_review)
            } else {
                // 에러 처리
                Log.d(TAG, "postReviewData: 리뷰 작성 못해따!")
            }
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
                    photoPicker()
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

    fun photoPicker() {
        // 이미지만
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

}
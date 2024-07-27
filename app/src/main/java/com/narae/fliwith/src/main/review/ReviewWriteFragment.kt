package com.narae.fliwith.src.main.review

import ReviewWriteImageAdapter
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
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentReviewWriteBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.review.models.ReviewInsertRequest
import com.narae.fliwith.src.main.review.models.ReviewPresignedRequest
import com.narae.fliwith.src.main.review.models.ReviewViewModel
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "ReviewWriteFragment_싸피"
class ReviewWriteFragment : BaseFragment<FragmentReviewWriteBinding>(
    FragmentReviewWriteBinding::inflate) {

    private lateinit var mainActivity: MainActivity
    private val viewModel: ReviewViewModel by activityViewModels()
    private var reviewId:Int=-1
    private lateinit var imageAdapter: ReviewWriteImageAdapter
    private val imageUrls: MutableList<String> = mutableListOf()
    private val presignedUrls: MutableList<String> = mutableListOf()
    private val _checkSpotId = MutableLiveData<Boolean>(false)
    private val _checkContentLength = MutableLiveData<Boolean>(false)
    private val _uploadSuccess = MutableLiveData<Boolean>(false)

    private val _isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(_checkContentLength) { checkButtonEnabled() }
        addSource(_checkSpotId) { checkButtonEnabled() }
        addSource(_uploadSuccess) { checkButtonEnabled() }
    }

    // pickMedia 함수
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
        if (uris.isNotEmpty()) {
            // uris 리스트에 값이 있을 경우
            val imageUrls = uris.map { uri -> uri.toString() } // Uri를 String으로 변환
            imageAdapter.setImages(imageUrls)

            // 여러 장의 이미지 업로드 시작
            uploadImagesSequentially(requireContext(), uris) { success ->
                if (success) {
                    Log.d(TAG, "All images uploaded successfully")
                    _uploadSuccess.value  = true
                } else {
                    Log.e(TAG, "Failed to upload images")
                    _uploadSuccess.value  = false
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 받아온 reviewId
        reviewId = arguments?.getInt("reviewId") ?: -1

        // RecyclerView 설정
        imageAdapter = ReviewWriteImageAdapter(requireContext(), imageUrls)
        binding.reviewWriteImageRv.adapter = imageAdapter

        // 수정
        if(reviewId!=-1) {
            // 수정 기본 데이터 유지 시켜 주기
            binding.reviewWriteImageFrame.visibility = View.VISIBLE
            // 장소
            binding.reviewWriteEt.setText(viewModel.reviewSpotName.value)
            // 기존에 서버에서 받아왔던 데이터를 가져와서 유지 시켜 줘야 하는 것임
            imageAdapter.setImages(viewModel.reviewImageUrls.value!!)
            // 후기
            binding.reviewWriteComment.setText(viewModel.reviewWriteContent.value)
            // contendId
            Log.d(TAG, "onViewCreated: 여기는 수정이다. ${viewModel.reviewSpotContentId.value}")
        }

        // 이미지 기본 frame
        binding.reviewWriteImageFrame.setOnClickListener {
            showPopUp(binding.reviewWriteImageFrame)
        }

        // 지역 작성 버튼을 누르면
        binding.reviewWriteEtLayout.setOnClickListener {
            // 검색창 화면 으로 이동
            navController.navigate(R.id.action_reviewWriteFragment_to_reviewSpotNameFragment)
        }

        // 내가 선택한 spotName
        binding.reviewWriteEt.setText(viewModel.reviewSpotName.value)
        Log.d(TAG, "onViewCreated review write fragment : ${viewModel.reviewSpotName.value}")

        // EditText에 TextWatcher 설정
        binding.reviewWriteComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.setReviewWriteContent(s.toString())
                binding.reviewWriteCommentTv.text = "${viewModel.reviewWriteContent.value?.length}자 / 최소 20자"
            }
        })

        viewModel.reviewWriteContent.observe(viewLifecycleOwner) { content ->
            _checkContentLength.value = content != null && content.length >= 20
        }

        viewModel.reviewSpotContentId.observe(viewLifecycleOwner) { id ->
            _checkSpotId.value = id != null
        }

        _isButtonEnabled.observe(viewLifecycleOwner) { isEnabled ->
            if(isEnabled) {
                Log.d(TAG, "onViewCreated: 버튼 상태 - $isEnabled")
                binding.reviewWriteBtn.isEnabled = true
                binding.reviewWriteBtn.setOnClickListener {
                    // 작성 하고 리뷰 화면 으로 다시 이동
                    val request = ReviewInsertRequest(
                        viewModel.reviewSpotContentId.value!!,
                        viewModel.reviewWriteContent.value!!,
                        viewModel.uploadImageUrls.value!!)
                    postReviewData(request)
                    viewModel.clearData()
                }
            }else {
                binding.reviewWriteBtn.isEnabled = false
            }
        }

        binding.reviewWriteBackIcon.setOnClickListener {
            navController.popBackStack()
        }

    }

    private fun checkButtonEnabled() {
        _isButtonEnabled.value = _checkContentLength.value == true &&
                _checkSpotId.value == true &&
                _uploadSuccess.value == true
        Log.d(TAG, "_isButtonEnabled.value: ${_isButtonEnabled.value}")
        Log.d(TAG, "_checkContentLength.value: ${_checkContentLength.value}")
        Log.d(TAG, "_checkSpotId.value: ${_checkSpotId.value}")
        Log.d(TAG, "_uploadSuccess.value: ${_uploadSuccess.value}")
    }

    fun postReviewData(request : ReviewInsertRequest) {
        viewModel.setReviewInsertRequest(request)
        if(reviewId==-1) {
            viewModel.fetchInsert(viewModel.reviewInsertRequest.value) { success ->
                if (success) {
                    // 작성하고 리뷰 화면으로 다시 이동
                    navController.navigate(R.id.action_reviewWriteFragment_to_menu_main_btm_nav_review)
                } else {
                    // 에러 처리
                    Log.d(TAG, "postReviewData: 리뷰 작성 못해따!")
                }
            }
        }else {
            viewModel.fetchUpdate(reviewId, viewModel.reviewInsertRequest.value) { success ->
                if (success) {
                    // 작성하고 리뷰 화면으로 다시 이동
                    navController.popBackStack()
                } else {
                    // 에러 처리
                    Log.d(TAG, "postReviewData: 리뷰 수정 못해따!")
                }
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
                    presignedUrls.clear()
                    viewModel.clearUploadImageUrls()
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

    fun uploadImagesSequentially(context: Context, uris: List<Uri>, callback: (Boolean) -> Unit) {
        lifecycleScope.launch {
            var allSuccess = true
            for (uri in uris) {
                val file = viewModel.uriToFile(context, uri)
                if (file != null) {
                    val mimeType = viewModel.getMimeType(context, uri)
                    if (mimeType != null) {
                        // Presigned URL 발급 요청
                        val success = fetchPresignedReviewAwait(viewModel.getFileExtension(context, uri))
                        if (success) {
                            val presignedUrl = presignedUrls.last()
                            val uploadSuccess = uploadImageAWSAwait(presignedUrl, file, mimeType)
                            if (!uploadSuccess) {
                                allSuccess = false
                                break
                            } else {
                                Log.d(TAG, "Image uploaded successfully")
                            }
                        } else {
                            allSuccess = false
                            break
                        }
                    } else {
                        Log.d(TAG, "Failed to get MIME type for image: $uri")
                        allSuccess = false
                        break
                    }
                } else {
                    Log.d(TAG, "Failed to convert URI to file: $uri")
                    allSuccess = false
                    break
                }
            }
            Log.d(TAG, "uploadImagesSequentially: ${viewModel.uploadImageUrls.value}")
            callback(allSuccess)
        }
    }

    suspend fun fetchPresignedReviewAwait(fileExtension: String): Boolean {
        return suspendCoroutine { continuation ->
            viewModel.fetchPresignedReview(ReviewPresignedRequest(fileExtension)) { success, presignedData ->
                if (success && presignedData != null) {
                    Log.d(TAG, "fetchPresignedReviewAwait: 프리사인드 url 발급 완료")
                    presignedUrls.add(presignedData.presignedUrl)
                    viewModel.addUploadImageUrl(presignedData.imageUrl)
                    continuation.resume(true)
                } else {
                    Log.d(TAG, "Failed to get presigned URL")
                    continuation.resume(false)
                }
            }
        }
    }

    suspend fun uploadImageAWSAwait(presignedUrl: String, file: File, mimeType: String): Boolean {
        return suspendCoroutine { continuation ->
            viewModel.uploadImageAWS(presignedUrl, file, mimeType) { success ->
                continuation.resume(success)
            }
        }
    }

}
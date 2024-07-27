package com.narae.fliwith.src.main.review.models

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narae.fliwith.src.main.review.ReviewApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import com.narae.fliwith.src.main.mypage.MyPageApi.myPageService
import com.narae.fliwith.src.main.review.ReviewApi.reviewService

private const val TAG = "ReviewViewModel_싸피"

class ReviewViewModel : ViewModel() {

    private val _reviewDataResponse = MutableLiveData<ReviewResponse?>()
    val reviewDataResponse: LiveData<ReviewResponse?>
        get() = _reviewDataResponse

    // 내가 좋아요 한 리뷰 조회
    fun fetchLikeReviews(pageNo: Int) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                myPageService.getLikeReviews(pageNo)
            }

            if (response.isSuccessful) {
                _reviewDataResponse.value = response.body()
            } else {
                Log.d(TAG, "fetchLikeReviews Error: ${response.errorBody()?.string()}")
            }
        }
    }

    // 내가 쓴 리뷰 조회
    fun fetchWriteReviews(pageNo: Int) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                myPageService.getWriteReviews(pageNo)
            }

            if (response.isSuccessful) {
                _reviewDataResponse.value = response.body()
            } else {
                Log.d(TAG, "fetchWriteReviews Error: ${response.errorBody()?.string()}")
            }
        }
    }

    // review 목록 전체 조회
    fun fetchSelectAllReviews(pageNo: Int, order: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    reviewService.selectAllReviews(pageNo, order)
                }
                if (response.isSuccessful) {
                    val reviewDataList = response.body()
                    _reviewDataResponse.value = reviewDataList
                    callback(true)
                } else {
                    Log.e(TAG, "Review Response not successful: ${response.errorBody()}")
                    callback(false)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Review Response API call failed", e)
                callback(false)
            }
        }
    }

    private val _reviewDetailData = MutableLiveData<ReviewDetailResponse?>()
    val reviewDetailData: LiveData<ReviewDetailResponse?>
        get() = _reviewDetailData

    // review 상세 조회
    fun fetchSelectReview(reviewId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    reviewService.selectReview(reviewId)
                }
                if (response.isSuccessful) {
                    val reviewData = response.body()
                    _reviewDetailData.value = reviewData
                    callback(true)
                } else {
                    Log.e(TAG, "Review Detail Response not successful: ${response.errorBody()}")
                    callback(false)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Review detail API call failed", e)
                callback(false)
            }
        }
    }

    // review 삭제
    fun fetchDeleteReview(reviewId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    reviewService.deleteReview(reviewId)
                }
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    Log.e(TAG, "Review Delete Response not successful: ${response.errorBody()}")
                    callback(false)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Review delete API call failed", e)
                callback(false)
            }
        }
    }

    // review 좋아요 & 좋아요 취소
    private val _reviewLikeStatus = MutableLiveData<Boolean?>()
    val reviewLikeStatus: LiveData<Boolean?>
        get() = _reviewLikeStatus

    fun setReviewLikeStatue(like: Boolean) {
        _reviewLikeStatus.value = like
    }

    private val _reviewLikeCount = MutableLiveData<Int?>()
    val reviewLikeCount: LiveData<Int?>
        get() = _reviewLikeCount

    fun setReviewLikeCount(count: Int) {
        _reviewLikeCount.value = count
    }

    fun fetchLikeReview(reviewId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    reviewService.likeReview(reviewId)
                }
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    Log.e(TAG, "Review like Response not successful: ${response.errorBody()}")
                    callback(false)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Review like API call failed", e)
                callback(false)
            }
        }
    }

    // review 작성
    private val _reviewInsertRequest = MutableLiveData<ReviewInsertRequest>()
    val reviewInsertRequest: LiveData<ReviewInsertRequest> get() = _reviewInsertRequest

    fun setReviewInsertRequest(request: ReviewInsertRequest) {
        _reviewInsertRequest.value = request
    }

    fun fetchInsert(request: ReviewInsertRequest?, callback: (Boolean) -> Unit) {
        request ?: return callback(false) // null 처리
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    reviewService.insertReview(request)
                }
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    Log.e(TAG, "Review insert Response not successful: ${response.errorBody()}")
                    callback(false)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Review insert API call failed", e)
                callback(false)
            }
        }
    }

    // 서버에서 받아 온 이미지들
    private val _reviewImageUrls = MutableLiveData<List<String>>()
    val reviewImageUrls : LiveData<List<String>> get() = _reviewImageUrls
    fun setReviewImageUrls(urls: List<String>) {
        _reviewImageUrls.value = urls
    }

    // 업로드 할 이미지 url
    private val _uploadImageUrls = MutableLiveData<MutableList<String>>(mutableListOf())
    val uploadImageUrls: LiveData<MutableList<String>> get() = _uploadImageUrls

    fun addUploadImageUrl(url: String) {
        _uploadImageUrls.value?.add(url)
    }

    fun clearUploadImageUrls() {
        _uploadImageUrls.value?.clear()
    }

    // presigend Response
    private val _presignedData = MutableLiveData<PresignedData?>()
    val presignedData: MutableLiveData<PresignedData?> get() = _presignedData

    fun setPresignedData(data: PresignedData) {
        _presignedData.value = data
    }

    fun fetchPresignedReview(
        request: ReviewPresignedRequest,
        callback: (Boolean, PresignedData?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    reviewService.presignedReview(request)
                }
                if (response.isSuccessful) {
                    val presignedData = response.body()?.data
                    if (presignedData != null) {
                        // presigned URL을 성공적으로 받은 경우
                        setPresignedData(presignedData)
                        Log.d(TAG, "fetchPresignedReview: $presignedData")
                        callback(true, presignedData)
                    } else {
                        // presigned URL이 null인 경우
                        callback(false, null)
                    }
                } else {
                    // presigned URL 요청이 실패한 경우
                    Log.e(TAG, "Presigned URL request failed: ${response.errorBody()}")
                    callback(false, null)
                }
            } catch (e: Exception) {
                // 네트워크 오류 등 예외 발생한 경우
                Log.e(TAG, "Presigned URL request failed", e)
                callback(false, null)
            }
        }
    }

    fun uploadImageAWS(
        presignedUrl: String,
        file: File,
        mimeType: String,
        callback: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val client = OkHttpClient()
                    val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
                    val request = Request.Builder()
                        .url(presignedUrl)
                        .put(requestBody)
                        .build()
                    val response = client.newCall(request).execute()
                    if (response.isSuccessful) {
                        callback(true)
                    } else {
                        callback(false)
                        Log.e(TAG, "Image upload failed: ${response.message}")
                    }
                } catch (e: HttpException) {
                    callback(false)
                    Log.e(TAG, "Image upload failed", e)
                } catch (e: Exception) {
                    callback(false)
                    Log.e(TAG, "Unexpected error", e)
                }
            }
        }
    }

    fun uriToFile(context: Context, uri: Uri): File? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "image_file_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }

    fun getMimeType(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        return contentResolver.getType(uri) ?: MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            ?.let {
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(it)
            }
    }

    fun getFileExtension(context: Context, uri: Uri): String {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType?.substringAfterLast('/') ?: "jpg"
    }

    private val _reviewSpotNameResponse = MutableLiveData<ReviewSpotNameResponse?>()
    val reviewSpotNameResponse: LiveData<ReviewSpotNameResponse?>
        get() = _reviewSpotNameResponse

    // 관광지 이름(키워드)으로 검색
    fun fetchSpotName(name: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    reviewService.spotName(name)
                }
                if (response.isSuccessful) {
                    val reviewSpotNameDataList = response.body()
                    _reviewSpotNameResponse.value = reviewSpotNameDataList
                    callback(true)
                } else {
                    Log.e(TAG, "Review insert Response not successful: ${response.errorBody()}")
                    callback(false)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Review insert API call failed", e)
                callback(false)
            }
        }
    }

    // 선택한 spot name
    private val _reviewSpotName = MutableLiveData<String?>()
    val reviewSpotName: LiveData<String?>
        get() = _reviewSpotName

    fun setSpotName(spotName: String) {
        _reviewSpotName.value = spotName
    }

    // 선택한 spot name 의 contentId
    private val _reviewSpotContentId = MutableLiveData<Int?>()
    val reviewSpotContentId: LiveData<Int?>
        get() = _reviewSpotContentId

    fun setSpotContentId(contentId: Int) {
        _reviewSpotContentId.value = contentId
    }

    // content 내용
    private val _reviewWriteContent = MutableLiveData<String?>()
    val reviewWriteContent: LiveData<String?>
        get() = _reviewWriteContent

    fun setReviewWriteContent(content: String) {
        _reviewWriteContent.value = content
    }

    // review 수정
    fun fetchUpdate(reviewId: Int, request: ReviewInsertRequest?, callback: (Boolean) -> Unit) {
        request ?: return callback(false) // null 처리
        viewModelScope.launch {
            try {
                val response = ReviewApi.reviewService.updateReview(reviewId, request)
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    Log.e(TAG, "Review insert Response not successful: ${response.errorBody()}")
                    callback(false)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Review insert API call failed", e)
                callback(false)
            }
        }
    }

    // 리뷰 작성 후 데이터 reset
    fun clearData() {
        _reviewWriteContent.value = ""
        _reviewSpotName.value = ""
        _uploadImageUrls.value = mutableListOf()
    }

}

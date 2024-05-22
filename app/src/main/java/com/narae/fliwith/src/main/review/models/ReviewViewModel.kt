package com.narae.fliwith.src.main.review.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narae.fliwith.src.main.review.ReviewApi
import kotlinx.coroutines.launch

private const val TAG = "ReviewViewModel_싸피"

class ReviewViewModel : ViewModel() {

    private val _reviewDataResponse = MutableLiveData<ReviewResponse?>()
    val reviewDataResponse: LiveData<ReviewResponse?>
        get() = _reviewDataResponse

    // review 목록 전체 조회
    fun fetchSelectAllReviews(pageNo: Int, order: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ReviewApi.reviewService.selectAllReviews(pageNo, order)
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
                val response = ReviewApi.reviewService.selectReview(reviewId)
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
    fun fetchDeleteReview(reviewId : Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ReviewApi.reviewService.deleteReview(reviewId)
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
    fun fetchLikeReview(reviewId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ReviewApi.reviewService.likeReview(reviewId)
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    Log.e(TAG, "Review Delete Response not successful: ${response.errorBody()}")
                    callback(false)
                }
            }catch (e : Exception) {
                Log.e(TAG, "Review like API call failed", e)
                callback(false)
            }
        }
    }

    // review 작성
    fun fetchInsert(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try{
                val response = ReviewApi.reviewService.insertReview()
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    Log.e(TAG, "Review insert Response not successful: ${response.errorBody()}")
                    callback(false)
                }
            }catch (e : Exception) {
                Log.e(TAG, "Review insert API call failed", e)
                callback(false)
            }
        }
    }

    // 리뷰 사진 첨부 URL 요청
    fun fetchPresignedReview(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try{
                val response = ReviewApi.reviewService.presignedReview()
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    Log.e(TAG, "Review insert Response not successful: ${response.errorBody()}")
                    callback(false)
                }
            }catch (e : Exception) {
                Log.e(TAG, "Review insert API call failed", e)
                callback(false)
            }
        }
    }

    private val _reviewSpotNameResponse = MutableLiveData<ReviewSpotNameResponse?>()
    val reviewSpotNameResponse: LiveData<ReviewSpotNameResponse?>
        get() = _reviewSpotNameResponse

    // 관광지 이름(키워드)으로 검색
    fun fetchSpotName(name: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try{
                val response = ReviewApi.reviewService.spotName(name)
                if (response.isSuccessful) {
                    val reviewSpotNameDataList = response.body()
                    _reviewSpotNameResponse.value = reviewSpotNameDataList
                    callback(true)
                } else {
                    Log.e(TAG, "Review insert Response not successful: ${response.errorBody()}")
                    callback(false)
                }
            }catch (e : Exception) {
                Log.e(TAG, "Review insert API call failed", e)
                callback(false)
            }
        }
    }

    // review 수정

}

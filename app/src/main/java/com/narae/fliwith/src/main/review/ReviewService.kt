package com.narae.fliwith.src.main.review

import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.models.ResponseDto
import com.narae.fliwith.src.main.review.models.Review
import com.narae.fliwith.src.main.review.models.ReviewDetailResponse
import com.narae.fliwith.src.main.review.models.ReviewInsertRequest
import com.narae.fliwith.src.main.review.models.ReviewPresignedRequest
import com.narae.fliwith.src.main.review.models.ReviewPresignedResponse
import com.narae.fliwith.src.main.review.models.ReviewResponse
import com.narae.fliwith.src.main.review.models.ReviewSpotNameResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

public interface ReviewService {
    // 리뷰 목록 조회 (인기순, 최신순)
    @GET("review/list")
    suspend fun selectAllReviews(@Query("pageNo") pageNo:Int, @Query("order") order:String): Response<ReviewResponse>

    // 리뷰 상세 조회
    @GET("review/{reviewId}")
    suspend fun selectReview(@Path("reviewId") reviewId:Int): Response<ReviewDetailResponse>

    // 리뷰 작성
    @POST("review")
    suspend fun insertReview(@Body request: ReviewInsertRequest): Response<ResponseDto>

    // 리뷰 수정
    @PATCH("review/{reviewId}")
    suspend fun updateReview(@Path("reviewId") reviewId:Int): Response<Review>

    // 리뷰 삭제
    @DELETE("review/{reviewId}")
    suspend fun deleteReview(@Path("reviewId") reviewId:Int): Response<ResponseDto>

    // 리뷰 좋아요 등록 & 좋아요 취소
    @POST("review/{reviewId}")
    suspend fun likeReview(@Path("reviewId") reviewId:Int): Response<Boolean>

    // 리뷰 사진 첨부 URL 요청
    @POST("review/presigned")
    suspend fun presignedReview(@Body request: ReviewPresignedRequest): Response<ReviewPresignedResponse>

    @PUT
    suspend fun uploadImageAWS(@Url presignedUrl: String, @Body file: RequestBody) : Response<Unit>

    // 관광지 이름 키워드로 검색
    @GET("review")
    suspend fun spotName(@Query("spotName") spotName: String) : Response<ReviewSpotNameResponse>

}

object ReviewApi {
    val reviewService by lazy {
        ApplicationClass.retrofit.create(ReviewService::class.java)
    }
}
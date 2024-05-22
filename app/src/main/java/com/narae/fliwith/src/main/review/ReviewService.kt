package com.narae.fliwith.src.main.review

import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.models.ResponseDto
import com.narae.fliwith.src.main.recommend.RecommendService
import com.narae.fliwith.src.main.review.models.Review
import com.narae.fliwith.src.main.review.models.ReviewDetailResponse
import com.narae.fliwith.src.main.review.models.ReviewResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

public interface ReviewService {
    // 리뷰 목록 조회 (인기순, 최신순)
    @GET("/review/list")
    suspend fun selectAllReviews(@Query("pageNo") pageNo:Int, @Query("order") order:String): Response<ReviewResponse>

    // 리뷰 상세 조회
    @GET("/review/{reviewId}")
    suspend fun selectReview(@Path("reviewId") reviewId:Int): Response<ReviewDetailResponse>

    // 리뷰 작성
    @POST("/review")
    suspend fun insertReview()

    // 리뷰 수정
    @PATCH("/review/{reviewId}")
    suspend fun updateReview(@Path("reviewId") reviewId:Int): Response<Review>

    // 리뷰 삭제
    @DELETE("/review/{reviewId}")
    suspend fun deleteReview(@Path("reviewId") reviewId:Int): Response<ResponseDto>

    // 리뷰 좋아요 등록 & 좋아요 취소
    @POST("/review/{reviewId}")
    suspend fun heartReview(@Path("reviewId") reviewId:Int): Response<Boolean>

    // 리뷰 사진 첨부 URL 요청
    @POST("/review/presigned")
    suspend fun presignedReview(): Response<MutableList<String>>

}

object ReviewApi {
    val reviewService by lazy {
        ApplicationClass.retrofit.create(ReviewService::class.java)
    }
}
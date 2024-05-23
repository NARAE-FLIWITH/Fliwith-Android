package com.narae.fliwith.src.main.recommend.models

import com.narae.fliwith.src.main.review.models.Review
import java.io.Serializable

data class TourResponse(
    val statusCode: Int,
    val message: String,
    val data: TourData
) : Serializable

data class TourData(
    val detailWithTour: DetailWithTour,
    val detailIntro: DetailIntro,
    val detailCommon: DetailCommon,
    val reviews: List<Review>
)

data class DetailWithTour(
    val contentid: String,
    val parking: String,
    val route: String?,
    val publictransport: String,
    val ticketoffice: String?,
    val promotion: String?,
    val wheelchair: String?,
    val exit: String,
    val elevator: String?,
    val restroom: String?,
    val auditorium: String?,
    val room: String?,
    val handicapetc: String,
    val braileblock: String?,
    val helpdog: String?,
    val guidehuman: String?,
    val audioguide: String?,
    val bigprint: String?,
    val brailepromotion: String?,
    val guidesystem: String?,
    val blindhandicapetc: String?,
    val signguide: String?,
    val videoguide: String?,
    val hearingroom: String?,
    val hearinghandicapetc: String?,
    val stroller: String?,
    val lactationroom: String?,
    val babysparechair: String?,
    val infantsfamilyetc: String?
)

data class DetailIntro(
    val contentid: String,
    val contenttypeid: String,
    val scale: String?,
    val usefee: String?,
    val discountinfo: String?,
    val spendtime: String?,
    val parkingfee: String?,
    val infocenterculture: String?,
    val accomcountculture: String?,
    val usetimeculture: String?,
    val restdateculture: String?,
    val parkingculture: String?,
    val chkbabycarriageculture: String?,
    val chkpetculture: String?,
    val chkcreditcardculture: String?
)

data class DetailCommon(
    val contentid: String,
    val contenttypeid: String,
    val title: String,
    val createdtime: String,
    val modifiedtime: String,
    val tel: String,
    val telname: String,
    val homepage: String?,
    val booktour: String?,
    val firstimage: String?,
    val firstimage2: String?,
    val cpyrhtDivCd: String,
    val areacode: String,
    val sigungucode: String,
    val addr1: String,
    val addr2: String?,
    val zipcode: String
)


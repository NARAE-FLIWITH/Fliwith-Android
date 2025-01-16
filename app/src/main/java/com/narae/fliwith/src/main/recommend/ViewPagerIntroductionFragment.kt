package com.narae.fliwith.src.main.recommend

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentViewPagerIntroductionBinding
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "ViewPagerIntroductionFr_싸피"
class ViewPagerIntroductionFragment : BaseFragment<FragmentViewPagerIntroductionBinding>(
    FragmentViewPagerIntroductionBinding::inflate) {

    private val viewModel: RecommendViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchView()

    }

    private fun fetchView() {
        Log.d(TAG, "fetchView tourData : ${viewModel.tourData.value}")
        val responseData = viewModel.tourData.value?.data

        binding.introductionAddressTv.text = responseData?.detailCommon?.addr1 // 주소

        additionDetailTourData()
    }

    private fun additionDetailTourData() {
        val responseData = viewModel.tourData.value?.data?.detailWithTour

        // 필드 다 확인해
        val fieldsToCheck = listOf(
            responseData?.parking,
            responseData?.route,
            responseData?.publictransport,
            responseData?.ticketoffice,
            responseData?.promotion,
            responseData?.wheelchair,
            responseData?.exit,
            responseData?.elevator,
            responseData?.restroom,
            responseData?.auditorium,
            responseData?.room,
            responseData?.handicapetc,
            responseData?.braileblock,
            responseData?.helpdog,
            responseData?.guidehuman,
            responseData?.audioguide,
            responseData?.bigprint,
            responseData?.brailepromotion,
            responseData?.guidesystem,
            responseData?.blindhandicapetc,
            responseData?.signguide,
            responseData?.videoguide,
            responseData?.hearingroom,
            responseData?.hearinghandicapetc,
            responseData?.stroller,
            responseData?.lactationroom,
            responseData?.babysparechair,
            responseData?.infantsfamilyetc
        )

        val nonEmptyFields = fieldsToCheck.filterNot { it.isNullOrBlank() }

        binding.introductionNumberTv.text = viewModel.tourData.value?.data?.detailCommon?.tel // 번호
        if(viewModel.tourData.value?.data?.detailCommon?.tel.isNullOrBlank() && nonEmptyFields.isNotEmpty()) binding.introductionNumberTv.text = nonEmptyFields[0]
        else if(viewModel.tourData.value?.data?.detailCommon?.tel.isNullOrBlank() && nonEmptyFields.isEmpty()) {
            binding.introductionNumberTv.visibility = View.GONE
        }
        if(nonEmptyFields.size >= 2) binding.introductionAddition1Tv.text = nonEmptyFields[1]
        else binding.introductionAddition1Tv.visibility = View.GONE
        if(nonEmptyFields.size >= 3) binding.introductionAddition2Tv.text = nonEmptyFields[2]
        else binding.introductionAddition2Tv.visibility = View.GONE
        if(nonEmptyFields.size >= 4) {
            val homepage = viewModel.tourData.value?.data?.detailCommon?.homepage
            val urlRegex = """href\s*=\s*"(http[s]?://[^"]+)"""".toRegex()

            // url 없을 때
            val extractedUrl = homepage?.let { urlRegex.find(it)?.groups?.get(1)?.value } ?: nonEmptyFields[2]
            binding.introductionAddition3Tv.text = extractedUrl // url
        }
        else binding.introductionAddition3Tv.visibility = View.GONE

    }

}
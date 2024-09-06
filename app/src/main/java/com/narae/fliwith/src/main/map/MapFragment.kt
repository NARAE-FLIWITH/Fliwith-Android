package com.narae.fliwith.src.main.map

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextStyle
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.DialogRequestActivateBinding
import com.narae.fliwith.databinding.DialogRequestPermissionsBinding
import com.narae.fliwith.databinding.FragmentMapBinding
import com.narae.fliwith.src.main.map.MapApi.mapService
import com.narae.fliwith.src.main.map.models.SpotRequest
import com.narae.fliwith.src.main.map.models.SpotWithLocation
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel
import com.narae.fliwith.util.setOnSingleClickListener
import com.narae.fliwith.util.showCustomSnackBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MapFragment"

class MapFragment : BaseFragment<FragmentMapBinding>(FragmentMapBinding::inflate) {

    private lateinit var mapView: MapView
    private lateinit var homeLocation: LatLng
    private var centerPosition = LatLng.from(37.547850180, 127.074454848)
    private lateinit var homeLabelStyles: LabelStyles
    private lateinit var labelStyles: LabelStyles
    private lateinit var map: KakaoMap
    private var spots: List<SpotWithLocation> = mutableListOf()

    private val recommendViewModel by activityViewModels<RecommendViewModel>()

    private val lifecycleCallback = object : MapLifeCycleCallback() {
        // 지도 API 가 정상적으로 종료될 때 호출됨
        override fun onMapDestroy() {
        }

        // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
        override fun onMapError(p0: Exception?) {
        }
    }

    private val kakaoMapReadyCallback = object : KakaoMapReadyCallback() {

        // 인증 후 API 가 정상적으로 실행될 때 호출됨
        override fun onMapReady(p0: KakaoMap) {
            map = p0
            homeLabelStyles =
                map.labelManager?.addLabelStyles(
                    LabelStyles.from(
                        LabelStyle.from(R.drawable.home_marker)
                            .setTextStyles(LabelTextStyle.from(22, R.color.grey))
                    )
                )!!
            labelStyles =
                map.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.spot)))!!
            map.cameraMaxLevel = 19
            map.cameraMinLevel = 8

            if (::homeLocation.isInitialized) {
                restoreMap()
            } else {
                setInitialLocation()
            }

            map.setOnCameraMoveEndListener { _, cameraPosition, _ ->
                centerPosition = cameraPosition.position
            }

            setListeners()
        }

        @SuppressLint("MissingPermission")
        private fun setInitialLocation() {
            val locationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            locationClient.lastLocation.addOnCompleteListener {
                val location = if (it.isSuccessful && it.result != null) {
                    LatLng.from(it.result.latitude, it.result.longitude)
                } else {
                    centerPosition
                }

                map.moveCamera(CameraUpdateFactory.newCenterPosition(location))
                homeLocation = LatLng.from(location.latitude, location.longitude)
                centerPosition = homeLocation
                setHomeLabel()
            }
        }

        // 지도 시작 시 확대/축소 줌 레벨 설정
        override fun getZoomLevel(): Int {
            return 13
        }

        override fun getPosition(): LatLng {
            return super.getPosition()
        }
    }

    private fun restoreMap() {
        map.moveCamera(CameraUpdateFactory.newCenterPosition(centerPosition))
        spots.forEach {
            setLabel(it)
        }
        setHomeLabel()
        setMarkerTouchEvent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapView

        val window = requireActivity().window
        // 플래그를 추가하여 시스템 바(상태바 및 네비게이션 바)가 윈도우 배경을 그리도록 설정
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // 플래그를 제거하여 상태바를 반투명 모드에서 해제
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // 상태바의 배경색을 투명으로 설정
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = (
                // 앱이 전체 화면 레이아웃 모드에서 작동, 시스템 UI의 레이아웃 안정성을 유지, 상태바의 아이콘과 텍스트를 진하게 설정
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                )

        checkPermissions()
        checkLocationActivated()
    }

    // Fragment가 사라질 때 상태바 원상복구
    override fun onDestroyView() {
        super.onDestroyView()
        // 상태바 및 시스템 UI 설정 원상복귀
        val window = requireActivity().window
        // 상태바 배경 그리는 플래그 다시 추가
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // 반투명 모드에서 해제
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // 상태바 색상 흰색으로 변경
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        // 상태바 아이콘과 텍스트를 진하게 설정
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    private fun setListeners() {
        // 검색 버튼 터치 시
        binding.layoutFab.setOnSingleClickListener {
            if (!(requireContext().getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(
                    LocationManager.FUSED_PROVIDER
                )
            ) {
                showLocationActivateDialog()
                return@setOnSingleClickListener
            }

            if (!isLocationPermissionsPermitted()) {
                showPermissionDialog()
                return@setOnSingleClickListener
            }
            searchSpots()
        }

        // 현 위치 터치 시
        binding.btnHome.setOnSingleClickListener {
            map.moveCamera(
                CameraUpdateFactory.newCenterPosition(homeLocation),
                CameraAnimation.from(500, true, true)
            )
        }
    }

    private fun searchSpots() {
        lifecycleScope.launch {
            mLoadingDialog.show()
            runCatching {
                withContext(Dispatchers.IO) {
                    mapService.searchByLocation(
                        centerPosition.latitude,
                        centerPosition.longitude,
                    )
                }
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    // 기존 모든 라벨 지우기
                    val layer: LabelLayer = map.labelManager?.layer!!
                    layer.removeAll()
                    // 홈 라벨 찍어주기
                    setHomeLabel()
                    spots = response.body()?.spotList!!

                    // 조회 결과가 비어 있으면
                    if (spots.isEmpty()) {
                        showCustomSnackBar(requireContext(), binding.root, "주변에 관광지가 없어요 😭")
                    }
                    // 있으면
                    else {
                        spots.forEach {
                            setLabel(it)
                        }
                        setMarkerTouchEvent()
                        showCustomSnackBar(requireContext(), binding.root, "주변 관광지를 찾았어요")
                    }
                } else {
                    Log.d(TAG, "searchSpots Error: ${response.errorBody()?.string()}")
                    showCustomSnackBar(requireContext(), binding.root, "검색 중 오류가 발생했습니다.")
                }
            }.onFailure { e ->
                Log.e(TAG, "Network error: ${e.message}")
                showCustomSnackBar(requireContext(), binding.root, "잠시 후 다시 시도해 주세요.")
            }.also {
                mLoadingDialog.dismiss()
            }
        }
    }

    private fun setLabel(spot: SpotWithLocation) {
        val options =
            LabelOptions.from(LatLng.from(spot.latitude, spot.longitude)).setStyles(labelStyles)
        options.tag = spot
        val layer: LabelLayer = map.labelManager?.layer!!
        layer.addLabel(options)
    }

    private fun setMarkerTouchEvent() {
        // 맵 라벨 클릭시 상세 페이지 보여주기
        map.setOnLabelClickListener { _, _, label ->
            mLoadingDialog.show()

            val spotItem = label.tag as SpotWithLocation
            val request =
                SpotRequest(spotItem.contentTypeId.toString(), spotItem.contentId.toString())
            recommendViewModel.fetchTourDetailData(request) { success ->
                if (success) {

                    val bundle = bundleOf().apply {
                        putBoolean("fromMap", true)
                    }
                    navController.navigate(
                        R.id.action_menu_main_btm_nav_map_to_recommendAIFragment,
                        bundle
                    )
                } else {
                    showCustomSnackBar(requireContext(), binding.root, "잠시 후 다시 시도해 주세요")
                    Log.d(TAG, "상세 데이터 로딩 오류")
                }
                mLoadingDialog.dismiss()
            }
        }
    }

    private fun setHomeLabel() {
        val options = LabelOptions.from(homeLocation).setStyles(homeLabelStyles).setTexts("현위치")
            .apply { isClickable = false }
        val layer: LabelLayer = map.labelManager?.getLayer()!!
        layer.addLabel(options)
    }

    private fun isLocationPermissionsPermitted(): Boolean {
        val permissions = arrayOf(
            permission.ACCESS_COARSE_LOCATION,
            permission.ACCESS_FINE_LOCATION
        )

        val result = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_DENIED
        }

        return result.isEmpty()
    }


    private fun checkLocationActivated() {
        val locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.FUSED_PROVIDER)) {
            showLocationActivateDialog()
        }
    }

    private fun showLocationActivateDialog() {
        lateinit var dialog: AlertDialog
        val dialogBinding =
            DialogRequestActivateBinding.inflate(LayoutInflater.from(requireContext()))
                .apply {
                    btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    btnGo.setOnClickListener {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        dialog.dismiss()
                    }
                }
        dialog = AlertDialog.Builder(requireContext()).setView(dialogBinding.root).show()
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }

    private fun checkPermissions() {
        TedPermission.create().setPermissionListener(
            object : PermissionListener {
                override fun onPermissionGranted() {
                    mapView.start(lifecycleCallback, kakaoMapReadyCallback)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    showPermissionDialog()
                }
            }
        ).setPermissions(
            permission.ACCESS_FINE_LOCATION,
            permission.ACCESS_COARSE_LOCATION
        ).check()
    }

    fun showPermissionDialog() {
        lateinit var dialog: AlertDialog
        val dialogBinding =
            DialogRequestPermissionsBinding.inflate(LayoutInflater.from(requireContext()))
                .apply {
                    btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    btnGo.setOnClickListener {
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts(
                            "package",
                            requireActivity().packageName,
                            null
                        )
                        intent.setData(uri)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                }
        dialog =
            AlertDialog.Builder(requireContext()).setView(dialogBinding.root).show()
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }

    override fun onResume() {
        super.onResume()
        if (::mapView.isInitialized && mapView.isActivated)
            mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        if (::mapView.isInitialized && mapView.isActivated)
            mapView.pause()
    }

}
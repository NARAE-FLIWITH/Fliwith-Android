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
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.DialogRequestActivateBinding
import com.narae.fliwith.databinding.DialogRequestPermissionsBinding
import com.narae.fliwith.databinding.FragmentMapBinding
import com.narae.fliwith.util.setOnSingleClickListener


private const val TAG = "싸피"

class MapFragment : BaseFragment<FragmentMapBinding>(FragmentMapBinding::inflate) {

    private lateinit var mapView: MapView
    lateinit var homeLabelStyles: LabelStyles
    lateinit var labelStyles: LabelStyles
    lateinit var map: KakaoMap

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
                map.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.home_marker)))!!
            labelStyles =
                map.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.spot)))!!
            map.cameraMaxLevel = 19
            map.cameraMinLevel = 14
            setInitialLocation()
        }

        @SuppressLint("MissingPermission")
        private fun setInitialLocation() {
            val locationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            locationClient.lastLocation.addOnCompleteListener {
                val location = if (it.isSuccessful && it.result != null) {
                    LatLng.from(it.result.latitude, it.result.longitude)
                } else {
                    LatLng.from(37.547850180, 127.074454848)
                }

                map.moveCamera(CameraUpdateFactory.newCenterPosition(location))
                setHomeMarker(location.latitude, location.longitude)
            }
        }

        // 지도 시작 시 확대/축소 줌 레벨 설정
        override fun getZoomLevel(): Int {
            return 18
        }

        private fun setHomeMarker(lat: Double, lng: Double) {
            val options = LabelOptions.from(LatLng.from(lat, lng)).setStyles(homeLabelStyles)
            val layer: LabelLayer = map.labelManager?.getLayer()!!
            layer.addLabel(options)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapView
        checkPermissions()
        checkLocationActivated()
        setListeners()
    }

    private fun setListeners() {
        binding.layoutFab.setOnSingleClickListener {

            if ((requireContext().getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(
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


        }
    }

    private fun setMarker(lat: Double, lng: Double) {
        val options = LabelOptions.from(LatLng.from(lat, lng)).setStyles(labelStyles)
        val layer: LabelLayer = map.labelManager?.getLayer()!!
        layer.addLabel(options)
    }

    private fun isLocationPermissionsPermitted(): Boolean {
        val permissions = arrayOf(
            permission.ACCESS_COARSE_LOCATION,
            permission.ACCESS_FINE_LOCATION
        )

        permissions.filter { permission ->
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_DENIED
        }

        return permissions.isEmpty()
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
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }
}
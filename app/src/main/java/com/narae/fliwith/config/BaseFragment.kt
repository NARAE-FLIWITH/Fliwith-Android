package com.narae.fliwith.config

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.narae.fliwith.config.ApplicationClass.Companion.sharedPreferences
import com.narae.fliwith.config.util.NetworkUtil
import com.narae.fliwith.src.auth.AuthActivity
import com.narae.fliwith.src.main.recommend.ViewPagerAdapter
import com.narae.fliwith.util.LoadingDialog

// Fragment의 기본을 작성, 뷰 바인딩 활용
abstract class BaseFragment<B : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> B
) : Fragment() {
    private var _binding: B? = null
    private var _navController: NavController? = null
    protected val mLoadingDialog: LoadingDialog by lazy {
        LoadingDialog(requireContext())
    }
    lateinit var networkUtil: NetworkUtil

    protected val binding get() = _binding!!
    protected val navController get() = _navController!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        networkUtil = NetworkUtil(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!networkUtil.isNetworkAvailable()) {
            networkUtil.showNetworkDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate(inflater, container, false)
        _navController = findNavController()
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun showCustomToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    fun showLoadingDialog() {
        if (!mLoadingDialog.isShowing) {
            mLoadingDialog.show()
        }
    }

    fun dismissLoadingDialog() {
        if (mLoadingDialog.isShowing) {
            mLoadingDialog.dismiss()
        }
    }
}
package com.narae.fliwith.config

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kakao.vectormap.KakaoMapSdk
import com.narae.fliwith.BuildConfig
import com.narae.fliwith.util.SharedPreferencesUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClass : Application() {
    // 코틀린의 전역변수
    companion object {
        // 만들어져있는 SharedPreferences 를 사용해야합니다. 재생성하지 않도록 유념해주세요
        lateinit var sharedPreferences: SharedPreferencesUtil

        const val SHARED_PREFERENCES_NAME = "FLIWITH"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val GRANT_TYPE = "GRANT_TYPE"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val REFRESH_TOKEN_EXPIRATION_TIME = "REFRESH_TOKEN_EXPIRATION_TIME"
        const val CREATED_AT = "CREATED_AT"
        const val AUTHORIZATION = "Authorization"

        val POLICY_CONTRACT_URL = BuildConfig.POLICY_CONTRACT_URL
        val PRIVACY_CONTRACT_URL = BuildConfig.PRIVACY_CONTRACT_URL
        val SENSITIVE_CONTRACT_URL = BuildConfig.SENSITIVE_CONTRACT_URL

        //ends with '/'
        val API_URL = BuildConfig.SERVER_URL
        val GPT_KEY = BuildConfig.GPT_KEY
        val KAKAO_APP_KEY = BuildConfig.KAKAO_APP_KEY
        val KAKAO_MAP_KEY = BuildConfig.KAKAO_APP_KEY


        // Retrofit 인스턴스, 앱 실행시 한번만 생성하여 사용합니다.
        lateinit var retrofit: Retrofit
    }

    // 앱이 처음 생성되는 순간, SP를 새로 만들어주고, 레트로핏 인스턴스를 생성합니다.
    override fun onCreate() {
        super.onCreate()

        sharedPreferences = SharedPreferencesUtil(applicationContext)

        // 레트로핏 인스턴스 생성
        initRetrofitInstance()

        KakaoMapSdk.init(this, KAKAO_APP_KEY);
    }

    // 레트로핏 인스턴스를 생성하고, 레트로핏에 각종 설정값들을 지정해줍니다.
    // 연결 타임아웃시간은 5초로 지정이 되어있고, HttpLoggingInterceptor를 붙여서 어떤 요청이 나가고 들어오는지를 보여줍니다.
    private fun initRetrofitInstance() {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(AddTokenInterceptor(this)) // JWT 자동 헤더 전송
            .build()

        // retrofit 이라는 전역변수에 API url, 인터셉터, Gson을 넣어주고 빌드해주는 코드
        // 이 전역변수로 http 요청을 서버로 보내면 됩니다.
        retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    //GSon은 엄격한 json type을 요구하는데, 느슨하게 하기 위한 설정. success, fail이 json이 아니라 단순 문자열로 리턴될 경우 처리..
    val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

}
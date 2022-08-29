package com.cyberquick.hearthstonedecks.data.server.battlenet.oauth

import android.util.Base64
import retrofit2.http.*


interface OAuthApi {

    companion object {
        private const val CLIENT_ID = "42b35d1bc2ae486cad95fef8f31d5ace"
        private const val CLIENT_SECRET = "PRGJ2kcFHThQXaKr7MFf490NjKsu2FFx"
    }

    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun retrieveOAuth(
        @Header("Authorization") authorization: String = authBase64(),
        @Field("grant_type") grantType: String = "client_credentials"
    ): OAuthResponse

    private fun authBase64() = "Basic " + ("$CLIENT_ID:$CLIENT_SECRET").toBase64()
    private fun String.toBase64() = Base64.encodeToString(toByteArray(), Base64.NO_WRAP)

}
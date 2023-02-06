package com.myadat.interfaces

import com.myadat.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface InterfaceAuthApi {
    @POST("/authUser")
    suspend fun getAuthId(@Body data: sendAuthReq):Response<authResults>

    @POST("/insert")
    suspend fun insertPurchaseRecord(@Body insertData:purchase):Response<String>

    @POST("/getPurchases")
    suspend fun getAllPurchases(@Body getPurchase: sendPurchaseRequest):Response<getAllPurchaseResults>
}
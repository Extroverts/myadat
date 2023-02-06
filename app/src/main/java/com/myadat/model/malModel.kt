package com.myadat.model

import com.google.gson.annotations.SerializedName

data class malModel(val malName:String,var nag:String,val vajan:String,val dar:String,val total:String)

data class authModel(val is_active:String,val end_at:String,val trader_name:String)

data class authResults (@SerializedName("results" ) var results : ArrayList<authModel>)

data class getAllPurchaseResults (@SerializedName("results" ) var results : ArrayList<purchase>)

data class sendAuthReq(val contact:String, val serialId:String)


/*sqlite Database*/

data class purchase(
    val trader_contact_no:String,
    val user_contact_no:String,
    var created_at:String,
    var name:String,
    var mal:String,
    val commission_percent:String,
    val aadat:String,
    val tolai:String,
    val hamali:String,
    val kameti:String,
    val uchal:String,
    val bhade:String,
    val badla:String,
    val rakkam:String,
    val kharche: String,
    val ekun: String,
    val ekunDile:String,
    val ekunBaki:String)

data class purchaseWithId(
    val id:String,
    val trader_contact_no:String,
    val user_contact_no:String,
    var created_at:String,
    var name:String,
    var mal:String,
    val commission_percent:String,
    val aadat:String,
    val tolai:String,
    val hamali:String,
    val kameti:String,
    val uchal:String,
    val bhade:String,
    val badla:String,
    val rakkam:String,
    val kharche: String,
    val ekun: String,
    val ekunDile:String,
    val ekunBaki:String)

data class sell(
    val trader_contact_no:String,
    val user_contact_no:String,
    var created_at:String,
    var name:String,
    var mal:String,
    val commission_percent:String,
    val rakkam:String,
    val aadat: String,
    val ekun: String,
    val magilBaki:String,
    val jama:String,
    val shillak:String)


data class bakiAmt(val trader_contact_no:String,
                      val user_contact_no:String,val ekunBaki:String)



data class sendPurchaseRequest(val trader_contact_no:String)
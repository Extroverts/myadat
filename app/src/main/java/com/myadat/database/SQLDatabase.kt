package com.myadat.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.myadat.model.bakiAmt
import com.myadat.model.purchase
import com.myadat.model.purchaseWithId
import com.myadat.model.sell

class SQLDatabase(val context: Context) : SQLiteOpenHelper(context, "aadat.db", null, 1, null) {
    override fun onCreate(db: SQLiteDatabase?) {

        db!!.execSQL("CREATE TABLE IF NOT EXISTS purchase(id INTEGER PRIMARY KEY, trader_contact_no TEXT, user_contact_no TEXT, created_at TEXT, name TEXT, mal TEXT, commission_percent TEXT,aadat TEXT, tolai TEXT, hamali TEXT, kameti TEXT, uchal TEXT, bhade TEXT, badla TEXT,rakkam TEXT,kharche TEXT,ekun TEXT, ekunDile TEXT, ekunBaki TEXT);")


        db.execSQL("CREATE TABLE IF NOT EXISTS sell(id INTEGER PRIMARY KEY, trader_contact_no TEXT, user_contact_no TEXT, created_at TEXT, name TEXT, mal TEXT, commission_percent TEXT, " +
                    "rakkam TEXT,aadat TEXT,ekun TEXT,magilBaki TEXT, jama TEXT, shillak TEXT);")

        db.execSQL("CREATE TABLE IF NOT EXISTS baki(id INTEGER PRIMARY KEY, trader_contact_no TEXT, user_contact_no TEXT,ekunBaki TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }


    fun addOrUpdatePurchase(purchase: purchase,id:String="0") {
        val values = ContentValues()
        values.put("trader_contact_no", purchase.trader_contact_no)
        values.put("user_contact_no", purchase.user_contact_no)
        values.put("created_at", purchase.created_at)
        values.put("name", purchase.name)
        values.put("mal", purchase.mal)
        values.put("commission_percent", purchase.commission_percent)
        values.put("aadat", purchase.aadat)
        values.put("tolai", purchase.tolai)
        values.put("hamali", purchase.hamali)
        values.put("kameti", purchase.kameti)
        values.put("uchal", purchase.uchal)
        values.put("bhade", purchase.bhade)
        values.put("badla", purchase.badla)
        values.put("rakkam", purchase.rakkam)
        values.put("kharche", purchase.kharche)
        values.put("ekun", purchase.ekun)
        values.put("ekunDile", purchase.ekunDile)
        values.put("ekunBaki", purchase.ekunBaki)
        val db = this.writableDatabase

        if(id!="0"){
            db.update("purchase",values,"id='${id}'",null)
        }else{
            db.insert("purchase", null, values)
        }
        db.close()

    }


    fun addBakiAmount(bakiAmt:bakiAmt){
        val db = this.writableDatabase
        var isRecordAvailable=false
        val query="SELECT EXISTS(SELECT * FROM baki where user_contact_no='${bakiAmt.user_contact_no}' LIMIT 1)"
        val cursor:Cursor=db.rawQuery(query,null)
        cursor.moveToFirst()
        isRecordAvailable = if (cursor.getInt(0) == 1) {
            cursor.close();
            true
        } else {
            cursor.close();
            false
        }

        if(isRecordAvailable){
            val query="UPDATE baki SET ekunBaki='${bakiAmt.ekunBaki}' where user_contact_no='${bakiAmt.user_contact_no}'"
            var isUpdated=db.execSQL(query)
        }else{
            val values = ContentValues()
            values.put("trader_contact_no", bakiAmt.trader_contact_no)
            values.put("user_contact_no", bakiAmt.user_contact_no)
            values.put("ekunBaki", bakiAmt.ekunBaki)
            db.insert("baki", null, values)
        }
        db.close()
    }

    @SuppressLint("Range")
    fun getBakiAmount(custNum:String):String{
        val db = this.readableDatabase
        var totalPurchaseActivity=0
        val toString:Cursor = db.rawQuery("select ekunBaki as Total from baki where user_contact_no=${custNum} limit 1",null)
        if(toString.moveToFirst()) {
            totalPurchaseActivity = toString.getInt(toString.getColumnIndex("Total"))
        }
        db.close()
        return totalPurchaseActivity.toString()
    }


    fun addSell(sell: sell){
        val values = ContentValues()
        values.put("trader_contact_no", sell.trader_contact_no)
        values.put("user_contact_no", sell.user_contact_no)
        values.put("created_at", sell.created_at)
        values.put("name", sell.name)
        values.put("mal", sell.mal)
        values.put("commission_percent", sell.commission_percent)
        values.put("rakkam", sell.rakkam)
        values.put("aadat", sell.aadat)
        values.put("ekun", sell.ekun)
        values.put("magilBaki", sell.magilBaki)
        values.put("jama", sell.jama)
        values.put("shillak", sell.shillak)
        val db = this.writableDatabase
        db.insert("sell", null, values)
        db.close()
    }


    fun getALlPurchaseHistory():MutableList<purchaseWithId>{
        var getPurchaseData:MutableList<purchaseWithId>?=null
        getPurchaseData=ArrayList()
        val db = this.readableDatabase
        val query:Cursor=db.rawQuery("select * from purchase",null)
        query.moveToFirst()
        if(query.moveToFirst()){
            do {
                val purchase=purchaseWithId(
                    query.getString(query.getColumnIndexOrThrow("id")),
                    query.getString(query.getColumnIndexOrThrow("trader_contact_no")),
                    query.getString(query.getColumnIndexOrThrow("user_contact_no")),
                    query.getString(query.getColumnIndexOrThrow("created_at")),
                    query.getString(query.getColumnIndexOrThrow("name")),
                    query.getString(query.getColumnIndexOrThrow("mal")),
                    query.getString(query.getColumnIndexOrThrow("commission_percent")),
                    query.getString(query.getColumnIndexOrThrow("aadat")),
                    query.getString(query.getColumnIndexOrThrow("tolai")),
                    query.getString(query.getColumnIndexOrThrow("hamali")),
                    query.getString(query.getColumnIndexOrThrow("kameti")),
                    query.getString(query.getColumnIndexOrThrow("uchal")),
                    query.getString(query.getColumnIndexOrThrow("bhade")),
                    query.getString(query.getColumnIndexOrThrow("badla")),
                    query.getString(query.getColumnIndexOrThrow("rakkam")),
                    query.getString(query.getColumnIndexOrThrow("kharche")),
                    query.getString(query.getColumnIndexOrThrow("ekun")),
                    query.getString(query.getColumnIndexOrThrow("ekunDile")),
                    query.getString(query.getColumnIndexOrThrow("ekunBaki")))
                getPurchaseData.add(purchase)
            }while (query.moveToNext())
        }
        query.close()
        return getPurchaseData
    }



    fun getPurchaseById(id:String):purchase{
        val db = this.readableDatabase
        val query:Cursor=db.rawQuery("select * from purchase where id='${id}'",null)
        query.moveToFirst()

        val purchase=purchase(
            query.getString(query.getColumnIndexOrThrow("trader_contact_no")),
            query.getString(query.getColumnIndexOrThrow("user_contact_no")),
            query.getString(query.getColumnIndexOrThrow("created_at")),
            query.getString(query.getColumnIndexOrThrow("name")),
            query.getString(query.getColumnIndexOrThrow("mal")),
            query.getString(query.getColumnIndexOrThrow("commission_percent")),
            query.getString(query.getColumnIndexOrThrow("aadat")),
            query.getString(query.getColumnIndexOrThrow("tolai")),
            query.getString(query.getColumnIndexOrThrow("hamali")),
            query.getString(query.getColumnIndexOrThrow("kameti")),
            query.getString(query.getColumnIndexOrThrow("uchal")),
            query.getString(query.getColumnIndexOrThrow("bhade")),
            query.getString(query.getColumnIndexOrThrow("badla")),
            query.getString(query.getColumnIndexOrThrow("rakkam")),
            query.getString(query.getColumnIndexOrThrow("kharche")),
            query.getString(query.getColumnIndexOrThrow("ekun")),
            query.getString(query.getColumnIndexOrThrow("ekunDile")),
            query.getString(query.getColumnIndexOrThrow("ekunBaki")))

        query.close()
        return purchase

    }



    fun deleteFromPurchaseById(id:String):Boolean{
        val db = this.writableDatabase
        return db.delete("purchase","id='${id}'",null) > 0


    }


/*
    fun getPurchase(isPurchaseOrSell:Int):MutableList<purchase>{
        val purchaseHistoryList=ArrayList<purchase>()
        val db = this.readableDatabase
        val query:Cursor

        if(isPurchaseOrSell==Constants.SELL){
            query = db.rawQuery("select * from sell", null)
        }else{
            query= db.rawQuery("select * from purchase", null)
        }
        query.moveToFirst()
        if (query.moveToFirst()) {
            do {
                val purchase = sell(
                    query.getString(query.getColumnIndexOrThrow("trader_contact_no")),
                    query.getString(query.getColumnIndexOrThrow("user_contact_no")),
                    query.getString(query.getColumnIndexOrThrow("created_at")),
                    query.getString(query.getColumnIndexOrThrow("name")),
                    query.getString(query.getColumnIndexOrThrow("mal")),
                    query.getString(query.getColumnIndexOrThrow("commission_percent")),
                    query.getString(query.getColumnIndexOrThrow("rakkam")),
                    query.getString(query.getColumnIndexOrThrow("kharche")),
                    query.getString(query.getColumnIndexOrThrow("ekun")),
                    query.getString(query.getColumnIndexOrThrow("ekunDile")),
                    query.getString(query.getColumnIndexOrThrow("ekunBaki"))
                )

                purchaseHistoryList.add(purchase)
            } while (query.moveToNext());
        }
        db.close()
        return purchaseHistoryList

    }



    fun getTotalSell():String{
        val db = this.readableDatabase
        var totalPurchaseActivity=0
        val toString:Cursor = db.rawQuery("select SUM(total) as Total from sell",null)
        if(toString.moveToFirst()) {
            totalPurchaseActivity = toString.getInt(toString.getColumnIndex("Total"))
        }
        db.close()
        return totalPurchaseActivity.toString()
    }


    fun getUserDetails(custContact:String):purchase{
        val db = this.readableDatabase

        val query:Cursor=db.rawQuery("select * from purchase where user_contact_no=${custContact} limit 1",null)
        query.moveToFirst()

        val purchase = purchase(
            query.getString(query.getColumnIndexOrThrow("trader_contact_no")),
            query.getString(query.getColumnIndexOrThrow("user_contact_no")),
            query.getString(query.getColumnIndexOrThrow("created_at")),
            query.getString(query.getColumnIndexOrThrow("name")),
            query.getString(query.getColumnIndexOrThrow("mal")),
            query.getString(query.getColumnIndexOrThrow("commission_percent")),
            query.getString(query.getColumnIndexOrThrow("aadat")),
            query.getString(query.getColumnIndexOrThrow("tolai")),
            query.getString(query.getColumnIndexOrThrow("hamali")),
            query.getString(query.getColumnIndexOrThrow("kameti")),
            query.getString(query.getColumnIndexOrThrow("uchal")),
            query.getString(query.getColumnIndexOrThrow("bhade")),
            query.getString(query.getColumnIndexOrThrow("badla")),
            query.getString(query.getColumnIndexOrThrow("rakkam")),
            query.getString(query.getColumnIndexOrThrow("kharche")),
            query.getString(query.getColumnIndexOrThrow("ekun")),
            query.getString(query.getColumnIndexOrThrow("ekunDile")),
            query.getString(query.getColumnIndexOrThrow("ekunBaki"))

        )
        return purchase
    }*/
}
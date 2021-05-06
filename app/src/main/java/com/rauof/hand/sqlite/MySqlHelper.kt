package com.rauof.hand

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.service.autofill.FillEventHistory

class MySqlHelper(context: Context) : SQLiteOpenHelper(context, "MyDatabase2", null, 2) {

    companion object {
         val TABLE_NAME_PLAYING:String  = "PLAYING"
         val ID_COLUMN:String  = "_id"
         val NAME:String  = "NAME"
         val CURRNET_SCORE:String  = "Currentscore"
         val HISTORY:String  = "HISTORY"
         val LAST_GAME:String  = "LastGame"
         val TOTLA_GAMES:String  = "TotlaGames"
    }

    private  val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${TABLE_NAME_PLAYING} ( ${ID_COLUMN} INTEGER PRIMARY KEY AUTOINCREMENT, ${NAME} TEXT NOT NULL," +
                " ${CURRNET_SCORE} INT NOT NULL, ${HISTORY} TEXT NOT NULL,  ${LAST_GAME} INT NOT NULL,${TOTLA_GAMES} INT NOT NULL)"


    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME_PLAYING}"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db,oldVersion,newVersion)
    }

    fun getHighestScore():Int {
        val mdb:SQLiteDatabase = this.readableDatabase
        var cursor:Cursor? = null
        try{
             cursor = mdb.rawQuery("Select Max(VALUE) from HIGHEST;",null)
            if(cursor.moveToFirst())
            {
                return cursor.getInt(0)
            }
        }catch (e:SQLiteException)
        {
            return  0
        }
        return  0
        // Select max(Value) from Highest ;

    }

    fun addPlayer(name:String,totalscore:Int,lastscore:Int,history:String): Long? {
        val db = this.writableDatabase
        val v = ContentValues().apply {
            put(NAME,name)
            put(CURRNET_SCORE,0)
            put(TOTLA_GAMES,totalscore)
            put(LAST_GAME,lastscore)
            put(HISTORY,history)
        }
        return  db?.insert(TABLE_NAME_PLAYING,null,v)
    }

    fun getPlayer(): Cursor? {
        val db = this.readableDatabase
        return  db.query(TABLE_NAME_PLAYING,null,null,null,null,null,null)
    }
}
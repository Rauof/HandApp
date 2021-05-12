package com.rauof.hand

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class MySqlHelper(context: Context) : SQLiteOpenHelper(context, "MyDatabase2", null, 2) {

    companion object : BaseColumns {
         val TABLE_NAME_PLAYING:String  = "PLAYING"
         val ID_COLUMN:String  = "_id"
         val NAME:String  = "NAME"
         val CURRNET_SCORE:String  = "Currentscore"
         val LAST_GAME:String  = "LastGame"
         val TOTLA_GAMES:String  = "TotlaGames"
    }

    private  val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${TABLE_NAME_PLAYING} ( ${ID_COLUMN} INTEGER PRIMARY KEY AUTOINCREMENT, ${NAME} TEXT NOT NULL," +
                " ${CURRNET_SCORE} INT NOT NULL,  ${LAST_GAME} INT NOT NULL,${TOTLA_GAMES} INT NOT NULL)"


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

    fun addPlayer(name:String,totalscore:Int,lastscore:Int): Long? {
        val db = this.writableDatabase
        val v = ContentValues().apply {
            put(NAME,name)
            put(CURRNET_SCORE,0)
            put(TOTLA_GAMES,totalscore)
            put(LAST_GAME,lastscore)

        }
        return  db?.insert(TABLE_NAME_PLAYING,null,v)
    }

    fun getPlayer(): Cursor? {
        val db = this.readableDatabase
        return  db.query(TABLE_NAME_PLAYING,null,null,null,null,null,null)
    }

    fun updatePlayer(arr : IntArray){
        var db = this.readableDatabase
        db.update(TABLE_NAME_PLAYING,null,null,null)
    }

    fun deleteAllPlayer(){
        val deletedRows = readableDatabase?.delete(TABLE_NAME_PLAYING, null, null)
        onDowngrade(this.readableDatabase, 2 ,1)
    }

    fun sortPlayersDecreasing(){
        val db = readableDatabase
        val sortOrder = "$TOTLA_GAMES DESC"
        val cursor = db.query(
                TABLE_NAME_PLAYING,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        )
    }

    fun updateData(score:Int , id:Int) {
        val db = writableDatabase
        val contentValues = ContentValues()
        //for(element in scores)
            //contentValues.put(LAST_GAME, element)
        contentValues.put(LAST_GAME, score)
        val WhereArgs = arrayOf("$id")
        val updatedRows = db.update(TABLE_NAME_PLAYING,contentValues, ID_COLUMN+" =?" ,WhereArgs)

    }

}
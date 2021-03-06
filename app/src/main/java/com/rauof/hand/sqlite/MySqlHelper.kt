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
         val TOTLA_GAMES:String  = "TotalGames"
         val HISTORY:String = "History"
        val Num_OF_TIMES_PLAYED:String = "NumOfTimesPlayed"
    }

    private  val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${TABLE_NAME_PLAYING} ( ${ID_COLUMN} INTEGER PRIMARY KEY AUTOINCREMENT, ${NAME} TEXT NOT NULL," +
                " ${CURRNET_SCORE} INT NOT NULL, ${Num_OF_TIMES_PLAYED} INT NOT NULL,  ${LAST_GAME} INT NOT NULL,${TOTLA_GAMES} INT NOT NULL, ${HISTORY} TEXT NOT NULL)"


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

    fun addPlayer(name:String,totalscore:Int,lastscore:Int,numOfTimesPlayed:Int): Long? {
        val db = this.writableDatabase
        val v = ContentValues().apply {
            put(NAME,name)
            put(CURRNET_SCORE,0)
            put(TOTLA_GAMES,totalscore)
            put(LAST_GAME,lastscore)
            put(HISTORY,"New Player! + \n")
            put(Num_OF_TIMES_PLAYED,numOfTimesPlayed)

        }
        return  db?.insert(TABLE_NAME_PLAYING,null,v)
    }

    fun getPlayer(): Cursor? {
        val db = this.readableDatabase
        return  db.query(TABLE_NAME_PLAYING,null,null,null,null,null,null)
    }

    fun getPlayerForHistory(): Cursor? {
        val db = this.readableDatabase
        var colmuns = arrayOf("$NAME" , "$HISTORY")
        return  db.query(TABLE_NAME_PLAYING,colmuns,null,null,null,null,null)
    }

    fun deleteAllPlayer(){
         readableDatabase?.delete(TABLE_NAME_PLAYING, null, null)
        onDowngrade(this.readableDatabase, 2 ,1)
    }

    fun sortPlayersIncreasing():Cursor{
        val db = readableDatabase
        val sortOrder = "$TOTLA_GAMES ASC"
        val cursor = db.query(
                TABLE_NAME_PLAYING,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        )
        return cursor
    }

    fun updateLastGame(score:Int ,name: String) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(LAST_GAME, score)
        val WhereArgs = arrayOf("$name")
        db.update(TABLE_NAME_PLAYING,contentValues, NAME+" =?" ,WhereArgs)
    }

    fun updateNumOfTimesPlayed(num:Int , name: String) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Num_OF_TIMES_PLAYED, num)
        val WhereArgs = arrayOf("$name")
         db.update(TABLE_NAME_PLAYING,contentValues, NAME+" =?" ,WhereArgs)
    }

    fun updateTotalGame(totalScores: Int ,name: String) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TOTLA_GAMES, totalScores)
        val whereArgs = arrayOf("$name")
        db.update(TABLE_NAME_PLAYING,contentValues, NAME+" =?" ,whereArgs)

    }

    fun updateHistory(history: String  ,name: String) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(HISTORY, history)
        val whereArgs = arrayOf("$name")
        db.update(TABLE_NAME_PLAYING,contentValues, NAME+" =?" ,whereArgs)

    }

    fun getTotalGameScores(sizeOfList:Int): IntArray{
        var x = IntArray(sizeOfList)
        val db =  writableDatabase
        val columns = arrayOf(LAST_GAME)
        val cursor: Cursor = db.query(TABLE_NAME_PLAYING,columns,null,null,null,null,null)
        var i =0
        while (cursor.moveToNext()){
            val index1 =  cursor.getColumnIndex(LAST_GAME)
            x[i] = cursor.getInt(index1)
            i++
        }
        return x
    }

    fun getNumOfTimesPlayed(sizeOfList: Int): IntArray {
        var x = IntArray(sizeOfList)
        val db =  writableDatabase
        val columns = arrayOf(Num_OF_TIMES_PLAYED)
        val cursor: Cursor = db.query(TABLE_NAME_PLAYING,columns,null,null,null,null,null)
        var i =0
        while (cursor.moveToNext()){
            val index1 = cursor.getColumnIndex(Num_OF_TIMES_PLAYED)
            x[i] = cursor.getInt(index1)
            i++
        }
        return x
    }

    fun getNames(): ArrayList<String> {
        var x = ArrayList<String>()
        val db =  writableDatabase
        val columns = arrayOf(NAME)
        val cursor: Cursor = db.query(TABLE_NAME_PLAYING,columns,null,null,null,null,null)
        var i =0
        while (cursor.moveToNext()){
            val index1 = cursor.getColumnIndex(NAME)
            x.add(cursor.getString(index1))
            i++
        }
        return x
    }

    fun getHistory(): ArrayList<String> {
        var x = ArrayList<String>()
        val db =  writableDatabase
        val columns = arrayOf(HISTORY)
        val cursor: Cursor = db.query(TABLE_NAME_PLAYING,columns,null,null,null,null,null)
        var i =0
        while (cursor.moveToNext()){
            val index1 = cursor.getColumnIndex(HISTORY)
            x.add(cursor.getString(index1))
            i++
        }
        return x
    }

    fun deleteAllHistories(name: String) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(HISTORY, "New Player")
        val whereArgs = arrayOf("$name")
        db.update(TABLE_NAME_PLAYING,contentValues, NAME+" =?" ,whereArgs)
    }

}
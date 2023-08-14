package com.frhanklin.disastory.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.frhanklin.disastory.data.local.dao.DisasterDao
import com.frhanklin.disastory.data.local.entity.DisasterModel

@Database(
    entities = [DisasterModel::class],
    version = 1
)
abstract class DisasterDatabase: RoomDatabase() {

    abstract fun disasterDao(): DisasterDao

    companion object {
        @Volatile
        private var INSTANCE: DisasterDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): DisasterDatabase {
            if (INSTANCE == null) {
                synchronized(DisasterDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DisasterDatabase::class.java,
                        "disaster_db"
                    ).build()
                }
            }
            return INSTANCE as DisasterDatabase
        }

    }

}
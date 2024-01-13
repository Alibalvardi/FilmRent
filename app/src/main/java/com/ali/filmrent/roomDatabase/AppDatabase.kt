package com.ali.filmrent.roomDatabase

import android.content.Context
import androidx.room.*
import com.ali.filmrent.dataClass.*

@Database(
    version = 1,
    exportSchema = false,
    entities = [Customer::class, Film::class, Manager::class, Store::class, Actor::class, BuyedInventory::class, Category::class, Language::class, Payment::class, Rental::class, RentedFilm::class, ImgStore::class]
)
@TypeConverters(FilmTypeConverters1::class, FilmTypeConverters2::class, FilmTypeConverters3::class)

abstract class AppDatabase : RoomDatabase() {
    abstract val customerDao: CustomerDao
    abstract val filmDao: FilmDao
    abstract val managerDao: ManagerDao
    abstract val storeDao: StoreDao
    abstract val actorDao: ActorDao
    abstract val buyedInventoryDao: BuyedInventoryDao
    abstract val categoryDao: CategoryDao
    abstract val languageDao: LanguageDao
    abstract val paymentDao: PaymentDao
    abstract val rentalDao: RentalDao
    abstract val rentedFilmDao: RentedFilmDao
    abstract val imgStoreDao: ImgStoreDao

    companion object {
        private var database: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (database == null) {
                database = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "FoodDatabase.db"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return database!!
        }

    }

}
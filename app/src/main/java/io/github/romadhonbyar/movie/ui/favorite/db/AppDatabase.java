package io.github.romadhonbyar.movie.ui.favorite.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Fav.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavDAO favDAO();
}
package com.example.mykampuslite.data;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.mykampuslite.model.Dosen;
import com.example.mykampuslite.model.Mahasiswa;

// Mendeklarasikan 2 Entity untuk dijadikan tabel
@Database(entities = {Mahasiswa.class, Dosen.class}, version = 1, exportSchema = false)
public abstract class MyKampusDatabase extends RoomDatabase {

    public abstract AkademikDao akademikDao();

    // Singleton Pattern
    private static volatile MyKampusDatabase INSTANCE;

    public static synchronized MyKampusDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MyKampusDatabase.class,
                    "mykampus_db"
            ).build();
        }
        return INSTANCE;
    }
}

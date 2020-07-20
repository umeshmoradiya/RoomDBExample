package haraye.com.shraddhatestappjava.db;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import haraye.com.shraddhatestappjava.model.AttachmentModel;
import haraye.com.shraddhatestappjava.model.DataModel;

@Database(entities = {DataModel.class, AttachmentModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DataDao dataDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "data_databases")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
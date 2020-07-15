package haraye.com.shraddhatestappjava.repository;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;
import haraye.com.shraddhatestappjava.db.AppDatabase;
import haraye.com.shraddhatestappjava.db.DataDao;
import haraye.com.shraddhatestappjava.model.DataModel;

public class DataRepository {

    private DataDao mDataDao;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public DataRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mDataDao = db.dataDao();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public List<DataModel> getAllData() {
        return mDataDao.getAllData();
    }

    public List<DataModel> getDataById(int dataId) {
        return mDataDao.loadAllByIds(new int[]{dataId});
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(DataModel dataModel) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mDataDao.insert(dataModel);
        });
    }

    public void update(DataModel dataModel) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mDataDao.update(dataModel);
        });
    }

    public void delete(DataModel dataModel){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mDataDao.delete(dataModel);
        });
    }
}
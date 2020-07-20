package haraye.com.shraddhatestappjava.repository;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import haraye.com.shraddhatestappjava.db.AppDatabase;
import haraye.com.shraddhatestappjava.db.DataDao;
import haraye.com.shraddhatestappjava.model.AttachmentModel;
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

        List<DataModel> data = mDataDao.getAllData();

        for(DataModel dataModel: data){
            List<AttachmentModel> attachmentModels = getAttachmentDataById(dataModel.dataid);
            for(AttachmentModel attachmentModel: attachmentModels){
                Log.e("Image File Path:", "" + attachmentModel.type +"" + attachmentModel.source);
                if("image".equals(attachmentModel.type)) {
                    if(dataModel.image == null)
                        dataModel.image = new ArrayList<>();

                    dataModel.image.add(attachmentModel.source);
                }
                else {
                    if(dataModel.audio == null)
                        dataModel.audio = new ArrayList<>();

                    dataModel.audio.add(attachmentModel.source);
                }
            }
        }
        return data;
    }

    public List<AttachmentModel> getAllAttachmentData() {
        return mDataDao.getAllAttachmentData();
    }


    public List<DataModel> getDataById(int dataId) {
        return mDataDao.loadAllByIds(new int[]{dataId});
    }

    public List<AttachmentModel> getAttachmentDataById(int dataModelId) {
        return mDataDao.loadAllAttachmentByIds(new int[]{dataModelId});
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(DataModel dataModel, InsertListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
           long insertedId = mDataDao.insert(dataModel);
           listener.onSuccess(insertedId);
        });
    }

    public void insert(AttachmentModel attachmentModel) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mDataDao.insertAttachment(attachmentModel);
        });
    }


    public void update(DataModel dataModel) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mDataDao.update(dataModel);
        });
    }

    public void update(AttachmentModel attachmentModel) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mDataDao.update(attachmentModel);
        });
    }

    public void delete(DataModel dataModel){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mDataDao.delete(dataModel);
        });
    }
    public void delete(AttachmentModel attachmentModel){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mDataDao.delete(attachmentModel);
        });
    }

    public interface InsertListener{
        void onSuccess(long id);
    }
}
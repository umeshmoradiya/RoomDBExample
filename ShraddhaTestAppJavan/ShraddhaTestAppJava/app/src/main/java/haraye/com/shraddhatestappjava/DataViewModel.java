package haraye.com.shraddhatestappjava;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;

import haraye.com.shraddhatestappjava.model.AttachmentModel;
import haraye.com.shraddhatestappjava.model.DataModel;
import haraye.com.shraddhatestappjava.repository.DataRepository;

public class DataViewModel extends AndroidViewModel {

    private DataRepository mRepository;

    public DataViewModel(Application application) {
        super(application);
        mRepository = new DataRepository(application);
    }

    public void insert(DataModel dataModel, DataRepository.InsertListener listener) {
        mRepository.insert(dataModel, listener);
    }

    public void insert(AttachmentModel attachmentModel){
        mRepository.insert(attachmentModel);
    }

    public List<DataModel> getDataById(int id) {
        return mRepository.getDataById(id);
    }

    public void update(DataModel dataModel) {
        mRepository.update(dataModel);
    }
}

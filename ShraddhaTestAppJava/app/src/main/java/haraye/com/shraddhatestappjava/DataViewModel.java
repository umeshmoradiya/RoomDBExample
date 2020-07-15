package haraye.com.shraddhatestappjava;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import haraye.com.shraddhatestappjava.model.DataModel;
import haraye.com.shraddhatestappjava.repository.DataRepository;

public class DataViewModel extends AndroidViewModel {

    private DataRepository mRepository;

    public DataViewModel(Application application) {
        super(application);
        mRepository = new DataRepository(application);
    }

    public void insert(DataModel dataModel) {
        mRepository.insert(dataModel);
    }

    public List<DataModel> getDataById(int id) {
        return mRepository.getDataById(id);
    }

    public void update(DataModel dataModel) {
        mRepository.update(dataModel);
    }
}

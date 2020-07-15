package haraye.com.shraddhatestappjava;


import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import haraye.com.shraddhatestappjava.model.DataModel;
import haraye.com.shraddhatestappjava.repository.DataRepository;

public class MainViewModel extends AndroidViewModel {

    private DataRepository mRepository;

    public MainViewModel (Application application) {
        super(application);
        mRepository = new DataRepository(application);
    }

    List<DataModel> getAllData() { return mRepository.getAllData(); }

    void delete(DataModel dataModel){
        mRepository.delete(dataModel);
    }
}
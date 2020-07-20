package haraye.com.shraddhatestappjava.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import haraye.com.shraddhatestappjava.model.AttachmentModel;
import haraye.com.shraddhatestappjava.model.DataModel;

@Dao
public interface DataDao {
    @Query("SELECT * FROM datamodel")
    List<DataModel> getAllData();

    @Query("SELECT * FROM datamodel WHERE dataid IN (:dataIds)")
    List<DataModel> loadAllByIds(int[] dataIds);

    @Query("SELECT * FROM datamodel WHERE title LIKE :title LIMIT 1")
    DataModel findByTile(String title);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(DataModel dataModel);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(DataModel... dataModels);

    @Delete
    void delete(DataModel dataModel);

    @Update
    void update(DataModel dataModel);

    @Query("SELECT * FROM attachment")
    List<AttachmentModel> getAllAttachmentData();

    @Query("SELECT * FROM attachment WHERE data_model_id IN (:dataModelIds)")
    List<AttachmentModel> loadAllAttachmentByIds(int[] dataModelIds);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAttachment(AttachmentModel attachmentModel);

    @Delete
    void delete(AttachmentModel attachmentModel);

    @Update
    void update(AttachmentModel attachmentModel);

}
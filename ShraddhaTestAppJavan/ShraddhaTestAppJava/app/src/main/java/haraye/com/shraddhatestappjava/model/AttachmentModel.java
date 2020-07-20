package haraye.com.shraddhatestappjava.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "attachment")
public class AttachmentModel {

    @PrimaryKey(autoGenerate = true)
    public int attachment_id;

    @ColumnInfo(name = "source")
    public String source;

    @ColumnInfo(name = "data_model_id")
    public long data_model_id;

    @ColumnInfo(name = "type")
    public String type;
}

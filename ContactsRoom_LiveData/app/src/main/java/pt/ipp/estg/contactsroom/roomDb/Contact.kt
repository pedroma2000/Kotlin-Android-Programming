package pt.ipp.estg.contactsroom.roomDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "contact_name") val name :String,
    @ColumnInfo(name = "contact_number") val number:String
)

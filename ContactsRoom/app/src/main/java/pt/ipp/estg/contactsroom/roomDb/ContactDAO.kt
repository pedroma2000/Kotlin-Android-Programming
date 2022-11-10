package pt.ipp.estg.contactsroom.roomDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactDAO {
    @Query("SELECT * FROM contact")
    fun getAll():List<Contact>

    @Insert
    fun addContact(contact: Contact)

    @Delete
    fun deleteContact(contact: Contact)
}
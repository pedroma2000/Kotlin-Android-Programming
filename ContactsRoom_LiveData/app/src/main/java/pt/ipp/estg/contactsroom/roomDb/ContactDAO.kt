package pt.ipp.estg.contactsroom.roomDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactDAO {
    @Query("SELECT * FROM contact")
    fun getAll():LiveData<List<Contact>>

    @Query("SELECT * FROM contact WHERE id = :contact_id")
    fun getContact(contact_id:Int): LiveData<Contact>

    @Insert
    fun addContact(contact: Contact)

    @Delete
    fun deleteContact(contact: Contact)
}
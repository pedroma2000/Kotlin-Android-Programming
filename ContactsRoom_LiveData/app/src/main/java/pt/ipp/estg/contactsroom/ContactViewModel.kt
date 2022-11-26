package pt.ipp.estg.contactsroom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipp.estg.contactsroom.roomDb.Contact
import pt.ipp.estg.contactsroom.roomDb.ContactRepository
import pt.ipp.estg.contactsroom.roomDb.ContactRoomDatabase

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    val repository: ContactRepository
    val allContacts: LiveData<List<Contact>>

    init {
        val db =ContactRoomDatabase.getDatabase(application)
        repository = ContactRepository(db.contactDao())
        allContacts = repository.getAllContacts()
    }

    fun insertContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO){
            repository.insertContact(contact)
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteContact(contact)
        }
    }

    fun getContact(id: Int) {
        viewModelScope.launch(Dispatchers.IO){
            repository.getContact(id)
        }
    }
}
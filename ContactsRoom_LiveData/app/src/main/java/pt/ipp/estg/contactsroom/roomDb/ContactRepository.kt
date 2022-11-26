package pt.ipp.estg.contactsroom.roomDb

import androidx.lifecycle.LiveData

class ContactRepository(val contactDAO: ContactDAO) {

    fun getAllContacts(): LiveData<List<Contact>> {
        return contactDAO.getAll()
    }

    fun getContact(id: Int): LiveData<Contact> {
        return contactDAO.getContact(id)
    }

    fun insertContact(contact: Contact) {
        return contactDAO.addContact(contact)
    }

    fun deleteContact(contact: Contact) {
        return contactDAO.deleteContact(contact)
    }
}
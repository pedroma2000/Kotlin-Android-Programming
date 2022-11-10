package pt.ipp.estg.contactsroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.contactsroom.roomDb.Contact
import pt.ipp.estg.contactsroom.roomDb.ContactRoomDatabase
import pt.ipp.estg.contactsroom.ui.theme.ContactsRoomTheme
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val databaseExecutor:ExecutorService = Executors.newFixedThreadPool(2)

class MainActivity : ComponentActivity() {
    val databaseService:ExecutorService = Executors.newFixedThreadPool(2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactsRoomTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "contactsList"
    ) {
        composable("contactsList") {
            ContactsList(
                navToAddContent =  {
                    navController.navigate("addContact")
                    {
                        popUpTo("contactsList")
                    }
                })
        }
        composable("addContact") {
            AddContactScreen(
                navBackToList = {
                    navController.navigate("contactsList")
                })
        }
    }
}

@Composable
fun ContactsList(navToAddContent:()->Unit) {
    val context = LocalContext.current
    var contactList:MutableList<Contact> by remember { mutableStateOf(mutableListOf()) }
    val scaffoldState = rememberScaffoldState()

    databaseExecutor.execute(Runnable {
        contactList = ContactRoomDatabase.getDatabase(context).contactDao().getAll() as MutableList<Contact>
    })

    fun deleteContact(contact: Contact) {
        databaseExecutor.execute(Runnable {
            ContactRoomDatabase.getDatabase(context).contactDao().deleteContact(contact)
            contactList = ContactRoomDatabase.getDatabase(context).contactDao().getAll() as MutableList<Contact>
        })
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Lista de Contactos") },
                actions = {
                    IconButton(onClick = navToAddContent) {
                        Icon(Icons.Filled.Add, contentDescription = "Add contact")
                    }
                }
            )
        },
        content = {
            if (contactList.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Lista de contactos vazia")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(contactList) { contact ->
                        ContactRow(contact, deleteContact = {deleteContact(contact)})
                    }
                }
            }
        }
    )
}

@Composable
fun ContactRow(contact: Contact, deleteContact:()->Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(contact.name, fontSize = 20.sp)
            Text(contact.number, fontSize = 16.sp)
        }
        Button(
            onClick = deleteContact,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.DarkGray)
        ) {
            Text(text = "DELETE")
        }
    }
}

@Composable
fun AddContactScreen(navBackToList:()->Unit) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    var name_input by remember { mutableStateOf(TextFieldValue("")) }
    var number_input by remember { mutableStateOf(TextFieldValue("")) }

    val insertContact = {
        databaseExecutor.execute(Runnable {
            ContactRoomDatabase.getDatabase(context).contactDao().addContact(Contact(name = name_input.text, number = number_input.text))
        })
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Inserir Contacto") },
                navigationIcon = {
                    IconButton(onClick = navBackToList) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back to Contact List")
                    }
                }
            )
        },
        content = {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 5.dp),
                    value = name_input,
                    onValueChange = { name_input = it }
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 5.dp),
                    value = number_input,
                    onValueChange = { number_input = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 5.dp),
                    onClick = {
                        insertContact()
                        navBackToList()
                    }
                ) {
                    Text(text = "Inserir Contacto")
                }
            }
        }
    )
}
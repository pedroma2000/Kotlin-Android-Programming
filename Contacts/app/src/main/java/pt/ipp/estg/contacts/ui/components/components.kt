package pt.ipp.estg.contacts.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import pt.ipp.estg.contacts.R
import pt.ipp.estg.contacts.models.Contact

@Composable
fun MainActivityComp(contact: Contact) {
    Column() {
        TopLabel(name = contact.name)
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            ContactsList(mobilePhone = contact.mobile_phone, workPhone = contact.work_phone)
            EmailsList(personalEmail = contact.personal_email, workEmail = contact.work_email)
        }
    }
}


@Composable
fun TopLabel(name:String) {
    Box() {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            painter = painterResource(id = R.drawable.contact_image),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            colorFilter = ColorFilter.lighting(Color.Gray,Color.Black)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(130.dp)
        ) {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                elevation = 0.dp,
                backgroundColor = Color.Transparent,
                title = { Text(text = "")},
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Edit, "editIcon")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.MoreVert, "moreVertIcon")
                    }
                }
            )
            Text(
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(start = 75.dp),
                color = Color.White,
                fontSize = 30.sp,
                text = name
            )
        }
    }
}

@Composable
fun ContactsList(mobilePhone:String, workPhone:String) {
    Row(modifier = Modifier
        .fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .width(75.dp)
            .padding(top = 10.dp, start = 15.dp)) {
            Icon(Icons.Filled.Call, "callIcon", tint = Color.Blue)
        }
        Column(modifier = Modifier
            .fillMaxWidth()
        ) {
            InformationLine(value = mobilePhone, label = "Mobile")
            InformationLine(value = workPhone, label = "Work")

        }
    }
}

@Composable
fun EmailsList(personalEmail:String, workEmail:String) {
    Row(modifier = Modifier
        .fillMaxSize()
    ) {
        Column(modifier = Modifier
            .width(75.dp)
            .padding(top = 10.dp, start = 15.dp) ) {
            Icon(Icons.Filled.Email, "emailIcon", tint = Color.Blue)
        }
        Column(modifier = Modifier
            .fillMaxWidth()
        ) {
            InformationLine(value = personalEmail, label = "Personal", isEmail = true)
            InformationLine(value = workEmail, label = "Work", isEmail = true)

        }
    }
}

fun makeACall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel: $phoneNumber"))
    startActivity(context, intent, bundleOf())
}



@Composable
fun InformationLine(value:String, label:String, isEmail: Boolean = false) {
    val context = LocalContext.current
    fun handleClick() {
        if(!isEmail) {
            makeACall(context, value)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.clickable { handleClick() }) {
            Text(text = value, fontWeight = FontWeight.W600, fontSize = 18.sp , color = Color.DarkGray, fontFamily = FontFamily.SansSerif)
            Text(text = label, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.W400, color = Color.Gray)
        }
        if(!isEmail) {
            IconButton( modifier = Modifier.padding(end = 10.dp) ,onClick = { /*TODO*/ }) {
                Image(painter = painterResource(id = R.drawable.ic_baseline_message_24), contentDescription = "messageIcon")
            }
        }
    }
}

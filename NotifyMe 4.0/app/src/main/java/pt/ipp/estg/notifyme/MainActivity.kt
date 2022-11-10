package pt.ipp.estg.notifyme

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import pt.ipp.estg.notifyme.ui.theme.NotifyMeTheme

val CHANNEL_ID = "1"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotifyMeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(notify = {
                        with(NotificationManagerCompat.from(this)) {
                            /**
                             * notificationId is a unique int for each notification that you must define
                             */
                            notify(1, builder.build())
                        }
                    },
                    cancelNotification = {
                        with(NotificationManagerCompat.from(this)) {
                            cancel(1)
                        }
                    },
                    updateNotification = {
                        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_banner_foreground)

                        var updatedNotification = NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_baseline_android_24)
                            .setContentTitle("Título Alterado")
                            .setContentText("Este é o texto da notificação")
                            .setLargeIcon(bitmap)
                            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                            .setAutoCancel(true)

                        with(NotificationManagerCompat.from(this)) {
                            notify(1, updatedNotification.build())
                        }
                    })
                }
            }
        }
    }

    var builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_baseline_android_24)
        .setContentTitle("Foi Notificado!")
        .setContentText("Este é o texto da notificação")
        .setAutoCancel(true)

    private fun createNotificationChannel() {
        /**
         * Create the NotificationChannel, but only on API 26+ because
         * the NotificationChannel class is new and not in the support library
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            /**
             * Register the channel with the system
             */
            val notificationManager:NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@Composable
fun MainScreen(notify:()->Unit, cancelNotification:()->Unit, updateNotification:()->Unit) {
    var isNotificationEnable by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize())
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                notify()
                isNotificationEnable = true
            }) {
                Text(text = "Notify Me!")
            }
            Button(
                modifier = Modifier.padding(top = 50.dp),
                enabled = isNotificationEnable,
                onClick = {
                    updateNotification()
                }) {
                Text(text = "Update Me!")
            }
            Button(
                modifier = Modifier.padding(top = 50.dp),
                enabled = isNotificationEnable,
                onClick = {
                    cancelNotification()
                }) {
                Text(text = "Cancel Me!")
            }
        }
    }
}
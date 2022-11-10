package pt.ipp.estg.notifyme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationBroadcastReceive: BroadcastReceiver() {

    var action = {}

    override fun onReceive(context: Context, intent: Intent) {
        /**
         * If intent.action the function simply returns
         */
        var _action = intent.action

        if(_action.equals("NOTIFICATION_CANCELLED")) {
            action()
        } else {
            return
        }
    }
}
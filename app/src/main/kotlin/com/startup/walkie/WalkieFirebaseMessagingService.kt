package com.startup.walkie

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.startup.common.util.Printer

class WalkieFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Printer.d("WalkieFirebaseMessagingService", message.data.entries.joinToString())
        super.onMessageReceived(message)
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}
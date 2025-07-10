package com.example.newsapp.utils.cloud

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class CloudMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        // TODO: Send token to server
        // TODO: Store token Locally
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun getStartCommandIntent(originalIntent: Intent?): Intent? {
        return super.getStartCommandIntent(originalIntent)
    }

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
    }
}

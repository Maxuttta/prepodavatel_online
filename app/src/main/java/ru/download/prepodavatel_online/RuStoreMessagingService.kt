package ru.download.prepodavatel_online

import android.net.Uri
import com.vk.push.common.messaging.ClickActionType
import ru.rustore.sdk.pushclient.messaging.exception.RuStorePushClientException
import ru.rustore.sdk.pushclient.messaging.model.RemoteMessage
import ru.rustore.sdk.pushclient.messaging.service.RuStoreMessagingService

class MessagingService: RuStoreMessagingService() {

    override fun onNewToken(token: String) {
    }

    override fun onMessageReceived(message: RemoteMessage) {
    }

    override fun onDeletedMessages() {
    }

    override fun onError(errors: List<RuStorePushClientException>) {
    }
}
public data class Notification(
    val title: String?,
    val body: String?,
    val channelId: String?,
    val imageUrl: Uri?,
    val color: String?,
    val icon: String?,
    val clickAction: String?,
    val clickActionType: ClickActionType?
)
enum class ClickActionType {
    DEFAULT,
    DEEP_LINK
}
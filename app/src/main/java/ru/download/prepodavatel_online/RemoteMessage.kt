package ru.download.prepodavatel_online

import ru.rustore.sdk.pushclient.messaging.model.Notification

public data class RemoteMessage(
    val messageId: String?,
    val priority: Int,
    val ttl: Int,
    val from: String,
    val collapseKey: String?,
    val data: Map<String, String>,
    val rawData: ByteArray?,
    val notification: Notification?
)
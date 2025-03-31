package ru.download.prepodavatel_online

import android.content.Context
import com.vk.push.common.clientid.ClientId
import com.vk.push.common.clientid.ClientIdCallback
import com.vk.push.common.clientid.ClientIdType
import ru.rustore.sdk.pushclient.common.logger.DefaultLogger
import ru.rustore.sdk.pushclient.common.logger.Logger
import ru.rustore.sdk.pushclient.provider.AbstractRuStorePushClientParams

class RuStorePushClientParams(context: Context)
    : AbstractRuStorePushClientParams(context) {

    override fun getLogger(): Logger = DefaultLogger("your_tag")

    override fun getTestModeEnabled(): Boolean = false

    override fun getClientIdCallback(): ClientIdCallback =
        ClientIdCallback { ClientId("your_gaid_or_oaid", ClientIdType.GAID) }
}
interface Logger {
    fun verbose(message: String, throwable: Throwable? = null)
    fun debug(message: String, throwable: Throwable? = null)
    fun info(message: String, throwable: Throwable? = null)
    fun warn(message: String, throwable: Throwable? = null)
    fun error(message: String, throwable: Throwable? = null)

    fun createLogger(tag: String): Logger
}
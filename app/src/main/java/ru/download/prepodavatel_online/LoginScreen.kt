package ru.download.prepodavatel_online

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.xml.OneTap

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val oneTap: OneTap = findViewById(R.id.oneTap)
        oneTap.setCallbacks(onAuth = { oAuth, accessToken ->
            val sharedPref = getSharedPreferences("USER", MODE_PRIVATE).edit()
            sharedPref.putString("phone_number", accessToken.userData.phone).apply()
            sharedPref.putString("first_name", accessToken.userData.firstName).apply()
            sharedPref.putString("last_name", accessToken.userData.lastName).apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // Добавление анимации
            finish()
        }, onFail = { oAuth, fail ->
            when (fail) {
                is VKIDAuthFail.Canceled -> {
                    Snackbar.make(
                        findViewById(R.id.main),
                        "Авторизация отклонена!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                is VKIDAuthFail.NoBrowserAvailable -> {
                    Snackbar.make(
                        findViewById(R.id.main),
                        "Отсутствует браузер!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                else -> {
                    Snackbar.make(
                        findViewById(R.id.main),
                        "Ошибка. Попробуйте ещё раз!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}
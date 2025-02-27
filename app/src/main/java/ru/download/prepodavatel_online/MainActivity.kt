package ru.download.prepodavatel_online

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.squareup.picasso.Picasso
import com.vk.id.VKID

class MainActivity : AppCompatActivity() {
    private val profileImage by lazy { findViewById<ImageView>(R.id.profileImage) }
    private val imageProfile by lazy { VKID.instance.accessToken?.userData?.photo200 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val chipNavigationBar = findViewById<ChipNavigationBar>(R.id.bottom_menu)
        chipNavigationBar.setItemSelected(R.id.home, true)
        laodPrepodData()
    }

    private fun laodPrepodData() {
        Picasso.get().load(imageProfile).into(profileImage)
    }
}
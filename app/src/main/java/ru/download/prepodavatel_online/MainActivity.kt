package ru.download.prepodavatel_online

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.squareup.picasso.Picasso
import com.vk.id.VKID

class MainActivity : AppCompatActivity() {
    private val profileImage by lazy { findViewById<ImageView>(R.id.profileImage) }

    private lateinit var chipNavigationBar: ChipNavigationBar
    private lateinit var Home: ConstraintLayout
    private lateinit var Settings: ConstraintLayout

    private lateinit var anim1: Animation
    private lateinit var anim2: Animation
    private lateinit var anim3: Animation
    private lateinit var anim4: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Home = findViewById(R.id.homeScreen)
        Settings = findViewById(R.id.settingsScreen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        anim1 = AnimationUtils.loadAnimation(this, R.anim.to_settings_top)
        anim2 = AnimationUtils.loadAnimation(this, R.anim.to_settings_bottom)
        anim3 = AnimationUtils.loadAnimation(this, R.anim.to_home_top)
        anim4 = AnimationUtils.loadAnimation(this, R.anim.to_home_bottom)

        chipNavigationBar = findViewById(R.id.bottom_menu)
        chipNavigationBar.setItemSelected(R.id.home, true)
        loadPrepodData()

        goSettings()
    }

    private fun goSettings() {
        chipNavigationBar = findViewById(R.id.bottom_menu)

        chipNavigationBar.setOnItemSelectedListener { id ->
            val selectedItemId = chipNavigationBar.getSelectedItemId()
            when(selectedItemId){
                R.id.home -> {
                    Home.startAnimation(anim3)
                    Settings.startAnimation(anim4)
                    Home.visibility = View.VISIBLE
                    Settings.visibility = View.GONE
                }
                R.id.search -> {
                    Home.startAnimation(anim1)
                    Settings.startAnimation(anim2)
                    Home.visibility = View.GONE
                    Settings.visibility = View.VISIBLE
                }
                R.id.profile -> {

                }
            }
        }
    }

    private fun loadPrepodData() {
        if(VKID.instance.accessToken?.idToken != null)
            Picasso.get().load(VKID.instance.accessToken?.userData?.photo200).into(profileImage)
    }
}
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
    private val profileImageInMain by lazy {
        findViewById<ImageView>(R.id.profileImage)
    }
    private val profileImageInAccount by lazy {
        findViewById<ImageView>(R.id.avaIcon)
    }

    private lateinit var chipNavigationBar: ChipNavigationBar
    private lateinit var Home: ConstraintLayout
    private lateinit var Settings: ConstraintLayout
    private lateinit var Profile: ConstraintLayout

    private var screen = 1

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
        Profile = findViewById(R.id.profileScreen)

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

        goScreens()
    }

    private fun goScreens() {
        chipNavigationBar = findViewById(R.id.bottom_menu)

        chipNavigationBar.setOnItemSelectedListener { id ->
            val selectedItemId = chipNavigationBar.getSelectedItemId()
            when(selectedItemId){
                R.id.home -> {
                    if (screen == 2){
                        Home.startAnimation(anim3)
                        Settings.startAnimation(anim4)
                        Home.visibility = View.VISIBLE
                        Settings.visibility = View.GONE
                        Profile.visibility = View.GONE
                        screen = 1
                    }else if(screen == 3){
                        Home.startAnimation(anim3)
                        Profile.startAnimation(anim4)
                        Home.visibility = View.VISIBLE
                        Settings.visibility = View.GONE
                        Profile.visibility = View.GONE
                        screen = 1
                    }
                }
                R.id.search -> {
                    if (screen == 1){
                        Home.startAnimation(anim1)
                        Settings.startAnimation(anim2)
                        Home.visibility = View.GONE
                        Settings.visibility = View.VISIBLE
                        Profile.visibility = View.GONE
                        screen = 2
                    }else if(screen == 3){
                        Settings.startAnimation(anim3)
                        Profile.startAnimation(anim4)
                        Home.visibility = View.GONE
                        Settings.visibility = View.VISIBLE
                        Profile.visibility = View.GONE
                        screen = 2
                    }
                }
                R.id.profile -> {
                    if (screen == 1){
                        Home.startAnimation(anim1)
                        Profile.startAnimation(anim2)
                        Home.visibility = View.GONE
                        Settings.visibility = View.GONE
                        Profile.visibility = View.VISIBLE
                        screen = 3
                    }else if(screen == 2){
                        Settings.startAnimation(anim1)
                        Profile.startAnimation(anim2)
                        Home.visibility = View.GONE
                        Settings.visibility = View.GONE
                        Profile.visibility = View.VISIBLE
                        screen = 3
                    }
                }
            }
        }
    }

    private fun loadPrepodData() {
        if(VKID.instance.accessToken?.idToken != null) {
            Picasso.get().load(VKID.instance.accessToken?.userData?.photo200).into(profileImageInMain)
            Picasso.get().load(VKID.instance.accessToken?.userData?.photo200).into(profileImageInAccount)
        }
    }
}
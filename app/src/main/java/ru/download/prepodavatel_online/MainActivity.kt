package ru.download.prepodavatel_online

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.squareup.picasso.Picasso
import com.vk.id.VKID
import com.vk.id.VKIDUser
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail

class MainActivity : AppCompatActivity(), CardAdapter.Listener {
    private val profileImageInMain by lazy {
        findViewById<ImageView>(R.id.profileImage)
    }
    private val profileImageInAccount by lazy {
        findViewById<ImageView>(R.id.avaIcon)
    }
    private val name_of_teacher by lazy {
        findViewById<TextView>(R.id.name)
    }
    private val surname_of_teacher by lazy {
        findViewById<TextView>(R.id.lastname)
    }
    private val age_of_teacher by lazy {
        findViewById<TextView>(R.id.age)
    }

    private lateinit var childEventListener: ChildEventListener
    private lateinit var dbRef: DatabaseReference

    private lateinit var chipNavigationBar: ChipNavigationBar
    private lateinit var Home: ConstraintLayout
    private lateinit var Settings: ConstraintLayout
    private lateinit var Profile: ConstraintLayout
    private lateinit var cardRec: RecyclerView
    private lateinit var adapter: CardAdapter


    private var screen = 1

    private lateinit var anim1: Animation
    private lateinit var anim2: Animation
    private lateinit var anim3: Animation
    private lateinit var anim4: Animation

    data class TeacherData(
        val firstName: String? = null,
        val lastName: String? = null,
        val age: Int? = null,
        val experience: Int? = null,
        val category: String = "",
        val description: String = ""
    )

    companion object {
        private var teacherData: TeacherData = TeacherData()
        fun getTeacherData(): TeacherData = teacherData
        fun update(
            firstName : String? = null,
            lastName: String? = null,
            age: Int? = null,
            experience: Int? = null,
            category: String? = null,
            description: String? = null
        ) {
            teacherData = teacherData.copy(
                firstName = firstName ?: teacherData.firstName,
                lastName = lastName ?: teacherData.lastName,
                age = age ?: teacherData.age,
                experience = experience ?: teacherData.experience,
                category = category ?: teacherData.category,
                description = description ?: teacherData.description
            )
        }
    }
    @SuppressLint("MissingInflatedId")
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
        loadCards()
        adapter = CardAdapter(this)
    }

    private fun loadCards() {
        cardRec = findViewById(R.id.cardRec)
        dbRef = FirebaseDatabase.getInstance().getReference("Online").child("Cards")
        childEventListener = AppChildEventListener { snapshot, eventType ->
            when (eventType) {
                1 -> {
                        adapter.updateItem(snapshot.getCardModel())
                    cardRec.scrollToPosition(
                        cardRec.adapter!!.itemCount - 1
                    )
                }

                2 -> {
                    adapter.updateItem(snapshot.getCardModel())
                    cardRec.scrollToPosition(
                        cardRec.adapter!!.itemCount - 1
                    )
                }

                3 -> {
                    adapter.removeItem(snapshot.getCardModel())
                }
            }

        }
        dbRef.addChildEventListener(childEventListener)
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
        val pref = getSharedPreferences("USERDATA", MODE_PRIVATE)

        Picasso.get().load(VKID.instance.accessToken?.userData?.photo200).into(profileImageInMain)
        Picasso.get().load(VKID.instance.accessToken?.userData?.photo200).into(profileImageInAccount)
        name_of_teacher.text = VKID.instance.accessToken?.userData?.firstName
        surname_of_teacher.text = VKID.instance.accessToken?.userData?.lastName
        update(firstName = name_of_teacher.text.toString(), lastName = surname_of_teacher.text.toString())

    }

    override fun onClick(CardData: CardData) {

    }

}
package ru.download.prepodavatel_online

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.squareup.picasso.Picasso
import com.vk.id.VKID
import ru.rustore.sdk.core.feature.model.FeatureAvailabilityResult
import ru.rustore.sdk.pushclient.RuStorePushClient
import ru.rustore.sdk.pushclient.common.logger.DefaultLogger
import ru.rustore.sdk.pushclient.messaging.model.TestNotificationPayload
import ru.rustore.sdk.pushclient.utils.resolveForPush
import java.util.Locale

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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // RuStore Push SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that your app will not show notifications.
        }
    }

    private lateinit var childEventListener: ChildEventListener
    private lateinit var dbRef: DatabaseReference
    private lateinit var dbRef1: DatabaseReference
    private lateinit var dbRef2: DatabaseReference

    private lateinit var chipNavigationBar: ChipNavigationBar
    private lateinit var Home: ConstraintLayout
    private lateinit var Settings: ConstraintLayout
    private lateinit var Profile: ConstraintLayout
    private lateinit var cardRec: RecyclerView
    private lateinit var adapter: CardAdapter

    private lateinit var age: EditText
    private lateinit var tgId: EditText
    private lateinit var exp: EditText
    private lateinit var subject: EditText
    private lateinit var about: EditText
    private lateinit var save: ConstraintLayout
    private val searchText by lazy { findViewById<EditText>(R.id.searchText) }

    private var screen = 1

    private lateinit var anim1: Animation
    private lateinit var anim2: Animation
    private lateinit var anim3: Animation
    private lateinit var anim4: Animation

    private val scientific_articles2 by lazy { findViewById<CardView>(R.id.scientific_articles) }
    private val obraz_videos_card by lazy { findViewById<CardView>(R.id.obraz_videos_card) }
    private val findRep by lazy { findViewById<CardView>(R.id.findRep) }
    private val add_res by lazy { findViewById<CardView>(R.id.add_res) }

    data class TeacherData(
        val firstName: String? = null,
        val lastName: String? = null,
        val age: Int? = null,
        val experience: Int? = null,
        val category: String = "",
        val description: String = ""
    )
    private val main by lazy { findViewById<ConstraintLayout>(R.id.main) }

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

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                // RuStore Push SDK (and your app) can post notifications.
            } else {
                // TODO: Inform user that your app will not show notifications.
            }
        }
        RuStorePushClient.init(
            application = this.application,
            projectId = "xhu6ujxTrGct06a3E7iXQd3t3cxAfrTO",
            testModeEnabled = true
        )
        RuStorePushClient.checkPushAvailability()
            .addOnSuccessListener { result ->
                when (result) {
                    FeatureAvailabilityResult.Available -> {
                        // Process push available
                    }

                    is FeatureAvailabilityResult.Unavailable -> {
                        result.cause.resolveForPush(this)
                    }
                }
            }
            .addOnFailureListener { throwable ->
                // Process error
            }
        RuStorePushClient.getToken()
            .addOnSuccessListener { result ->
                // Process success
            }
            .addOnFailureListener { throwable ->
                // Process error
            }
        val testNotificationPayload = TestNotificationPayload(
            title = "Test notification title",
            body = "Test notification message",
            imgUrl = "some_image_http_url",
            data = mapOf("some_key" to "some_value")
        )

        RuStorePushClient.sendTestNotification(testNotificationPayload)

        Home = findViewById(R.id.homeScreen)
        Settings = findViewById(R.id.settingsScreen)
        Profile = findViewById(R.id.profileScreen)
        age = findViewById(R.id.age)
        tgId = findViewById(R.id.tgId)
        exp = findViewById(R.id.exp)
        subject = findViewById(R.id.subject)
        about = findViewById(R.id.about)
        save = findViewById(R.id.save)

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
        adapter = CardAdapter(this)

        askNotificationPermission()
        loadPrepodData()
        goScreens()
        loadCards()
        updateProfile()
        cardRec.layoutManager = LinearLayoutManager(this@MainActivity)
        cardRec.adapter = adapter
        loadListenersOnButtons()
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level>= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
                // RuStore Push SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun loadListenersOnButtons() {
        scientific_articles2.setOnClickListener {
            val intent = Intent(this, scientific_articles::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
        obraz_videos_card.setOnClickListener {
            val intent = Intent(this, videos_activity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
        findRep.setOnClickListener {
            chipNavigationBar.setItemSelected(R.id.search)
        }
        add_res.setOnClickListener {
            val intent = Intent(this, add_resource::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
    }

    private fun updateProfile() {
        save.setOnClickListener{
            if ((about.text.toString() != "" && exp.text.toString() == "" && subject.text.toString() == "") ||
                (about.text.toString() == "" && exp.text.toString() != "" && subject.text.toString() == "") ||
                (about.text.toString() == "" && exp.text.toString() == "" && subject.text.toString() != "")){
                Toast.makeText(this@MainActivity,"Заполните все поля пункта 'Преподователь'", Toast.LENGTH_SHORT).show()
            }else if (about.text.toString() != "" && exp.text.toString() != "" && subject.text.toString() != ""){
                dbRef = FirebaseDatabase.getInstance().getReference("Online").child("Profiles").child("${VKID.instance.accessToken?.userID}")
                val hashMap = hashMapOf<String, Any>(
                    "name" to "${VKID.instance.accessToken?.userData?.firstName}",
                    "lastname" to "${VKID.instance.accessToken?.userData?.lastName}",
                    "avaUrl" to "${VKID.instance.accessToken?.userData?.photo200}",
                    "vkId" to "${VKID.instance.accessToken?.userID}",
                    "tgId" to "${tgId.text}",
                    "subject" to "${subject.text}",
                    "about" to "${about.text}",
                    "age" to "${age.text}",
                    "exp" to "${exp.text}"
                )
                Snackbar.make(main, "Сохранено!", Snackbar.LENGTH_LONG).show()
                dbRef.updateChildren(hashMap as Map<String, Any>)
                dbRef1 = FirebaseDatabase.getInstance().getReference("Online").child("Cards").child("${VKID.instance.accessToken?.userID}")
                dbRef2 = FirebaseDatabase.getInstance().getReference("Online").child("Teachers").child("${VKID.instance.accessToken?.userID}")
                val hashMap2 = hashMapOf<String, Any>(
                    "name" to "${VKID.instance.accessToken?.userData?.firstName}",
                    "lastname" to "${VKID.instance.accessToken?.userData?.lastName}",
                    "type" to "teacher",
                    "subject" to "${subject.text}",
                    "avaUrl" to "${VKID.instance.accessToken?.userData?.photo200}",
                    "rating" to 3,
                    "vkId" to "${VKID.instance.accessToken?.userID}"
                )
                dbRef1.updateChildren(hashMap2 as Map<String, Any>)
                dbRef2.updateChildren(hashMap2 as Map<String, Any>)

            } else{
                dbRef = FirebaseDatabase.getInstance().getReference("Online").child("Profiles").child("${VKID.instance.accessToken?.userID}")
                val hashMap = hashMapOf<String, Any>(
                    "name" to "${VKID.instance.accessToken?.userData?.firstName}",
                    "lastname" to "${VKID.instance.accessToken?.userData?.lastName}",
                    "avaUrl" to "${VKID.instance.accessToken?.userData?.photo200}",
                    "vkId" to "${VKID.instance.accessToken?.userID}",
                    "tgId" to "${tgId.text}",
                    "subject" to "${subject.text}",
                    "about" to "${about.text}",
                    "age" to "${age.text}",
                    "exp" to "${exp.text}"
                )
                Toast.makeText(this@MainActivity,"Сохранено", Toast.LENGTH_SHORT).show()
                dbRef.updateChildren(hashMap as Map<String, Any>)
            }
        }


    }

    private fun loadCards() {
        cardRec = findViewById(R.id.cardRec)
        dbRef = FirebaseDatabase.getInstance().getReference("Online").child("Cards")
        childEventListener = AppChildEventListener { snapshot, eventType ->
            when (eventType) {
                1 -> {
                    adapter.updateItem(snapshot.getCardModel())
                }

                2 -> {
                    adapter.updateItem(snapshot.getCardModel())
                }

                3 -> {
                    adapter.removeItem(snapshot.getCardModel())
                }
            }
        }
        dbRef.addChildEventListener(childEventListener)
        setupSearch()
    }

    private fun setupSearch() {
        val searchText = findViewById<EditText>(R.id.searchText)
        searchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s.toString()) // Фильтруем список
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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
        Picasso.get().load(VKID.instance.accessToken?.userData?.photo200).into(profileImageInMain)
        Picasso.get().load(replaceImageSize(VKID.instance.accessToken?.userData?.photo200.toString())).into(profileImageInAccount)
        name_of_teacher.text = VKID.instance.accessToken?.userData?.firstName
        surname_of_teacher.text = VKID.instance.accessToken?.userData?.lastName
        val tgId:TextView = findViewById(R.id.tgId)
        val experience:TextView = findViewById(R.id.exp)
        val subject:TextView = findViewById(R.id.subject)
        val about:TextView = findViewById(R.id.about)


        FirebaseDatabase.getInstance().getReference("Online").child("Profiles").child("${VKID.instance.accessToken?.userID}").get().addOnSuccessListener {
            age_of_teacher.text = it.child("age").value.toString()
            tgId.text = it.child("tgId").value.toString()
            experience.text = it.child("exp").value.toString()
            subject.text = it.child("subject").value.toString()
            about.text = it.child("about").value.toString()
        }
    }

    override fun onClick(CardData: CardData) {

    }

    private fun replaceImageSize(imageUrl: String): String {
        val baseUrl = imageUrl.substringBeforeLast("=")
        return "${baseUrl}=250x250"
    }
}
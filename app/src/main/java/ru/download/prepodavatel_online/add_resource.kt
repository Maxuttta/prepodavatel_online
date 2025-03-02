package ru.download.prepodavatel_online

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.vk.id.VKID

class add_resource : AppCompatActivity() {

    private val type_of_res by lazy { findViewById<ConstraintLayout>(R.id.type_of_res)}
    private val video_constr by lazy { findViewById<ConstraintLayout>(R.id.video_constr) }
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_resource)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val science_click:ConstraintLayout = findViewById(R.id.science_click)
        val video_click:ConstraintLayout = findViewById(R.id.video_click)

        science_click.setOnClickListener {

        }
        video_click.setOnClickListener {
            type_of_res.animate()
                .alpha(0f)
                .setDuration(500)
                .withEndAction {
                    type_of_res.visibility = View.INVISIBLE
                    video_constr.alpha = 0f
                    video_constr.visibility = View.VISIBLE
                    video_constr.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .start()
                }
                .start()
        }

        val send_video:ConstraintLayout = findViewById(R.id.send_video)
        send_video.setOnClickListener {

            val label_input:TextView = findViewById(R.id.label_input)
            val link_input:TextView = findViewById(R.id.link_input)
            if(label_input.text.length >= 4 && link_input.text.length >= 6 && (link_input.text.toString().contains("rutube.ru")
                        || link_input.text.toString().contains("vkvideo.ru") || link_input.text.toString().contains("youtube.com")
                        || link_input.text.toString().contains("disk.yandex.ru") || link_input.text.toString().contains("dzen.ru/video"))
                && (link_input.text.toString().contains("https://") || link_input.text.toString().contains("http://"))){
                dbRef = FirebaseDatabase.getInstance().getReference("Online").child("videos").child(System.currentTimeMillis().toString())
                val hashMap = hashMapOf<String, Any>(
                    "label" to "${label_input.text}",
                    "link" to "${link_input.text}",
                    "type" to "video",
                    "author" to "${VKID.instance.accessToken?.userData?.firstName} ${VKID.instance.accessToken?.userData?.lastName}",
                    "status" to false
                )
                Toast.makeText(this, getString(R.string.video_send_to_mod), Toast.LENGTH_LONG).show()
                dbRef.updateChildren(hashMap as Map<String, Any>)
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }else{
                Toast.makeText(this, "Проверьте данные в полях!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}
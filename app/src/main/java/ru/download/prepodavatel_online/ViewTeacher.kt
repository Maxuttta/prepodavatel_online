package ru.download.prepodavatel_online

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.vk.id.VKID

class ViewTeacher : AppCompatActivity() {
    private val avaIcon by lazy { findViewById<ImageView>(R.id.ava) }
    private val deskription by lazy { findViewById<TextView>(R.id.ava) }
    private val name by lazy { findViewById<TextView>(R.id.name) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_teacher)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val avaIcon2 =  findViewById<ImageView>(R.id.avaIcon2)
        val deskription2 = findViewById<TextView>(R.id.deskription2)
        val name2 = findViewById<TextView>(R.id.name2)
        val id = intent.getStringExtra("id").toString()
        val expr = findViewById<TextView>(R.id.expr)

        FirebaseDatabase.getInstance().getReference("Online").child("Profiles").child(id).get().addOnSuccessListener {

            name2.text = "${it.child("lastname").value} ${it.child("name").value}"
            deskription2.text = it.child("about").value.toString()
            Picasso.get().load(replaceImageSize(it.child("avaUrl").value.toString())).into(avaIcon2)
            expr.text = it.child("exp").value.toString() + "+"
        }

    }

    private fun replaceImageSize(imageUrl: String): String {
        val baseUrl = imageUrl.substringBeforeLast("=")
        return "${baseUrl}=250x250"
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}
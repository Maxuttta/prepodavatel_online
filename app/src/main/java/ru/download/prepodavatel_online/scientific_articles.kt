package ru.download.prepodavatel_online

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class scientific_articles : AppCompatActivity() {
    private lateinit var adapter: CardAdapter
    private lateinit var childEventListener: ChildEventListener
    private lateinit var dbRef: DatabaseReference
    private val cardRec by lazy { findViewById<RecyclerView>(R.id.cardRec) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_scientific_articles)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = CardAdapter(this@scientific_articles) // <-- Инициализация адаптера
        cardRec.layoutManager = LinearLayoutManager(this)
        cardRec.adapter = adapter

        loadCards()
    }


    private fun loadCards() {
        dbRef = FirebaseDatabase.getInstance().getReference("Online").child("Articles")
        childEventListener = AppChildEventListener { snapshot, eventType ->
            when (eventType) {
                1 -> {
                    adapter.addItem(snapshot.getCardModel())
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

    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}
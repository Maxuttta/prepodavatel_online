package ru.download.prepodavatel_online

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Article_about : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_article_about)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val text_desk_2:TextView = findViewById(R.id.text_desk_2)

        val title = intent.getStringExtra("title")
        val subtitle = intent.getStringExtra("subtitle")
        val author = intent.getStringExtra("author")
        val deskription = intent.getStringExtra("deskription")
        text_desk_2.text = "$title\n$subtitle\n$author\n$deskription"
    }
}
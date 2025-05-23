package ru.download.prepodavatel_online

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vk.id.VKID


@Suppress("NAME_SHADOWING")
class CardAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var cardList = mutableListOf<CardData>()
    private val ITEM_VIDEO = 3
    private val ITEM_ARTICLE = 2
    private val ITEM_TEACHER = 1

    private lateinit var mDiffResult: DiffUtil.DiffResult
    private var fullList = mutableListOf<CardData>()

    class CardTeacherHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.name)
        val lastname = itemView.findViewById<TextView>(R.id.lastname)
        val subject = itemView.findViewById<TextView>(R.id.subject)
        val ava = itemView.findViewById<ImageView>(R.id.ava)
        val rating = itemView.findViewById<RatingBar>(R.id.rating)
        val message_to_teacher = itemView.findViewById<CardView>(R.id.message_to_teacher)
        val call_to_teacher = itemView.findViewById<CardView>(R.id.call_to_teacher)
        val main = itemView.findViewById<CardView>(R.id.bg)
    }

    class CardArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val subtitle = itemView.findViewById<TextView>(R.id.subtitle)
        val author = itemView.findViewById<TextView>(R.id.author)
        val read_text = itemView.findViewById<ConstraintLayout>(R.id.read_text)
    }

    class CardVideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val platform = itemView.findViewById<TextView>(R.id.platform)
        val nazvan_video = itemView.findViewById<TextView>(R.id.nazvan_video)
        val authorvideo = itemView.findViewById<TextView>(R.id.authorvideo)
        val watch_video = itemView.findViewById<ConstraintLayout>(R.id.watch_video)
    }


    override fun getItemViewType(position: Int): Int {
        val currentCard = cardList[position]
        return if (currentCard.type == "teacher") {
            ITEM_TEACHER
        } else if (currentCard.type == "article") {
            return ITEM_ARTICLE
        } else if (currentCard.type == "video"){
            return ITEM_VIDEO
        } else{
            return ITEM_VIDEO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            3 -> {
                val view: View = LayoutInflater.from(context)
                    .inflate(R.layout.video_card, parent, false)
                CardVideoHolder(view)
            }

            2 -> {
                val view: View = LayoutInflater.from(context)
                    .inflate(R.layout.article_card, parent, false)
                CardArticleHolder(view)
            }

            1 -> {
                val view: View = LayoutInflater.from(context)
                    .inflate(R.layout.teacher_card, parent, false)
                CardTeacherHolder(view)
            }

            else -> {
                val view: View = LayoutInflater.from(context)
                    .inflate(R.layout.teacher_card, parent, false)
                CardTeacherHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentCard = cardList[position]
        if (holder.javaClass == CardTeacherHolder::class.java) {
            val holder = holder as CardTeacherHolder
            holder.apply {
                name.text = currentCard.name
                lastname.text = currentCard.lastname
                subject.text = currentCard.subject
                val avaUr = replaceImageSize("${currentCard.avaUrl}")
                Picasso.get().load(avaUr).into(ava)
                rating.rating = currentCard.rating!!.toFloat()

                message_to_teacher.setOnClickListener {
                    val id = currentCard.vkId
                    val url = "https://vk.com/im?sel=$id"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }

                call_to_teacher.setOnClickListener {
                    val id = currentCard.vkId
                    val url = "https://vk.com/call?id=$id"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
                main.setOnClickListener {
                    val id = currentCard.vkId
                    val intent = Intent(context, ViewTeacher::class.java).apply {
                        putExtra("id", id)
                    }
                    context.startActivity(intent)
                }

            }
        } else if (holder.javaClass == CardArticleHolder::class.java) {
            val holder = holder as CardArticleHolder
            holder.apply {
                title.text = currentCard.title
                subtitle.text = currentCard.subtitle
                author.text = "Автор: ${currentCard.author}"
                read_text.setOnClickListener {
                    val intent = Intent(context, Article_about::class.java).apply {
                        putExtra("title", currentCard.title)
                        putExtra("subtitle", currentCard.subtitle)
                        putExtra("author", currentCard.author)
                        putExtra("deskription", currentCard.deskription)
                    }
                    context.startActivity(intent)
                }
            }
        }
        else {
            val holder = holder as CardVideoHolder
            holder.apply {
                val link = currentCard.link
                if(link!!.contains("rutube"))
                    platform.text = "Видео с Rutube"
                else
                    platform.text = "Цифровое видео"
                nazvan_video.text = currentCard.label
                authorvideo.text = "Автор: ${currentCard.author}"
                watch_video.setOnClickListener {
                    val videoUrl = currentCard.link
                    if (!videoUrl.isNullOrEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Ссылка на видео отсутствует", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    fun addItem(item: CardData) {
        if(item.status == true || item.subject != null){
            val newList = mutableListOf<CardData>()
            newList.addAll(cardList)
            newList.add(item)
            mDiffResult = DiffUtil.calculateDiff(DiffUtilCard(cardList, newList))
            mDiffResult.dispatchUpdatesTo(this)
            cardList = newList
            fullList.add(item)
            filter("")
        }
        else{
            Log.d("VIDEOS", "Не прошло модерацию")
        }
    }

    fun updateItem(item: CardData) {
        val index = cardList.indexOfFirst { it.vkId == item.vkId }
        if (index != -1) {
            val newList = mutableListOf<CardData>()
            newList.addAll(cardList)
            newList[index] = item
            mDiffResult = DiffUtil.calculateDiff(DiffUtilCard(cardList, newList))
            mDiffResult.dispatchUpdatesTo(this)
            cardList = newList

        } else {
            addItem(item)
        }
    }
    fun updateList(newList: List<CardData>) {
        cardList = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun removeItem(item: CardData) {
        val index = cardList.indexOfFirst { it.vkId == item.vkId }
        cardList.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, cardList.size)
    }
    interface Listener {
        fun onClick(CardData: CardData)
    }

    fun replaceImageSize(imageUrl: String): String {
        val baseUrl = imageUrl.substringBeforeLast("=")
        return "${baseUrl}=250x250"
    }

    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            fullList // Полный список, если строка пуста
        } else {
            fullList.filter {
                it.name!!.contains(query, ignoreCase = true) ||
                        it.lastname!!.contains(query, ignoreCase = true) ||
                        it.subject!!.contains(query, ignoreCase = true)
            }
        }
        updateList(filteredList)
    }



}
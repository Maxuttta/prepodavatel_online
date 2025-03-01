package ru.download.prepodavatel_online

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
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
    private val ITEM_TEST = 3
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
    }

    class CardArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val subtitle = itemView.findViewById<TextView>(R.id.subtitle)
        val author = itemView.findViewById<TextView>(R.id.author)
    }

    class CardTestHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val subtitle = itemView.findViewById<TextView>(R.id.subtitle)
        val author = itemView.findViewById<TextView>(R.id.author)
    }


    override fun getItemViewType(position: Int): Int {
        val currentCard = cardList[position]
        return if (currentCard.type == "teacher") {
            ITEM_TEACHER
        } else if (currentCard.type == "article") {
            return ITEM_ARTICLE
        } else {
            return ITEM_TEST
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            3 -> {
                val view: View = LayoutInflater.from(context)
                    .inflate(R.layout.test_card, parent, false)
                CardTestHolder(view)
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

            }
        } else if (holder.javaClass == CardArticleHolder::class.java) {
            val holder = holder as CardArticleHolder
            holder.apply {  }
        }
        else {
            val holder = holder as CardTestHolder
            holder.apply {}
        }
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    fun addItem(item: CardData) {
        val newList = mutableListOf<CardData>()
        newList.addAll(cardList)
        newList.add(item)
        mDiffResult = DiffUtil.calculateDiff(DiffUtilCard(cardList, newList))
        mDiffResult.dispatchUpdatesTo(this)
        cardList = newList
        fullList.add(item)
        filter("")

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
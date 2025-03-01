package ru.download.prepodavatel_online

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
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

    class CardTeacherHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.name)
        val lastname = itemView.findViewById<TextView>(R.id.lastname)
        val subject = itemView.findViewById<TextView>(R.id.subject)
        val ava = itemView.findViewById<ImageView>(R.id.ava)
        val rating = itemView.findViewById<RatingBar>(R.id.rating)
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
                Picasso.get().load(currentCard.avaUrl).into(ava)
            }
        } else if (holder.javaClass == CardArticleHolder::class.java) {
            val holder = holder as CardArticleHolder
            holder.apply {  }
        }
        else {
            val holder = holder as CardTestHolder
            holder.apply {

            }
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

    fun removeItem(item: CardData) {
        val index = cardList.indexOfFirst { it.vkId == item.vkId }
        cardList.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, cardList.size)
    }
    interface Listener {
        fun onClick(CardData: CardData)
    }
}
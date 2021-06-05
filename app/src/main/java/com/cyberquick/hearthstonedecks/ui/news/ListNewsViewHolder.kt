package com.cyberquick.hearthstonedecks.ui.news

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.model.News
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item.view.*

class ListNewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.row_tv_title
    private val deckClass: TextView = itemView.row_tv_class
    private val image: ImageView  =  itemView.row_img
    private val dust: TextView = itemView.row_tv_dust
    private val time: TextView = itemView.row_tv_time
    private val formatType: TextView = itemView.row_tv_standard_wild
    private var link = ""

    fun bind(newsItem: News) {
        title.text = newsItem.title
        deckClass.text = newsItem.deckClass
        setHeroIcon(image, newsItem.deckClass)
        setDustIcon(itemView.dust_icon)
        dust.text = newsItem.dust
        time.text = newsItem.timeCreated
        link = newsItem.linkDetails

        formatType.text = newsItem.formatType
        if (newsItem.formatType == "Standard")
            formatType.setTextColor(Color.parseColor("#cc00ff"))
        else
            formatType.setTextColor(Color.parseColor("#0033cc"))

        itemView.setOnClickListener {
            itemView.findNavController()
                .navigate(
                    ListNewsFragmentDirections.actionListNewsFragmentToDetailsFragment(
                        Gson().toJson(newsItem)
                    )
                )
        }
    }


    companion object {
        fun setDustIcon(dustIcon: ImageView?) {
            dustIcon?.setImageDrawable(ResourcesCompat.getDrawable(dustIcon.resources, R.drawable.dust, null))
        }

        fun setHeroIcon(image: ImageView, deckClass: String) {
            val linkOnImageResource = when (deckClass) {
                image.resources.getString(R.string.demon_hunter) -> R.drawable.demonhunter
                image.resources.getString(R.string.druid) -> R.drawable.druid
                image.resources.getString(R.string.hunter) -> R.drawable.hunter
                image.resources.getString(R.string.paladin) -> R.drawable.paladin
                image.resources.getString(R.string.priest) -> R.drawable.mage
                image.resources.getString(R.string.shaman) -> R.drawable.shaman
                image.resources.getString(R.string.rogue) -> R.drawable.rogue
                image.resources.getString(R.string.warlock) -> R.drawable.warlock
                image.resources.getString(R.string.warrior) -> R.drawable.warrior
                image.resources.getString(R.string.mage) -> R.drawable.mage
                else -> return
            }
            image.setImageDrawable(ResourcesCompat.getDrawable(image.resources, linkOnImageResource, null))
        }
    }
}
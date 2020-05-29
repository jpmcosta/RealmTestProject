package com.jpmcosta.test.realmtestproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jpmcosta.test.realmtestproject.R
import com.jpmcosta.test.realmtestproject.adapter.ItemListAdapter.ItemHolder
import com.jpmcosta.test.realmtestproject.realm.obj.Item
import io.realm.RealmChangeListener
import io.realm.RealmResults

class ItemListAdapter(private val items: RealmResults<Item>) : RecyclerView.Adapter<ItemHolder>() {

    private val itemsChangeListener = RealmChangeListener<RealmResults<Item>> { notifyDataSetChanged() }

    private var onItemClickListener: OnItemClickListener? = null


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        items.addChangeListener(itemsChangeListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        items.removeChangeListener(itemsChangeListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder =
            ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_item, parent, false))

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = items[position] ?: return

        holder.bind(item)
    }

    override fun onViewRecycled(holder: ItemHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    override fun getItemCount(): Int = items.size

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        onItemClickListener = listener
    }


    interface OnItemClickListener {

        fun onItemClick(item: Item)
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var item: Item? = null

        init {
            itemView.setOnClickListener {
                val item = item ?: return@setOnClickListener

                onItemClickListener?.onItemClick(item)
            }
        }


        fun bind(item: Item) {
            this.item = item
            val name = "Item$${item.id}"
            val isBookmarked = item.isBookmarked
            onBind(name, isBookmarked)
        }

        fun unbind() {
            item = null
        }

        private fun onBind(name: String, isBookmarked: Boolean) {
            val text = if (isBookmarked) "$name - bookmarked" else name
            (itemView as TextView).text = text
        }
    }
}
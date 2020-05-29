package com.jpmcosta.test.realmtestproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jpmcosta.test.realmtestproject.R
import com.jpmcosta.test.realmtestproject.adapter.GroupListAdapter.GroupHolder
import com.jpmcosta.test.realmtestproject.realm.obj.Group

abstract class GroupListAdapter : RecyclerView.Adapter<GroupHolder>() {

    var groups: List<Group>? = null
        set(items) {
            if (field !== items) {
                field = items
                notifyDataSetChanged()
            }
        }

    private var onGroupClickListener: OnGroupClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder =
            GroupHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_group, parent, false))

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        val group = groups?.get(position) ?: return

        holder.bind(group)
    }

    override fun onViewRecycled(holder: GroupHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    override fun getItemCount(): Int = groups?.size ?: 0

    fun setOnGroupClickListener(listener: OnGroupClickListener?) {
        onGroupClickListener = listener
    }


    interface OnGroupClickListener {

        fun onGroupClick(group: Group)
    }

    inner class GroupHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var group: Group? = null

        init {
            itemView.setOnClickListener {
                val group = group ?: return@setOnClickListener

                onGroupClickListener?.onGroupClick(group)
            }
        }


        fun bind(group: Group) {
            this.group = group
            var name = "Group$${group.id}"
            val groupFeed = group.feed
            if (groupFeed != null) {
                name += " -- Feed$${groupFeed.id}"
            } else {
                val items = group.items
                if (items != null) {
                    name += " -- Items(${items.size})"
                }
            }
            onBind(name)
        }

        fun unbind() {
            group = null
        }

        private fun onBind(name: String) {
            (itemView as TextView).text = name
        }
    }
}
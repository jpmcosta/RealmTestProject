package com.jpmcosta.test.realmtestproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jpmcosta.test.realmtestproject.R
import io.realm.RealmChangeListener
import io.realm.RealmObject
import io.realm.RealmResults

abstract class ResultsListAdapter<T : RealmObject> : RecyclerView.Adapter<ResultsListAdapter<T>.ObjHolder>() {

    private val listener = RealmChangeListener<RealmResults<T>> { results ->
        this.results = results
    }

    private var results: RealmResults<T>? = null
        set(results) {
            field = results
            notifyDataSetChanged()
        }

    private var onObjClickListener: OnObjClickListener<T>? = null


    fun updateResults(results: RealmResults<T>?) {
        this.results?.removeChangeListener(listener)
        this.results = if (results?.isLoaded == true) results else null
        results?.addChangeListener(listener)
    }

    fun clearResults() {
        results = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjHolder =
        ObjHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_obj, parent, false))

    override fun onBindViewHolder(holder: ObjHolder, position: Int) {
        val obj = results?.get(position) ?: return

        holder.bind(obj)
    }

    abstract fun onCreateObjText(obj: T): String

    override fun onViewRecycled(holder: ObjHolder) {
        super.onViewRecycled(holder)

        holder.unbind()
    }

    override fun getItemCount(): Int = results?.size ?: 0

    fun setOnObjClickListener(listener: OnObjClickListener<T>?) {
        onObjClickListener = listener
    }


    interface OnObjClickListener<T : RealmObject> {

        fun onObjClick(obj: T)
    }

    inner class ObjHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var obj: T? = null

        init {
            itemView.setOnClickListener {
                val obj = obj ?: return@setOnClickListener

                onObjClickListener?.onObjClick(obj)
            }
        }


        fun bind(obj: T) {
            this.obj = obj

            val text = onCreateObjText(obj)
            onBind(text)
        }

        fun unbind() {
            obj = null
        }

        private fun onBind(text: String) {
            (itemView as TextView).text = text
        }
    }
}
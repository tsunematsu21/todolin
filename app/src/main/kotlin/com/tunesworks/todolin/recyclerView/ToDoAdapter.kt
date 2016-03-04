package com.tunesworks.todolin.recyclerView

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tunesworks.todolin.BR
import com.tunesworks.todolin.R
import com.tunesworks.todolin.model.ToDo
import io.realm.RealmResults

open class ToDoAdapter(context: Context, results: RealmResults<ToDo>): RealmRecyclerViewAdapter<ToDo, ToDoAdapter.ViewHolder>(context, results) {
    val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = ToDoAdapter.ViewHolder(inflater.inflate(ViewHolder.LAYOUT_ID, parent, false))

    override fun onBindViewHolder(holder: ToDoAdapter.ViewHolder?, position: Int) {
        holder?.apply {
            binding.setVariable(BR.todo, realmResults[position])
            binding.executePendingBindings()
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        companion object {
            val LAYOUT_ID = R.layout.list_item_todo
        }

        val binding: ViewDataBinding = DataBindingUtil.bind(itemView)
    }
}
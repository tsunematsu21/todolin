package com.tunesworks.todolin.recyclerView

import android.content.Context
import android.support.v7.widget.RecyclerView
import io.realm.RealmObject
import io.realm.RealmResults

abstract class RealmRecyclerViewAdapter<T: RealmObject, VH: RecyclerView.ViewHolder>(
        val context: Context,
        var realmResults: RealmResults<T>
): RecyclerView.Adapter<VH>() {
    override fun getItemCount() = realmResults.size
}
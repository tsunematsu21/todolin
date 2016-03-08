package com.tunesworks.todolin.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.otto.Subscribe
import com.tunesworks.todolin.R
import com.tunesworks.todolin.ToDolin
import com.tunesworks.todolin.event.ToDoEvent
import com.tunesworks.todolin.model.ToDo
import com.tunesworks.todolin.recyclerView.ToDoAdapter
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.fragment_list.*
import kotlin.properties.Delegates

class ListFragment : BaseFragment() {
    companion object {
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }

    var realm: Realm by Delegates.notNull<Realm>()
    var adapter: ToDoAdapter by Delegates.notNull<ToDoAdapter>()
    var realmResults: RealmResults<ToDo> by Delegates.notNull<RealmResults<ToDo>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
        ToDolin.bus.register(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        realmResults = realm.allObjects(ToDo::class.java).apply {
            sort(ToDo::createdAt.name, Sort.DESCENDING)
        }

        adapter = ToDoAdapter(context, realmResults)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
        ToDolin.bus.unregister(this)
    }

    @Subscribe fun createToDo(event: ToDoEvent.Create) {
        val uuid = event.todo.uuid
        adapter.realmResults.forEachIndexed { pos, todo ->
            if (todo.uuid == uuid) {
                adapter.notifyItemInserted(pos)
                return
            }
        }

        adapter.notifyDataSetChanged()
    }
}
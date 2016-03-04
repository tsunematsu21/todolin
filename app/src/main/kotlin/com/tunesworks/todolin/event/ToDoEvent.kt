package com.tunesworks.todolin.event

import com.tunesworks.todolin.model.ToDo

class ToDoEvent {
    data class Create(val todo: ToDo)
}
package com.tunesworks.todolin.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class ToDo(
        @PrimaryKey
        open var uuid: String = UUID.randomUUID().toString(),
        open var title: String = "",
        open var createdAt: Date = Date(),
        open var updatedAt: Date = Date()
): RealmObject() {}
package edu.neu.khoury.madsea.majianqing

class TodoModule {
    companion object Factory {
        fun createList(): TodoModule = TodoModule()
    }

    var UID: String? = null
    var itemDataText: String? = null
    var moreDetails: String? = null
    var tags: CharSequence? = null
    var deadline: String? = null
    var reminder: Boolean? = false
    var timeToRemind: String? = null
    var done: Boolean? = false
}

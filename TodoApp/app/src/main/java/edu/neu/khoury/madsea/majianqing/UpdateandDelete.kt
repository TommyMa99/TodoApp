package edu.neu.khoury.madsea.majianqing

interface UpdateandDelete{

    fun modifyItem(itemUID:String, isDone:Boolean)
    fun onItemDelete(itemUID:String)
    fun onItemEdit(itemUID: String)
}
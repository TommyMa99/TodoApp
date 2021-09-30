package edu.neu.khoury.madsea.majianqing

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() ,UpdateandDelete{
    lateinit var database:DatabaseReference
    var toDOList:MutableList<TodoModule>? = null
    lateinit var adapter: ToDoAdapter
    private var listViewItem : ListView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        listViewItem = findViewById<ListView>(R.id.item_listview)

        database = FirebaseDatabase.getInstance().reference

        fab.setOnClickListener {view ->
            val editIntent = Intent(this, EditActivity::class.java)
            startActivityForResult(editIntent,1234)
        }
        toDOList = mutableListOf()
        adapter = ToDoAdapter(this,toDOList!!)
        listViewItem!!.adapter = adapter
        database.addValueEventListener(object:ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "No item Added", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot){
                toDOList!!.clear()
                addItemToList(snapshot)
            }
        })
    }

    private fun addItemToList(snapshot: DataSnapshot) {
        val items = snapshot.children.iterator()
        if(items.hasNext()){
            val toDoIndexedValue = items.next()
            val itemIterator = toDoIndexedValue.children.iterator()

            while(itemIterator.hasNext()){
                val currentItem = itemIterator.next()
                val toDoItemData=TodoModule.createList()
                val map = currentItem.getValue() as HashMap<String, Any>

                toDoItemData.UID=currentItem.key
                toDoItemData.done=map.get("done") as Boolean?
                toDoItemData.itemDataText=map.get("itemDataText") as String?
                toDoItemData.reminder=map.get("reminder") as Boolean?
                toDoItemData.timeToRemind=map.get("timeToRemind") as String?
                toDoItemData.moreDetails=map.get("moreDetails") as String?
                toDoItemData.tags=map.get("tags") as String?
                toDoItemData.deadline=map.get("deadline") as String?
                toDOList!!.add(toDoItemData)
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun modifyItem(itemUID: String, isDone: Boolean) {
        val itemReference=database.child("todo").child(itemUID)
        itemReference.child("done").setValue(isDone)
    }

    override fun onItemDelete(itemUID: String) {
        val itemReference =database.child("todo").child(itemUID)
        itemReference.removeValue()
        adapter.notifyDataSetChanged()
    }

    override fun onItemEdit(itemUID: String) {
        val extraEditActivity = Intent(this, ExtraEditActivity::class.java)
        extraEditActivity.putExtra("value", itemUID)
        startActivity(extraEditActivity)
    }
}
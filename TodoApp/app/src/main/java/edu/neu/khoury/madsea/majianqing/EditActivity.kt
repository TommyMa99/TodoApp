package edu.neu.khoury.madsea.majianqing
import java.io.Serializable;
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.KeyListener
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditActivity : AppCompatActivity() {
    private var task: EditText? = null
    private var details: EditText? = null
    private var deadline: EditText? = null
    private var reminder: EditText? = null
    var clicked:Boolean = false
    lateinit var spinnerResult: TextView
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        database = FirebaseDatabase.getInstance().reference
        val cancel = findViewById<View>(R.id.cancel_butt) as Button
        val save = findViewById<View>(R.id.save_butt) as Button
        task = findViewById(R.id.task_title)
        details = findViewById(R.id.more_details)
        val tags :Spinner = findViewById(R.id.tags)
        deadline = findViewById(R.id.deadline)
        reminder = findViewById(R.id.reminder)
        spinnerResult = findViewById(R.id.spinnerDisplay)
        val reminderCheck = findViewById<View>(R.id.reminder_checkbox) as Button
        reminder?.isEnabled = false

        val todoItemData = TodoModule.createList()
        ArrayAdapter.createFromResource(
            this,
            R.array.tags_todo,
            android.R.layout.simple_spinner_dropdown_item
        ).also{adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tags.adapter = adapter
        }
        tags.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val text: String = " "+p0?.getItemAtPosition(p2).toString()
                val origin: String = spinnerResult.text.toString()
                spinnerResult.text = (text + origin)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        reminderCheck.setOnClickListener { view ->
            clicked = !clicked
            reminder?.isEnabled = clicked
        }

    }

    fun saveAction(view: View) {
        val todoItemData = TodoModule.createList()
        todoItemData.itemDataText = task?.text.toString()
        todoItemData.deadline = deadline?.text.toString()
        todoItemData.done = false
        todoItemData.moreDetails = details?.text.toString()
        todoItemData.timeToRemind = reminder?.text.toString()
        todoItemData.tags = spinnerResult.text
        todoItemData.reminder = clicked
        val newItemData = database.child("todo").push()
        todoItemData.UID = newItemData.key
        newItemData.setValue(todoItemData)
        Toast.makeText(this,"item saved", Toast.LENGTH_LONG).show()
        finish()
    }

    fun cancelAction(view: View) {
        finish()
    }
}
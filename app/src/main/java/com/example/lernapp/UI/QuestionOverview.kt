package com.example.lernapp.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.lernapp.Adapter.QuestionAdapter
import com.example.lernapp.DataClasses.Questions
import com.example.lernapp.R
import com.google.firebase.database.*

class QuestionOverview : AppCompatActivity() {

    lateinit var kategorieName: TextView
    lateinit var questionList: MutableList<Questions>
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_overview)

        //Handed over from CategoryAdapter
        val category = intent.getStringExtra("extra_category_id")
        val categoryName = intent.getStringExtra("extra_category_name")

        kategorieName = findViewById<TextView>(R.id.categoryName)

        kategorieName.setText(categoryName)

        val database = FirebaseDatabase.getInstance().getReference("Categorys").child(category).child("questions")

        questionList = mutableListOf()
        listView = findViewById(R.id.listViewQuestion)

        database.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                questionList.clear()
                if (p0!!.exists()) {
                    for(h in p0.children) {
                        val question = h.getValue(Questions::class.java)
                        questionList.add(question!!)
                    }
                }
                val adapter = QuestionAdapter(this@QuestionOverview, R.layout.questions, questionList)
                listView.adapter = adapter
            }
        })
    }

    fun back_to_home(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

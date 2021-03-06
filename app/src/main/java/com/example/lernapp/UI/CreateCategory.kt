package com.example.lernapp.UI

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lernapp.Adapter.CategoryAdapter
import com.example.lernapp.DataClasses.Categories
import com.example.lernapp.R
import com.google.firebase.database.*

class CreateCategory : AppCompatActivity() {

    lateinit var nameCategory: EditText
    lateinit var saveCategory: Button
    private lateinit var database: DatabaseReference
    lateinit var categoryList: MutableList<Categories>
    lateinit var listView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category)

        database = FirebaseDatabase.getInstance().getReference("Categorys")

        saveCategory = findViewById(R.id.saveCategory)
        nameCategory = findViewById(R.id.nameCategory)

        saveCategory.setOnClickListener {
            savesCategory()
        }

        categoryList = mutableListOf()
        listView = findViewById(R.id.listView)

        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                categoryList.clear()
                if (p0!!.exists()) {
                    for (h in p0.children) {
                        val category = h.getValue(Categories::class.java)
                        categoryList.add(category!!)
                    }
                }
                val adapter =
                    CategoryAdapter(
                        this@CreateCategory, R.layout.categories, categoryList)
                listView.adapter = adapter
            }
        })
    }

    fun savesCategory() {

        val kategorie = nameCategory.text.toString().trim()

        if (kategorie.isEmpty()) {
            nameCategory.error = "Geben Sie einen Name an."
            return
        }

        val id = database.push().key.toString()
        val kat = Categories(id, kategorie)

        database.child(id).setValue(kat)
            .addOnCompleteListener {
                Toast.makeText(baseContext, kategorie + " wurde gespeichert.", Toast.LENGTH_LONG)
                    .show()
                nameCategory.text.clear()
            }
            .addOnCanceledListener {
                Toast.makeText(
                    baseContext,
                    kategorie + " wurde nicht gespeichert.",
                    Toast.LENGTH_LONG
                ).show()
            }
    }


    fun back_to_home(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

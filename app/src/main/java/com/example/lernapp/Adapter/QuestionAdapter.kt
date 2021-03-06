package com.example.lernapp.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.lernapp.DataClasses.Questions
import com.example.lernapp.R
import com.google.firebase.database.FirebaseDatabase

class QuestionAdapter (val mCtx: Context, val layoutResId: Int, val questionList: List<Questions>)
    : ArrayAdapter<Questions>(mCtx, layoutResId, questionList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        //for Questions Overview
        val textViewName = view.findViewById<TextView>(R.id.textViewQuestion1)
        val textViewAnswer1 = view.findViewById<TextView>(R.id.textViewName1)
        val textViewAnswer2 = view.findViewById<TextView>(R.id.textViewName2)
        val textViewAnswer3 = view.findViewById<TextView>(R.id.textViewName3)
        val textViewAnswer4 = view.findViewById<TextView>(R.id.textViewName4)
        val imageViewDelete = view.findViewById<ImageView>(R.id.imageViewDelete)
        val imageViewUpdate = view.findViewById<ImageView>(R.id.imageViewUpdate)

        val question = questionList[position]

        textViewName.text = question.question
        textViewAnswer1.text = question.answer1
        textViewAnswer2.text = question.answer2
        textViewAnswer3.text = question.answer3
        textViewAnswer4.text = question.answer4

        imageViewDelete.setOnClickListener {
            deleteQuestion(question)
        }

        imageViewUpdate.setOnClickListener {
            showUpdateQuestion(question)
        }

        return view
    }



    private fun deleteQuestion(question: Questions) {

        val builder = AlertDialog.Builder(mCtx)

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.delete_layout, null)

        val questionName = question.question.toString().trim()

        builder.setView(view)

        val deleteText = view.findViewById<TextView>(R.id.deleteCategory)
        val title = view.findViewById<TextView>(R.id.title)

        title.text = "Frage löschen"

        deleteText.text = "Möchten Sie die Frage: " + questionName + " wirklick Löschen?"

        builder.setPositiveButton("Löschen"){p0, p1 ->

            val dbQuestions = FirebaseDatabase.getInstance().getReference("Categorys").child(question.categoryId).child("questions").child(question.id)

            dbQuestions.removeValue()

            Toast.makeText(mCtx, "Die Frage: " + questionName + " wurde gelöscht.", Toast.LENGTH_LONG).show()
        }

        builder.setNegativeButton("Zurück"){p0, p1 ->
            Toast.makeText(mCtx, "Löschen wurde abgebrochen", Toast.LENGTH_LONG).show()
        }

        val alert = builder.create()
        alert.show()

    }

    private fun showUpdateQuestion(question: Questions) {

        val builder = AlertDialog.Builder(mCtx)

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.update_question_layout, null)

        val editText = view.findViewById<EditText>(R.id.nameQuestion)
        val answer1 = view.findViewById<EditText>(R.id.answer1)
        val answer2 = view.findViewById<EditText>(R.id.answer2)
        val answer3 = view.findViewById<EditText>(R.id.answer3)
        val answer4 = view.findViewById<EditText>(R.id.answer4)
        val title = view.findViewById<TextView>(R.id.title)

        editText.setText(question.question)
        answer1.setText(question.answer1)
        answer2.setText(question.answer2)
        answer3.setText(question.answer3)
        answer4.setText(question.answer4)
        title.text = "Ändere deine Karteikarte"

        builder.setView(view)

        builder.setPositiveButton("Ändern") {p0, p1 ->
            val dbQuestions = FirebaseDatabase.getInstance().getReference("Categorys").child(question.categoryId).child("questions")

            val questionName = editText.text.toString().trim()
            val answer1 = answer1.text.toString().trim()
            val answer2 = answer2.text.toString().trim()
            val answer3 = answer3.text.toString().trim()
            val answer4 = answer4.text.toString().trim()

            if (questionName == "" || answer1 == "" || answer2 == "" ||answer3 == "" ||answer4 == "" ) {
                Toast.makeText(this.context, "Bitte füllen Sie alle Felder aus.", Toast.LENGTH_LONG).show()
                return@setPositiveButton
            } else {

                val question = Questions(question.id, questionName, answer1, answer2, answer3, answer4, question.categoryId)

                dbQuestions.child(question.id).setValue(question)

                Toast.makeText(mCtx, "Wurde zu " + questionName + " geändert.", Toast.LENGTH_LONG).show()
            }
        }

        builder.setNegativeButton("Zurück") { p0, p1 ->

        }

        val alert = builder.create()
        alert.show()

    }
}
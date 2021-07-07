package com.example.opictest

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.lang.Thread.sleep

class EditActivity : AppCompatActivity() {
    private var editText:EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        editText = findViewById(R.id.editText)
        inputFromExternal()
    }

    private fun inputFromExternal() {
        val fileTitle = "file.txt"
        val file = File(Environment.getExternalStorageDirectory(), fileTitle)
        try {
            val reader = BufferedReader(FileReader(file))
            var result: String? = ""
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                result += (line + "\n")
            }
            editText?.setText(result)
            reader.close()
        } catch (e: Exception) {
        }
    }

    fun onClickSave(view: View) {
        write()
        setResult(RESULT_OK)
        finish()
    }

    fun onClickCancel(view : View) {
        onBackPressed()
    }

    private fun write() {
        val fileTitle = "file.txt"
        val file = File(Environment.getExternalStorageDirectory(), fileTitle)

        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            val writer = FileWriter(file, false)
            val str = editText?.text.toString()
            writer.write(str)
            writer.close()
        } catch (e: Exception) {
        }
    }
}
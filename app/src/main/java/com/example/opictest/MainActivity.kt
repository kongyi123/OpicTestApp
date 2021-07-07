package com.example.opictest

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.*


class MainActivity : AppCompatActivity() {
    private val list:ArrayList<TextCard> = ArrayList()
    private var layout:LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val string = Manifest.permission.WRITE_EXTERNAL_STORAGE
        ActivityCompat.requestPermissions(this, arrayOf(string), MODE_PRIVATE);

        layout = findViewById(R.id.root)
        inputFromExternal()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            refresh();
        }
    }

    private fun inputFromInternal() {
        try {
            val iStream:InputStream = openFileInput("file.txt")
            val iStreamReader = InputStreamReader(iStream)
            val bufferedReader = BufferedReader(iStreamReader)
            var temp:String? = bufferedReader.readLine()
            while(temp != null) {
                if (temp != null) {
                    addTextView(temp)
                }
                temp = bufferedReader.readLine()
            }
            iStream.close()
        } catch (e:FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun inputFromExternal() {
        val fileTitle = "file.txt"
        val file = File(Environment.getExternalStorageDirectory(), fileTitle)
        try {
            val fr = FileReader(file)
            val reader = BufferedReader(fr)
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                line?.let { addTextView(it) }
            }
            reader.close()
            fr.close()
        } catch (e: Exception) {
        }
    }
    
    private fun addTextView(string:String) {
        val textView = TextView(this)
        textView.text = string
        textView.textSize = 20F
        layout?.addView(textView)
        val textCard = TextCard(textView)
        list.add(textCard)
    }

    fun onClickClear(view: View) {
        for (textCard in list) {
            textCard.hide()
        }
    }

    fun onClickEdit(view: View) {
        val intent = Intent(this, EditActivity::class.java)
        startActivityForResult(intent,0)
//        refresh(); // 바로 윗줄의 startActivity보다 refresh가 더 빨리 처리 된다. async 하게 수행된다.
        // 그렇기 때문에 EditActivity에서 변경된 결과가 이 액티비티로 돌아왔을 때 반영 안된다.
    }

    fun refresh() {
        layout?.removeAllViews()
        list.clear()
        inputFromExternal()
    }
}

class TextCard constructor(private val textView: TextView) {
    private var isHided:Boolean = true

    init {
        textView.setOnClickListener {
            show()
        }
        hide()
    }
    fun hide() {
        isHided = true
        textView.setBackgroundColor(Color.GRAY)
        textView.setTextColor(Color.GRAY)
    }
    fun show() {
        isHided = false
        textView.setBackgroundColor(Color.WHITE)
        textView.setTextColor(Color.BLUE)
    }

}




package android.ufrpe.com.explore

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Chat : AppCompatActivity() {

    private var textView: TextView? = null
    private val user: ImageAdapter? = null
    private var contactName = "Marcelo Araujo"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        textView = findViewById(R.id.contactName) as TextView
        textView!!.text = this.contactName
    }

    fun setContactName(contactName: String) {
        this.contactName = contactName
    }
}

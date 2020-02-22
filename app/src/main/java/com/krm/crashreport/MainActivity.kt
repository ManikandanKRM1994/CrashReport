package com.krm.crashreport

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.krm.crashreport.CrashReporter.logException
import com.krm.crashreport.ui.CrashReporterActivity

import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var context: Context? = null
    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        findViewById<View>(R.id.nullPointer).setOnClickListener {
            context = null
            assert(false)
            context!!.resources
        }
        findViewById<View>(R.id.indexOutOfBound).setOnClickListener {
            val list: ArrayList<Any> = ArrayList()
            list.add("hello")
            list[2]
        }
        findViewById<View>(R.id.classCastExeption).setOnClickListener {
            val x: Any = 0
            println(x as String)
        }
        findViewById<View>(R.id.arrayStoreException).setOnClickListener {
            val x: Array<String?> = arrayOfNulls(3)
            x[0] = 0.toString()
        }
        Thread(Runnable {
            try {
                context = null
                assert(false)
                context!!.resources
            } catch (e: Exception) { //log caught Exception
                logException(e)
            }
        }).start()
        mContext = this
        findViewById<View>(R.id.crashLogActivity).setOnClickListener {
            val intent = Intent(mContext, CrashReporterActivity::class.java)
            startActivity(intent)
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

package com.example.synechron_test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.synechron_test.adapter.PetsAdapter
import com.example.synechron_test.dialog.DialogFrag
import com.example.synechron_test.model.PetsItem
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity(), PetsAdapter.OnViewClickListener {

    val listOfPets = arrayListOf<PetsItem>()
    var currentHour: Int = 0
    var chatStatus: Boolean = false
    var callStatus: Boolean = false
    var workHours: String? = null
    lateinit var myadapter: PetsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Set Adapter
        setAdapter()

        //fetch office timing
        fetchOfficeTiming()
        //Fetch pet List
        fetchlist()
        //Time of the day
        getTimeOfDay()

        btn_chat.setOnClickListener {
            displayInformation()
        }
        btn_call.setOnClickListener {
            displayInformation()
        }


    }

    fun checkButtonStatus() {
        if (chatStatus.equals(false)) {
            btn_chat.visibility = View.GONE
        } else if (callStatus.equals(false)) {
            btn_chat.visibility = View.GONE
        }

    }

    fun setOfficeTime() {
        tv_office_time.text =
            String.format(getResources().getString(R.string.office_timing), workHours)
    }

    fun getTimeOfDay() {
        val cal: Calendar = Calendar.getInstance()
        cal.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"))
        currentHour = cal.get(Calendar.HOUR_OF_DAY)

    }

    fun fetchOfficeTiming() {
        Client.sendRequest().newCall(Client.getRequest(CONSTANT.OFFICE_TIME_URL))
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val myResponse = response.body?.string()
                        runOnUiThread {
                            val jsonObj = JSONObject(myResponse)
                            chatStatus = jsonObj.getBoolean(CONSTANT.ISCHATENABLE)
                            callStatus = jsonObj.getBoolean(CONSTANT.ISCALLENABLE)
                            workHours = jsonObj.getString(CONSTANT.WORKHOUR)
                            setOfficeTime()
                            checkButtonStatus()
                        }


                    }

                }

            })
    }

    fun fetchlist() {
        Client.sendRequest().newCall(Client.getRequest(CONSTANT.PET_LIST_URL))
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {

                        val myResponse = response.body?.string()

                        runOnUiThread {
                            val jsonarray = JSONArray(myResponse)
                            for (i in 0 until jsonarray.length() - 1) {
                                val jsonobject: JSONObject = jsonarray.getJSONObject(i)
                                val image_url = jsonobject.getString(CONSTANT.IMAGE_URL)
                                val title = jsonobject.getString(CONSTANT.TITLE)
                                val content_url = jsonobject.getString(CONSTANT.CONTENT_URL)
                                val date_added = jsonobject.getString(CONSTANT.DATE_ADDED)
                                listOfPets.add(PetsItem(image_url, title, content_url, date_added))

                            }
                            //Update the list response to the adapter
                            myadapter.setData(listOfPets)
                        }


                    }

                }

            })
    }


    fun displayInformation() {
        Log.d("Hour", currentHour.toString())
        if (currentHour >= 10 && currentHour < 18) {
            //show Dialog
            showDialog(resources.getString(R.string.office_open))

        } else {
            showDialog(resources.getString(R.string.office_closed))
        }
    }

    fun showDialog(msg: String) {
        val dialog = DialogFrag.newInstance(msg)
        dialog?.show(supportFragmentManager, "Dialog Fragment")
    }

    fun setAdapter() {
        //init recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        myadapter = PetsAdapter(this@MainActivity, this)
        recyclerView.adapter = myadapter

    }

    override fun onViewClick(content_url: String) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("content_url", content_url)
        startActivity(intent)
    }


}
package com.smartgen.simplemessageapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    lateinit var messageNumber: EditText
    lateinit var messageBody: EditText
    lateinit var send: Button

    private lateinit var receiveMessageBody: TextView
    lateinit var receiveMessageNumber: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageBody=findViewById(R.id.messageBody)
        messageNumber=findViewById(R.id.phoneNumber)
        send=findViewById(R.id.send)
        receiveMessageBody=findViewById(R.id.messageBodyReceive)
        receiveMessageNumber=findViewById(R.id.messageNumberReceive)

        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            resultListener.launch(arrayOf(android.Manifest.permission.RECEIVE_SMS,android.Manifest.permission.SEND_SMS))
        }else{
            sendSms()
        }











    }

    private fun sendSms(){
        readSms()

        send.setOnClickListener {
            val messageNumberString=messageNumber.text.toString().trim()
            val messageBodyString=messageBody.text.toString().trim()
            if (messageBodyString.isEmpty() || messageNumberString.isEmpty()){
                Toast.makeText(this,"enter details",Toast.LENGTH_SHORT).show()
            }else{
                val smsManager=SmsManager.getDefault()
                smsManager.sendTextMessage(messageNumberString,messageNumberString,messageBodyString,null,null)
            }
        }


    }




    private val resultListener=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        it.entries.forEach { it1 ->
            if (it1.value){
                sendSms()
            }
        }
    }


   private fun readSms(){
       val br=object :BroadcastReceiver(){
           override fun onReceive(p0: Context?, p1: Intent?) {

               Log.d("checkSms", "onReceive: ")

              for (i in Telephony.Sms.Intents.getMessagesFromIntent(p1)){

                  receiveMessageBody.text=i.messageBody
                  receiveMessageNumber.text=i.originatingAddress
              }
           }
       }

       registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }



}
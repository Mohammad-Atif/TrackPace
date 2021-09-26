package com.example.trackpace.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.trackpace.databinding.ActivitySplashScreenBinding
import com.example.trackpace.util.Constants

class SplashScreen : AppCompatActivity() {

    lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreference=getSharedPreferences("forcounts", Context.MODE_PRIVATE)
        val gender=sharedPreference.getString(Constants.USER_GENDER,"null")
        /*
        Checking if user has entered their details or not
        if not start login activty
        else start main activity
         */
        Handler().postDelayed(
            {
                if(gender=="null")
                {
                    val intent= Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                finish()
            },1600
        )
    }
}

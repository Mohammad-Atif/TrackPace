package com.example.trackpace.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.trackpace.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var userName:String?=null
    private var userAge: Int? = null
    private var userGender: String? = null
    private var userWeight:Int? = null
    private var userHeight:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        Handler().postDelayed(
//            {
//                val intent= Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            },1600
//        )

    }




}
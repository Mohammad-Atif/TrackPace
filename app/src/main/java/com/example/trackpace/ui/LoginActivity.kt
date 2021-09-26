package com.example.trackpace.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.trackpace.R
import com.example.trackpace.databinding.ActivityLoginBinding
import com.example.trackpace.util.Constants

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var userName:String?=null
    private var userAge: Int = 18
    private var userGender: String = "Male"
    private var userWeight:Int? = null
    private var userHeight:Int? = null
    private lateinit var ageList:Array<Int>
    private var hFt:Int = 0
    private var hIn:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ageList = Array(80) { i -> i + 18 }



        val spinner: Spinner = findViewById(R.id.sp_Age)
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ageList
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

//        Handler().postDelayed(
//            {
//                val intent= Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            },1600
//        )

        binding.spAge.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                userAge=adapterView?.getItemAtPosition(position).toString().toInt()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.spGender.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                userGender=adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.spHeightFt.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                hFt=adapterView?.getItemAtPosition(position).toString().toInt()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.spHeightIn.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                hIn=adapterView?.getItemAtPosition(position).toString().toInt()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.btnStart.setOnClickListener {
            signIn()
        }




    }

    private fun kgToPounds(weight: Int): Double {
        return weight / 0.45359237
    }

    private fun toInches(feet:Int,inches:Int): Int {
        return feet*12+inches
    }

    private fun signIn()
    {
        if(binding.editTextName.text.isNullOrBlank())
        {
            Toast.makeText(this,"Enter Name",Toast.LENGTH_SHORT).show()
            binding.editTextName.requestFocus()
            return
        }
        if(binding.textInputWeightEdit.text.isNullOrBlank())
        {
            Toast.makeText(this,"Enter Weight",Toast.LENGTH_SHORT).show()
            binding.textInputWeightEdit.requestFocus()
            return
        }

        if(hFt==0)
        {
            binding.spHeightFt.requestFocus()
            return
        }
        userHeight=toInches(hFt,hIn)
        userWeight=kgToPounds(binding.textInputWeightEdit.text.toString().toInt()).toInt()
        val sharedPreference=getSharedPreferences("forcounts", Context.MODE_PRIVATE)
        with(sharedPreference.edit())
        {
            putString(Constants.USER_NAME,binding.editTextName.text.toString())
            putString(Constants.USER_GENDER, userGender)
            putInt(Constants.USER_HEIGHT,userHeight!!)
            putInt(Constants.USER_WEIGHT,userWeight!!)
            putInt(Constants.USER_AGE, userAge)
            apply()
            commit()
        }

        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)

    }


}
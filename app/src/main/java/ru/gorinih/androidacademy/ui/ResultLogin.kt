package ru.gorinih.androidacademy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.databinding.ActivityResultLoginBinding
import ru.gorinih.androidacademy.model.User

class ResultLogin : AppCompatActivity() {
    private lateinit var binding : ActivityResultLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding = DataBindingUtil.setContentView(this, R.layout.activity_result_login)

        //old intent without Parcelable
        //val intent = intent.getStringExtra("loginName")

        // intent with Parcelable
        val intent = intent.getParcelableExtra<User>("loginName")

        binding.textLoginName.text = intent?.name.toString()
    }
}
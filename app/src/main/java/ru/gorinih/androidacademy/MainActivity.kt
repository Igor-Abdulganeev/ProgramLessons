package ru.gorinih.androidacademy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import ru.gorinih.androidacademy.databinding.ActivityMainBinding
import ru.gorinih.androidacademy.model.User
import ru.gorinih.androidacademy.ui.ResultLogin

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //old intent, without Parcelable
/*
        binding.loginEnter.setOnClickListener {
            if (onClickEnter())
        {
            val intent = Intent(this, ResultLogin::class.java)
            intent.putExtra("loginName", binding.loginName.text.toString())
            startActivity(intent)
        }
        }
*/

        // intent with Parcelable
        binding.loginEnter.setOnClickListener {
            if (onClickEnter()){
                val data = User(name = binding.loginName.text.toString())
                val intent = Intent(this, ResultLogin::class.java)
                intent.putExtra("loginName", data)
                startActivity(intent)
            }
        }

        binding.loginPassVisible.setOnClickListener{onClickPassVisible()}
    }

    private fun onClickEnter() = binding.loginPass.text.toString() == "1234"


    private fun onClickPassVisible() {
      Log.d(TAG,binding.loginPass.inputType.toString())
        if (binding.loginPass.inputType==18) {
            binding.loginPass.inputType=2
            binding.loginPassVisible.setImageResource(R.drawable.ic_baseline_visibility_24)
        }
        else {
            binding.loginPass.inputType = 18
            binding.loginPassVisible.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }
    }

    companion object {
        const val TAG = "MAINACTIVITY"
    }
}

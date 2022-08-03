package ru.netology.nerecipe.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nerecipe.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}

package com.journaler.activity

import android.databinding.DataBindingUtil
import android.location.Location
import android.os.Bundle
import com.journaler.R
import com.journaler.databinding.ActivityBindingBinding
import com.journaler.model.Note

abstract class BindingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * ActivityBindingBinding is auto generated class
         * which name is derived from activity_binding.xml filename.
         */
        val binding : ActivityBindingBinding = DataBindingUtil.setContentView(this, R.layout.activity_binding)
        val location = Location("dummy")
        val note = Note("my note", "bla", location)
        binding.note = note
    }

}
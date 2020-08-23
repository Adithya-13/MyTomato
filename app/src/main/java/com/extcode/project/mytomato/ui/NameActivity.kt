package com.extcode.project.mytomato.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.ui.SettingActivity.Companion.EXTRA_FROM_SETTING
import kotlinx.android.synthetic.main.layout_name_interested.*
import java.util.*

class NameActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        iLike.text = getString(R.string.i_like_s, "...")
        loadData()

        business.setOnClickListener(this)
        entertainment.setOnClickListener(this)
        science.setOnClickListener(this)
        health.setOnClickListener(this)
        sports.setOnClickListener(this)
        technology.setOnClickListener(this)
        toGetSchedulesActivity.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.business -> iLike.text = getTextButton(business)
            R.id.entertainment -> iLike.text = getTextButton(entertainment)
            R.id.health -> iLike.text = getTextButton(health)
            R.id.science -> iLike.text = getTextButton(science)
            R.id.sports -> iLike.text = getTextButton(sports)
            R.id.technology -> iLike.text = getTextButton(technology)
            R.id.toGetSchedulesActivity -> toGetSchedulesActivity()
        }
    }

    private fun toGetSchedulesActivity() {

        val getArrayInterested = iLike.text.toString().split(" ").toTypedArray()
        val getValueInterested = getArrayInterested[2].toLowerCase(Locale.getDefault())
        val getName = name.editText?.text.toString()

        when {
            iLike.text == "I Like ..." -> showToast("Please select one of Topics below")
            name.editText?.text.isNullOrEmpty() -> showToast("Please fill Name Field")
            else -> {

                val sharedPref = getSharedPreferences(
                    NameActivity::class.simpleName,
                    Context.MODE_PRIVATE
                )
                val sharedPrefSchedules = getSharedPreferences(
                    GetSchedulesActivity::class.simpleName,
                    Context.MODE_PRIVATE
                )

                sharedPref.edit {
                    putString("name", getName)
                    putString("interested", getValueInterested)
                    commit()
                }

                if (intent.getBooleanExtra(EXTRA_FROM_SETTING, false)) {

                    startActivity(Intent(this, SettingActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    })
                    finish()

                } else {

                    if (sharedPrefSchedules.all.isEmpty()) {
                        startActivity(Intent(this, GetSchedulesActivity::class.java))
                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }

                }
            }
        }
    }

    private fun loadData() {

        val sharedPref = getSharedPreferences(
            NameActivity::class.simpleName,
            Context.MODE_PRIVATE
        )

        val getName = sharedPref.getString("name", null)
        val getInterested = sharedPref.getString("interested", null)
        if (getName != null) name.editText?.setText(getName)
        if (getInterested != null) iLike.text = getString(R.string.i_like_s, getInterested)

    }

    private fun getTextButton(button: Button): String {
        return getString(R.string.i_like_s, button.text)
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
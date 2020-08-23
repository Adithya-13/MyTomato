package com.extcode.project.mytomato.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.extcode.project.mytomato.R
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.layout_setting_item.*

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_FROM_SETTING = "extraFromSetting"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        changesNameTopic.setOnClickListener(this)
        changesSchedules.setOnClickListener(this)
        backButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.changesNameTopic -> intentActivity(NameActivity::class.java)
            R.id.changesSchedules -> intentActivity(GetSchedulesActivity::class.java)
            R.id.backButton -> onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
    }

    private fun intentActivity(cls: Class<*>) {
        startActivity(Intent(this, cls).putExtra(EXTRA_FROM_SETTING, true))
    }
}
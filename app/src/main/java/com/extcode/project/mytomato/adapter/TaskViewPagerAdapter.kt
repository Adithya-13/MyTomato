package com.extcode.project.mytomato.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.ui.fragment.CompletedFragment
import com.extcode.project.mytomato.ui.fragment.OnGoingFragment

class TaskViewPagerAdapter(private val context: Context, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles = intArrayOf(
        R.string.onGoing,
        R.string.completed
    )

    private val fragment = listOf(
        OnGoingFragment(),
        CompletedFragment()
    )

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(tabTitles[position])
    }

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getCount(): Int = 2

}
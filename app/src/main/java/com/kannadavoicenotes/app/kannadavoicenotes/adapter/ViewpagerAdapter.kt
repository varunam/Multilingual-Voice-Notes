package com.kannadavoicenotes.app.kannadavoicenotes.adapter

/**
 * Created by varun.am on 05/12/18
 */

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kannadavoicenotes.app.kannadavoicenotes.view.fragments.ChooseLanguageFragment

class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val title = arrayOf("Speech to text", "History")

    override fun getItem(position: Int): Fragment {
        return ChooseLanguageFragment.newInstance()
    }

    override fun getCount(): Int {
        return title.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}

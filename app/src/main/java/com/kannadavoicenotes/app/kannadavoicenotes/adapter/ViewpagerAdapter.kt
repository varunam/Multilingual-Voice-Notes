package com.kannadavoicenotes.app.kannadavoicenotes.adapter

/**
 * Created by varun.am on 05/12/18
 */

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.kannadavoicenotes.app.kannadavoicenotes.view.fragments.ChooseLanguageFragment

class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val title = arrayOf("Translator", "History")

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

package ro.dobrescuandrei.utils

import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.setupWithViewPager(viewPager : ViewPager, initialTab : Int = 0)
{
    viewPager.currentItem = initialTab
    menu.getItem(0).isChecked = false
    menu.getItem(initialTab).isChecked = true
    tag=initialTab

    setOnNavigationItemSelectedListener { item ->
        viewPager.currentItem = item.order
        return@setOnNavigationItemSelectedListener false
    }

    viewPager.setOnPageChangedListener { position ->
        menu.getItem(tag as Int).isChecked = false
        menu.getItem(position).isChecked = true
        tag=position
    }
}
package ro.dobrescuandrei.utils

import android.support.v4.view.ViewPager

fun ViewPager.setOnPageChangedListener(listener : (Int) -> (Unit))
{
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener
    {
        override fun onPageScrollStateChanged(p0: Int)
        {
        }

        override fun onPageScrolled(p0: Int, p1: Float, p2: Int)
        {
        }

        override fun onPageSelected(page: Int)
        {
            listener(page)
        }
    })
}

package ro.dobrescuandrei.utils

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.balysv.materialmenu.MaterialMenu
import com.balysv.materialmenu.MaterialMenuDrawable

class ToolbarXData
{
    val menuItemClickListeners : MutableMap<Int, () -> (Unit)> = mutableMapOf()
    var menu : Int = 0
}

fun Toolbar.getData() : ToolbarXData
{
    if (tag==null)
        tag=ToolbarXData()
    return tag as ToolbarXData
}

fun Toolbar.setupBackIcon()
{
    setNavigationIcon(R.drawable.ic_arrow_left_white_24dp)
    setNavigationOnClickListener { (context as Activity).onBackPressed() }
}

fun Toolbar.setMenu(menu : Int)
{
    getData().menu=menu
}

operator fun Toolbar.set(menuItemId : Int, value: () -> (Unit)) =
    getData().menuItemClickListeners.put(menuItemId, value)

fun Toolbar.onOptionsItemSelected(item : MenuItem?)
{
    if (item?.itemId==android.R.id.home)
    {
        (context as Activity).onBackPressed()
    }
    else
    {
        val listeners=getData().menuItemClickListeners
        for ((itemId, action) in listeners)
        {
            if (item?.itemId==itemId)
            {
                action()
                return
            }
        }
    }
}

fun Toolbar.onCreateOptionsMenu(menuInflater : MenuInflater, menuObj : Menu?)
{
    val menu=getData().menu
    if (menu!=0)
        if (menuObj!=null)
            menuInflater.inflate(menu, menuObj)
}

fun Toolbar.onCreateOptionsMenuFromFragment()
{
    val menu=getData().menu
    if (menu!=0)
        inflateMenu(menu)
}

fun Toolbar.setupHamburgerMenu()
{
    val materialMenu=MaterialMenuDrawable(context, Color.WHITE, MaterialMenuDrawable.Stroke.THIN)
    materialMenu.iconState=MaterialMenuDrawable.IconState.BURGER
    navigationIcon = materialMenu
}

fun Toolbar.setOnHamburgerMenuClickedListener(listener : () -> (Unit))
{
    setNavigationOnClickListener {
        (navigationIcon as? MaterialMenu)?.let { materialMenu ->
            if (materialMenu.iconState==MaterialMenuDrawable.IconState.BURGER)
                materialMenu.animateIconState(MaterialMenuDrawable.IconState.ARROW)
            else materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER)
        }

        listener()
    }
}

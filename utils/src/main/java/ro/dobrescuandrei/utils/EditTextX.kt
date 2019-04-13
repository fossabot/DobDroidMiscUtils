package ro.dobrescuandrei.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.setOnTextChangedListener(listener : (String) -> (Unit)) {
    addTextChangedListener(object : TextWatcher
    {
        override fun afterTextChanged(s: Editable?)
        {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
        {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        {
            listener(s.toString())
        }
    })
}

fun EditText.setOnEditorActionListener(listener : (Int) -> (Unit))
{
    setOnEditorActionListener { view, actionId, event ->
        listener(actionId)
        return@setOnEditorActionListener true
    }
}

fun EditText.setOnDoneClickedListener(listener: () -> Unit)
{
    setOnEditorActionListener { view, actionId, event ->
        if (actionId==EditorInfo.IME_ACTION_DONE)
            listener()
        return@setOnEditorActionListener true
    }
}

fun EditText.openKeyboard()
{
    requestFocus()
    Keyboard.open(on = context)
}

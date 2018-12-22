package ro.dobrescuandrei.utils

class DispatchOnce
(
    val procedure : () -> (Unit)
)
{
    var invoked : Boolean = false

    fun invoke()
    {
        if (!invoked)
        {
            invoked=true
            procedure()
        }
    }
}
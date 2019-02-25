package ro.dobrescuandrei.utils

class YieldContext<T>
{
    val result=mutableListOf<T>()

    fun yield(obj : T)
    {
        result.add(obj)
    }

    fun yield(list : Collection<T>)
    {
        result.addAll(list)
    }

    fun yield(array : Array<T>)
    {
        result.addAll(array)
    }
}

inline fun <T> yielding(block : YieldContext<T>.() -> (Unit)) : List<T>
{
    val yieldContext=YieldContext<T>()
    block(yieldContext)
    return yieldContext.result
}

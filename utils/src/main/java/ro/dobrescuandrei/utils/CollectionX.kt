package ro.dobrescuandrei.utils

inline fun <IN, OUT> Collection<IN>.mapToSet(mapper : (IN) -> (OUT?)) : Set<OUT>
{
    val set=HashSet<OUT>()
    for (item in this)
    {
        val mappedItem=mapper(item)
        if (mappedItem!=null)
            set.add(mappedItem)
    }

    return set
}

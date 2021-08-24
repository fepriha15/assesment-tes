package id.android.pokeapp.util

import java.util.*

fun toCapitalize(input : String?): String?{
    return input?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
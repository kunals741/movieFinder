package com.kunal.locoassignment.constants

import androidx.annotation.StringDef
import com.kunal.locoassignment.constants.SortOptions.Companion.RANDOM
import com.kunal.locoassignment.constants.SortOptions.Companion.RATING
import com.kunal.locoassignment.constants.SortOptions.Companion.YEAR

@Retention(AnnotationRetention.SOURCE)
@StringDef(YEAR, RATING, RANDOM)
annotation class SortOptions {

    companion object {
        const val YEAR = "Year"
        const val RATING = "Rating"
        const val RANDOM = "Random"
    }
}
package com.midexamp.mdp.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Data class representing an image item in the explorer
 * @param titleRes Resource ID for the image title
 * @param imageRes Resource ID for the drawable image
 */
data class ImageItem(
    @StringRes val titleRes: Int,
    @DrawableRes val imageRes: Int
)
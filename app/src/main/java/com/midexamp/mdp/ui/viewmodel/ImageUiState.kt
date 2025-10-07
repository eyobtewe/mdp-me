package com.midexamp.mdp.ui.viewmodel

import com.midexamp.mdp.data.ImageItem

/**
 * Immutable data class representing the UI state for the Image Explorer
 * 
 * @param currentImageItem The currently displayed image item, null if no data available
 * @param currentIndex The index of the current image in the list
 * @param totalImages The total number of images available
 * @param isLoading Whether the UI is in a loading state
 * @param errorMessage Error message if any error occurred, null otherwise
 */
data class ImageUiState(
    val currentImageItem: ImageItem? = null,
    val currentIndex: Int = 0,
    val totalImages: Int = 0,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    /**
     * Convenience property to check if there's valid image data
     */
    val hasData: Boolean
        get() = currentImageItem != null && totalImages > 0

    /**
     * Convenience property to get the display index (1-based for UI)
     */
    val displayIndex: Int
        get() = if (hasData) currentIndex + 1 else 0
}
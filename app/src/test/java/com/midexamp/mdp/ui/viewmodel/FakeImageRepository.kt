package com.midexamp.mdp.ui.viewmodel

import com.midexamp.mdp.data.ImageItem
import com.midexamp.mdp.data.ImageRepository

/**
 * Fake implementation of ImageRepository for testing purposes
 * Provides controlled test data without external dependencies
 */
class FakeImageRepository : ImageRepository {

    private val testImageItems = listOf(
        ImageItem(
            titleRes = 1001, // Fake string resource ID
            imageRes = 2001  // Fake drawable resource ID
        ),
        ImageItem(
            titleRes = 1002,
            imageRes = 2002
        ),
        ImageItem(
            titleRes = 1003,
            imageRes = 2003
        )
    )

    private var shouldReturnError = false
    private var customImageList: List<ImageItem>? = null

    /**
     * Returns test image data or custom data if set
     */
    override fun getImageData(): List<ImageItem> {
        if (shouldReturnError) {
            throw RuntimeException("Test error")
        }
        return customImageList ?: testImageItems
    }

    /**
     * Returns image item at specified position
     */
    override fun getImageItem(position: Int): ImageItem? {
        val images = customImageList ?: testImageItems
        return if (position in images.indices) {
            images[position]
        } else {
            null
        }
    }

    /**
     * Configure repository to return an error for testing error scenarios
     */
    fun setShouldReturnError(shouldError: Boolean) {
        shouldReturnError = shouldError
    }

    /**
     * Set custom image list for testing different scenarios
     */
    fun setCustomImageList(images: List<ImageItem>) {
        customImageList = images
    }

    /**
     * Reset repository to default state
     */
    fun reset() {
        shouldReturnError = false
        customImageList = null
    }
}
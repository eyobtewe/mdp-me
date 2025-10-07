package com.midexamp.mdp.data

/**
 * Repository interface for managing image data
 * Defines the contract for accessing image items
 */
interface ImageRepository {
    /**
     * Get all available image data
     * @return List of ImageItem objects
     */
    fun getImageData(): List<ImageItem>
    
    /**
     * Get image item by position
     * @param position Index of the image item
     * @return ImageItem at the specified position or null if not found
     */
    fun getImageItem(position: Int): ImageItem?
}
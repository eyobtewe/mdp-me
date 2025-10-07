package com.midexamp.mdp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midexamp.mdp.data.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the state of the Image Explorer
 * Handles image navigation and UI state management
 */
@HiltViewModel
class ImageViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {

    // Private mutable state for internal index tracking
    private val _currentIndex = MutableStateFlow(0)
    
    // Private mutable state for UI state
    private val _uiState = MutableStateFlow(ImageUiState())
    
    // Public immutable StateFlow for UI consumption
    val uiState: StateFlow<ImageUiState> = _uiState.asStateFlow()

    // Cache the image list for efficient access
    private var imageList = emptyList<com.midexamp.mdp.data.ImageItem>()

    init {
        loadImages()
    }

    /**
     * Load images from repository and initialize UI state
     */
    private fun loadImages() {
        viewModelScope.launch {
            try {
                imageList = imageRepository.getImageData()
                if (imageList.isNotEmpty()) {
                    updateUiState()
                } else {
                    _uiState.value = ImageUiState(
                        isLoading = false,
                        errorMessage = "No images available"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ImageUiState(
                    isLoading = false,
                    errorMessage = "Failed to load images: ${e.message}"
                )
            }
        }
    }

    /**
     * Handle next button click - advances to next image with wrap-around
     */
    fun onNextClicked() {
        if (imageList.isNotEmpty()) {
            _currentIndex.value = (_currentIndex.value + 1) % imageList.size
            updateUiState()
        }
    }

    /**
     * Handle previous button click - goes to previous image with wrap-around
     */
    fun onPreviousClicked() {
        if (imageList.isNotEmpty()) {
            val newIndex = _currentIndex.value - 1
            _currentIndex.value = if (newIndex < 0) imageList.size - 1 else newIndex
            updateUiState()
        }
    }

    /**
     * Navigate to specific image by index
     * @param index The target index to navigate to
     */
    fun navigateToImage(index: Int) {
        if (index in imageList.indices) {
            _currentIndex.value = index
            updateUiState()
        }
    }

    /**
     * Update UI state based on current index and image list
     */
    private fun updateUiState() {
        val currentIndex = _currentIndex.value
        val currentItem = imageList.getOrNull(currentIndex)
        
        _uiState.value = ImageUiState(
            currentImageItem = currentItem,
            currentIndex = currentIndex,
            totalImages = imageList.size,
            isLoading = false,
            errorMessage = null
        )
    }

    /**
     * Refresh the image data
     */
    fun refresh() {
        _uiState.value = ImageUiState(isLoading = true)
        loadImages()
    }
}
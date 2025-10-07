package com.midexamp.mdp.ui.viewmodel

import com.midexamp.mdp.data.ImageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for ImageViewModel
 * Tests ViewModel initialization, navigation functionality, and error handling
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ImageViewModelTest {

    private lateinit var fakeRepository: FakeImageRepository
    private lateinit var viewModel: ImageViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Set main dispatcher to test dispatcher for coroutines
        Dispatchers.setMain(testDispatcher)
        
        // Initialize fake repository
        fakeRepository = FakeImageRepository()
        
        // Initialize ViewModel with fake repository
        viewModel = ImageViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        // Reset main dispatcher
        Dispatchers.resetMain()
        
        // Reset fake repository state
        fakeRepository.reset()
    }

    @Test
    fun `viewModel initializes with first image and correct state`() = runTest {
        // Wait for initialization to complete
        advanceUntilIdle()
        
        val uiState = viewModel.uiState.value
        
        // Assert initial state
        assertEquals(0, uiState.currentIndex)
        assertEquals(3, uiState.totalImages)
        assertEquals(1, uiState.displayIndex) // 1-based index
        assertTrue(uiState.hasData)
        assertFalse(uiState.isLoading)
        assertNull(uiState.errorMessage)
        
        // Assert first image is loaded
        val firstImage = uiState.currentImageItem
        assertEquals(1001, firstImage?.titleRes)
        assertEquals(2001, firstImage?.imageRes)
    }

    @Test
    fun `onNextClicked moves to second image`() = runTest {
        // Wait for initialization
        advanceUntilIdle()
        
        // Call onNextClicked
        viewModel.onNextClicked()
        
        val uiState = viewModel.uiState.value
        
        // Assert state updated to second image
        assertEquals(1, uiState.currentIndex)
        assertEquals(2, uiState.displayIndex)
        assertEquals(3, uiState.totalImages)
        
        // Assert second image is loaded
        val secondImage = uiState.currentImageItem
        assertEquals(1002, secondImage?.titleRes)
        assertEquals(2002, secondImage?.imageRes)
    }

    @Test
    fun `onNextClicked wraps around from last to first image`() = runTest {
        // Wait for initialization
        advanceUntilIdle()
        
        // Navigate to last image (index 2)
        viewModel.onNextClicked() // index 1
        viewModel.onNextClicked() // index 2
        
        // Verify we're at the last image
        assertEquals(2, viewModel.uiState.value.currentIndex)
        
        // Call onNextClicked to wrap around
        viewModel.onNextClicked()
        
        val uiState = viewModel.uiState.value
        
        // Assert wrapped around to first image
        assertEquals(0, uiState.currentIndex)
        assertEquals(1, uiState.displayIndex)
        
        // Assert first image is loaded
        val firstImage = uiState.currentImageItem
        assertEquals(1001, firstImage?.titleRes)
        assertEquals(2001, firstImage?.imageRes)
    }

    @Test
    fun `onPreviousClicked moves to previous image`() = runTest {
        // Wait for initialization
        advanceUntilIdle()
        
        // Move to second image first
        viewModel.onNextClicked()
        assertEquals(1, viewModel.uiState.value.currentIndex)
        
        // Call onPreviousClicked
        viewModel.onPreviousClicked()
        
        val uiState = viewModel.uiState.value
        
        // Assert moved back to first image
        assertEquals(0, uiState.currentIndex)
        assertEquals(1, uiState.displayIndex)
        
        // Assert first image is loaded
        val firstImage = uiState.currentImageItem
        assertEquals(1001, firstImage?.titleRes)
    }

    @Test
    fun `onPreviousClicked wraps around from first to last image`() = runTest {
        // Wait for initialization (starts at index 0)
        advanceUntilIdle()
        
        // Call onPreviousClicked from first image
        viewModel.onPreviousClicked()
        
        val uiState = viewModel.uiState.value
        
        // Assert wrapped around to last image
        assertEquals(2, uiState.currentIndex)
        assertEquals(3, uiState.displayIndex)
        
        // Assert last image is loaded
        val lastImage = uiState.currentImageItem
        assertEquals(1003, lastImage?.titleRes)
        assertEquals(2003, lastImage?.imageRes)
    }

    @Test
    fun `navigateToImage updates to specific index`() = runTest {
        // Wait for initialization
        advanceUntilIdle()
        
        // Navigate directly to index 2
        viewModel.navigateToImage(2)
        
        val uiState = viewModel.uiState.value
        
        // Assert navigated to correct image
        assertEquals(2, uiState.currentIndex)
        assertEquals(3, uiState.displayIndex)
        
        // Assert third image is loaded
        val thirdImage = uiState.currentImageItem
        assertEquals(1003, thirdImage?.titleRes)
        assertEquals(2003, thirdImage?.imageRes)
    }

    @Test
    fun `navigateToImage with invalid index does not change state`() = runTest {
        // Wait for initialization
        advanceUntilIdle()
        val initialState = viewModel.uiState.value
        
        // Try to navigate to invalid index
        viewModel.navigateToImage(99)
        
        val uiState = viewModel.uiState.value
        
        // Assert state unchanged
        assertEquals(initialState.currentIndex, uiState.currentIndex)
        assertEquals(initialState.currentImageItem, uiState.currentImageItem)
    }

    @Test
    fun `repository error shows error state`() = runTest {
        // Set repository to return error
        fakeRepository.setShouldReturnError(true)
        
        // Create new ViewModel with error repository
        val errorViewModel = ImageViewModel(fakeRepository)
        
        // Wait for initialization
        advanceUntilIdle()
        
        val uiState = errorViewModel.uiState.value
        
        // Assert error state
        assertFalse(uiState.hasData)
        assertFalse(uiState.isLoading)
        assertTrue(uiState.errorMessage?.contains("Failed to load images") == true)
        assertNull(uiState.currentImageItem)
    }

    @Test
    fun `empty repository shows error state`() = runTest {
        // Set repository to return empty list
        fakeRepository.setCustomImageList(emptyList())
        
        // Create new ViewModel with empty repository
        val emptyViewModel = ImageViewModel(fakeRepository)
        
        // Wait for initialization
        advanceUntilIdle()
        
        val uiState = emptyViewModel.uiState.value
        
        // Assert error state for empty data
        assertFalse(uiState.hasData)
        assertFalse(uiState.isLoading)
        assertEquals("No images available", uiState.errorMessage)
        assertNull(uiState.currentImageItem)
    }

    @Test
    fun `refresh reloads data successfully`() = runTest {
        // Set repository to error initially
        fakeRepository.setShouldReturnError(true)
        val errorViewModel = ImageViewModel(fakeRepository)
        advanceUntilIdle()
        
        // Verify error state
        assertTrue(errorViewModel.uiState.value.errorMessage != null)
        
        // Fix repository and refresh
        fakeRepository.setShouldReturnError(false)
        errorViewModel.refresh()
        advanceUntilIdle()
        
        val uiState = errorViewModel.uiState.value
        
        // Assert data loaded successfully after refresh
        assertTrue(uiState.hasData)
        assertFalse(uiState.isLoading)
        assertNull(uiState.errorMessage)
        assertEquals(0, uiState.currentIndex)
        assertEquals(3, uiState.totalImages)
    }

    @Test
    fun `single image repository handles navigation correctly`() = runTest {
        // Set repository to return single image
        val singleImage = listOf(ImageItem(titleRes = 9001, imageRes = 9001))
        fakeRepository.setCustomImageList(singleImage)
        
        val singleImageViewModel = ImageViewModel(fakeRepository)
        advanceUntilIdle()
        
        // Verify initial state
        assertEquals(0, singleImageViewModel.uiState.value.currentIndex)
        assertEquals(1, singleImageViewModel.uiState.value.totalImages)
        
        // Test next button (should wrap to same image)
        singleImageViewModel.onNextClicked()
        assertEquals(0, singleImageViewModel.uiState.value.currentIndex)
        
        // Test previous button (should wrap to same image)
        singleImageViewModel.onPreviousClicked()
        assertEquals(0, singleImageViewModel.uiState.value.currentIndex)
    }
}
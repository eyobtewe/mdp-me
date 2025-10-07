package com.midexamp.mdp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.midexamp.mdp.ui.theme.MdpTheme
import com.midexamp.mdp.ui.viewmodel.ImageViewModel

/**
 * Main screen for the Image Explorer app
 * Displays images with navigation functionality
 */
@Composable
fun ImageExplorerScreen(
    modifier: Modifier = Modifier,
    viewModel: ImageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            uiState.isLoading -> {
                // Loading state
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Loading images...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            uiState.errorMessage != null -> {
                // Error state
                Text(
                    text = uiState.errorMessage!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.refresh() }
                ) {
                    Text("Retry")
                }
            }
            
            uiState.hasData -> {
                // Content state
                val currentItem = uiState.currentImageItem!!
                
                // Image
                Image(
                    painter = painterResource(id = currentItem.imageRes),
                    contentDescription = stringResource(id = currentItem.titleRes),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Title
                Text(
                    text = stringResource(id = currentItem.titleRes),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Image counter
                Text(
                    text = "${uiState.displayIndex} of ${uiState.totalImages}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Next button
                Button(
                    onClick = { viewModel.onNextClicked() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Next Image")
                }
            }
        }
    }
}

/**
 * Preview for ImageExplorerScreen
 */
@Preview(showBackground = true)
@Composable
fun ImageExplorerScreenPreview() {
    MdpTheme {
        // Note: Preview won't work with hiltViewModel() 
        // This is just for layout preview
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Image Explorer Screen Preview")
        }
    }
}
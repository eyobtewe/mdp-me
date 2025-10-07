package com.midexamp.mdp.data

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ImageRepository
 * Provides static list of image items for the Image Explorer app
 */
@Singleton
class ImageRepositoryImpl @Inject constructor() : ImageRepository {

    private val imageItems = listOf(
        ImageItem(
            titleRes = com.midexamp.mdp.R.string.miu_campus_title,
            imageRes = com.midexamp.mdp.R.drawable.miu_campus
        ),
        ImageItem(
            titleRes = com.midexamp.mdp.R.string.sustainable_living_center_title,
            imageRes = com.midexamp.mdp.R.drawable.sustainable_living_center
        ),
        ImageItem(
            titleRes = com.midexamp.mdp.R.string.compro_professionals_title,
            imageRes = com.midexamp.mdp.R.drawable.compro_professionals
        ),
        ImageItem(
            titleRes = com.midexamp.mdp.R.string.miu_snow_fall_title,
            imageRes = com.midexamp.mdp.R.drawable.miu_snow_fall
        ),
        ImageItem(
            titleRes = com.midexamp.mdp.R.string.compro_admission_team_title,
            imageRes = com.midexamp.mdp.R.drawable.compro_admission_team
        ),
        ImageItem(
            titleRes = com.midexamp.mdp.R.string.faculty_student_title,
            imageRes = com.midexamp.mdp.R.drawable.faculty_student
        ),
        ImageItem(
            titleRes = com.midexamp.mdp.R.string.rainbow_title,
            imageRes = com.midexamp.mdp.R.drawable.rainbow
        )
    )

    override fun getImageData(): List<ImageItem> {
        return imageItems
    }

    override fun getImageItem(position: Int): ImageItem? {
        return if (position in imageItems.indices) {
            imageItems[position]
        } else {
            null
        }
    }
}
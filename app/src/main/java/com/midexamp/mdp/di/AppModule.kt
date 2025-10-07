package com.midexamp.mdp.di

import com.midexamp.mdp.data.ImageRepository
import com.midexamp.mdp.data.ImageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger/Hilt module for providing application-level dependencies
 * This module tells Hilt how to provide instances of various dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides an instance of ImageRepository
     * 
     * @return ImageRepositoryImpl instance that implements ImageRepository interface
     */
    @Provides
    @Singleton
    fun provideImageRepository(): ImageRepository {
        return ImageRepositoryImpl()
    }
}
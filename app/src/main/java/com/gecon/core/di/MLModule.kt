package com.gecon.core.di

import android.content.Context
import com.gecon.core.image.ImageLoader
import com.gecon.core.image.ImagePreprocess
import com.gecon.core.ml.GestureDetector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MLModule {

    @Provides
    @Singleton
    fun provideGestureDetector(@ApplicationContext context: Context): GestureDetector {
        return GestureDetector(context)
    }

    @Provides
    @Singleton
    fun provideImagePreprocess(
        @ApplicationContext context: Context,
        gestureDetector: GestureDetector
    ): ImagePreprocess {
        return ImagePreprocess(context, gestureDetector)
    }

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context,
        imagePreprocess: ImagePreprocess
    ): ImageLoader {
        return ImageLoader(context, imagePreprocess)
    }
}
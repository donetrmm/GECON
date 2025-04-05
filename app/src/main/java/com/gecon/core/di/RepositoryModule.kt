package com.gecon.core.di

import com.gecon.recognition.data.repository.GestureRecognitionRepositoryImpl
import com.gecon.recognition.domain.repository.GestureRecognitionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGestureRecognitionRepository(
        impl: GestureRecognitionRepositoryImpl
    ): GestureRecognitionRepository
}

package com.gecon.core.di

import android.content.Context
import com.gecon.core.ml.GestureClassifier
import com.gecon.core.ml.GestureMapper
import com.gecon.core.ml.ModelInterpreter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideModelInterpreter(@ApplicationContext context: Context): ModelInterpreter {
        return ModelInterpreter(context)
    }

    @Provides
    @Singleton
    fun provideGestureMapper(): GestureMapper {
        return GestureMapper()
    }

    @Provides
    @Singleton
    fun provideGestureClassifier(
        modelInterpreter: ModelInterpreter,
        gestureMapper: GestureMapper
    ): GestureClassifier {
        return GestureClassifier(modelInterpreter, gestureMapper)
    }
}

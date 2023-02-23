package com.notesapi.android.di

import com.notesapi.android.api.AuthInterceptor
import com.notesapi.android.api.NoteApi
import com.notesapi.android.api.UserApi
import com.notesapi.android.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder() : Retrofit.Builder {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor) : OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideUserApi(retrofitBuilder: Retrofit.Builder) : UserApi {
        return retrofitBuilder.build().create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNoteApi(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient) : NoteApi {
        return retrofitBuilder.client(okHttpClient).build().create(NoteApi::class.java)
    }

}


/* WITHOUT INTERCEPTOR
@Singleton
@Provides
fun provideRetrofit() : Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
}

@Singleton
@Provides
fun provideUserApi(retrofit: Retrofit) : UserApi {
    return retrofit.create(UserApi::class.java)
}*/

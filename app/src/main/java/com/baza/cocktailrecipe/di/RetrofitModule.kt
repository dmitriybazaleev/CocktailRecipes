package com.baza.cocktailrecipe.di

import com.baza.cocktailrecipe.presentation.module.data.api.BASE_URL
import com.baza.cocktailrecipe.presentation.module.data.api.CONNECTION_TIME_OUT
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun createRetrofit(): Retrofit {
        val gson = Gson()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(object : Converter.Factory() {
                /**
                 * В данной Api не очень удобно использовать GsonConverterFactory
                 */
                override fun responseBodyConverter(
                    type: Type,
                    annotations: Array<Annotation>,
                    retrofit: Retrofit
                ): Converter<ResponseBody, *> {
                    val adapter =
                        gson.getAdapter(TypeToken.get(type))
                    return GsonResponseBodyConverter<Any>(
                        gson,
                        adapter
                    )
                }

                override fun requestBodyConverter(
                    type: Type,
                    parameterAnnotations: Array<Annotation>,
                    methodAnnotations: Array<Annotation>,
                    retrofit: Retrofit
                ): Converter<*, RequestBody> {
                    val adapter =
                        gson.getAdapter(TypeToken.get(type))
                    return GsonRequestBodyConverter(
                        adapter
                    )
                }
            })
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(getOkHttpClient())
            .build()
    }


    @Provides
    @Singleton
    fun getOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .readTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    private class GsonResponseBodyConverter<T>(
        private val gson: Gson,
        private val adapter: TypeAdapter<out T>
    ) :
        Converter<ResponseBody, T> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): T {
            val jsonReader =
                gson.newJsonReader(value.charStream())
            return try {
                adapter.read(jsonReader)
            } finally {
                value.close()
            }
        }
    }

    private class GsonRequestBodyConverter<T>(
        private val adapter: TypeAdapter<T>
    ) :
        Converter<T, RequestBody> {

        private val mediaType: MediaType? = "application/json; charset=UTF-8".toMediaTypeOrNull()

        @Throws(IOException::class)
        override fun convert(value: T): RequestBody {
            if (value is RequestBody) {
                return value
            }
            var result = adapter.toJson(value)
            val length = result.length
            if (length > 1 && result[0] == '"' && result[length - 1] == '"') {
                result = result.substring(1, length - 1)
            }
            return result.toRequestBody(mediaType)
        }
    }
}
package dan.myapptest

import android.text.method.SingleLineTransformationMethod
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class ProductRaw(val id: Long, val title: String, val body_html: String, val image: ImageRaw)
data class ImageRaw(val id: Long, val src: String)
data class ProductsRaw(val products: List<ProductRaw>)

class ProductFields {
    var mData: Array<out String>
    constructor(vararg data: String) {
        mData = data
    }
    override fun toString(): String {
            return mData.joinToString(",")
    }
}

val DEFAULT_PROD_FIELDS = ProductFields("id", "title", "image", "body_html")

interface ShopifyService {
        @GET("admin/products.json")
        fun GetProducts(@Query("page") page: Int, @Query("fields") fields: ProductFields? = null): Single<ProductsRaw>

        @GET("admin/products/{id}.json")
        fun GetProduct(@Path("id") id: Long, @Query("fields") fields: ProductFields? = null): Single<ProductRaw>
}

//In real app move this to Dagger2 module
object ApiFactory {
        fun GetRetrofitBuilder(): Retrofit {
            var httpClient = OkHttpClient.Builder()
                    .addInterceptor({ chain ->
                        var updatedUrl = chain.request().url().newBuilder().addQueryParameter("access_token", "c32313df0d0ef512ca64d5b336a0d7c6").build()
                        chain.proceed(chain.request().newBuilder().url(updatedUrl).build())
                    }).build()
            val retrofitBuilder = Retrofit.Builder()
                    .baseUrl("https://shopicruit.myshopify.com")
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

            return retrofitBuilder
        }
    fun create() = GetRetrofitBuilder().create(ShopifyService::class.java)
}
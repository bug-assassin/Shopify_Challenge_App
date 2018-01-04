package dan.myapptest

import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    lateinit var shopifyService: ShopifyService
    @Before
    fun init() {
        shopifyService = ApiFactory.create()
    }

    @Test
    fun GetAllProducts() {
        val productsQuery = shopifyService.GetProducts(1)
        val result = productsQuery.doOnError { assert(false, { it.message!! }) }.blockingGet()
        print(result.products?.joinToString("\n"))
    }

    @Test
    fun GetAllProductsFilteredFields() {
        val productsQuery = shopifyService.GetProducts(1, DEFAULT_PROD_FIELDS)
        val result = productsQuery.doOnError { assert(false, { it.message!! }) }.blockingGet()
        print(result.products?.joinToString("\n"))
    }

    @Test
    fun GetOneProduct() {
        val productsQuery = shopifyService.GetProduct(2759137027, DEFAULT_PROD_FIELDS)
        val result = productsQuery.doOnError { assert(false, { it.message!! }) }.blockingGet()
        print(result)
    }

}

package dan.shopifyproducts

import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ProductsApiTest {
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
        assert(result.products.isNotEmpty())
    }

    @Test
    fun GetAllProductsFilteredFields() {
        val productsQuery = shopifyService.GetProducts(1, DEFAULT_PROD_FIELDS)
        val result = productsQuery.doOnError { assert(false, { it.message!! }) }.blockingGet()
        print(result.products?.joinToString("\n"))
        assert(result.products.isNotEmpty())
    }

    @Test
    fun GetOneProduct() {
        val prodId = 2759137027
        val productsQuery = shopifyService.GetProduct(prodId, DEFAULT_PROD_FIELDS)
        val result = productsQuery.doOnError { assert(false, { it.message!! }) }.blockingGet()
        print(result)
        assert(result != null)
        assert(result.product.id == prodId)
    }

}

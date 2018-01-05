package dan.shopifyproducts.mainpage

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import dan.shopifyproducts.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel : ViewModel() {
    private val subscriptions: CompositeDisposable = CompositeDisposable()
    private val productsSubscription: MutableLiveData<List<ProductRaw>> = MutableLiveData()
    val productsSub: LiveData<List<ProductRaw>> = productsSubscription
    private val repo = ApiFactory.create()

    //TODO Use an in memory cache class instead of this + a local database
    private var products: List<ProductRaw>? = null

    init {
        getAllProducts()
    }

    fun search(searchVal: String) {
        //Shopify api does not support wildcards so here we are
        var productSub = Observable.just(products)
                .flatMapIterable { it }
                .filter { it.title.contains(searchVal, ignoreCase = true) }
                .toList()
                .subscribe(productsSubscription::postValue)

        subscriptions.add(productSub)
    }
    fun getAllProducts() {
        if(products != null) {//TODO Get from memory cache + database instead of null check
            productsSubscription.postValue(products)
            return
        }

        var productSub = repo.GetProducts(1, DEFAULT_PROD_FIELDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(ProductsRaw::products)
                .subscribe({ products = it; productsSubscription.postValue(products) },
                        {
                            //TODO Show error to user: https://developer.android.com/topic/libraries/architecture/guide.html#addendum
                            Loge(it.toString())
                        })


        subscriptions.add(productSub)
    }
    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}
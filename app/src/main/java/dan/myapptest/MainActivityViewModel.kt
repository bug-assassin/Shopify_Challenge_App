package dan.myapptest

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel() : ViewModel() {
    private val subscriptions: CompositeDisposable = CompositeDisposable()
    private val productsSubscription: MutableLiveData<List<ProductRaw>> = MutableLiveData()
    private val repo = ApiFactory.create()
    fun getProductsSubscription(): LiveData<List<ProductRaw>> = productsSubscription

    init {
        getAllProducts()
    }

    fun search(searchVal: String) {
        //Why shopify api?, why don't you have search?
        var productSub = Observable.just(productsSubscription.value).filter{ it != null }.flatMapIterable { it }.filter { it.title.contains(searchVal) }.toList().subscribe(productsSubscription::postValue)
        subscriptions.add(productSub)
    }
    fun getAllProducts() {
        var productSub = repo.GetProducts(1).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(ProductsRaw::products)
                .subscribe(productsSubscription::postValue, { Logd(it.toString())})
        subscriptions.add(productSub)
    }
    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}
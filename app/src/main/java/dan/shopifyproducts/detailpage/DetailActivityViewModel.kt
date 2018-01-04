package dan.shopifyproducts.detailpage

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import dan.shopifyproducts.ApiFactory
import dan.shopifyproducts.Loge
import dan.shopifyproducts.ProductRaw
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class DetailActivityViewModel: ViewModel() {
    private val subscriptions: CompositeDisposable = CompositeDisposable()
    private val productSubscription: MutableLiveData<ProductRaw> = MutableLiveData()
    val productSub: LiveData<ProductRaw> = productSubscription
    private val repo = ApiFactory.create() //This would come from Dagger2 in real app

    private var product: ProductRaw? = null

    fun GetProduct(id: Long) {
        product?.let {
            if(it.id == id) {
                productSubscription.postValue(product)
                return
            }
        }
        repo.GetProduct(id).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { it.product }
                .subscribe(Consumer {
                    product = it
                    productSubscription.postValue(product)
                }, Consumer { Loge(it.message!!) })
    }
}
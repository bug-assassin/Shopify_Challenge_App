package dan.shopifyproducts

import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import io.reactivex.subjects.BehaviorSubject

fun Logd(message: String) = Log.d("AppMessage", message)
fun Loge(message: String) = Log.e("AppMessage", message)

object RxView {
    fun fromSearch(searchView: SearchView): BehaviorSubject<String> {
        val subject = BehaviorSubject.create<String>()
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                subject.onComplete()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                subject.onNext(newText)
                return true
            }
        })
        searchView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {

            }

            override fun onViewDetachedFromWindow(v: View?) {
                subject.onComplete()
            }

        })

        return subject
    }
}
val PROD_ID = "PROD_ID"
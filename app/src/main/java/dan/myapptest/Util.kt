package dan.myapptest

import android.util.Log
import android.view.View
import android.widget.SearchView
import io.reactivex.subjects.BehaviorSubject

fun Logd(message: String) = Log.e("", message)

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

        return subject
    }
}
package dan.myapptest

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.arch.lifecycle.ViewModelProviders
import android.view.SearchEvent
import android.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val model = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        var searchView = SearchView(this)
        RxView.fromSearch(searchView)
            .filter { it.length > 2}
            .debounce(300, TimeUnit.MILLISECONDS)
            .doOnComplete { if(searchView.textView.text.isEmpty()) model.getAllProducts() }
            .subscribe(model::search)

        model.getProductsSubscription().observe(this, Observer {
            Logd(it.toString())
        })
    }
}

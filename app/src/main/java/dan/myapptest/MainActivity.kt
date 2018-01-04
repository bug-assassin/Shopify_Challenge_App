package dan.myapptest

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.widget.SearchView
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    lateinit var searchView: SearchView
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = resources.getString(R.string.mainactivity_title)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ProductsAdapter()

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        viewModel.productsSub.observe(this, Observer {
            (recyclerView.adapter as ProductsAdapter).set(it!!)
        })

    }

    private fun initSearch() {
        RxView.fromSearch(searchView)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .debounce(300, TimeUnit.MILLISECONDS)
                .doOnComplete {
                    Logd("Search value: " + searchView.query.toString())

                    if(searchView.query.isEmpty()) {
                        viewModel.getAllProducts()
                    }

                }
                .subscribe(viewModel::search)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_options_menu, menu)
        searchView = menu.findItem(R.id.menu_search).actionView as SearchView

        initSearch()

        return true
    }
}

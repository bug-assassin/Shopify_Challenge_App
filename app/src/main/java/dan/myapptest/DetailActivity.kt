package dan.myapptest

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    lateinit var viewModel: DetailActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val prodId = intent.extras[PROD_ID] as Long
        viewModel = ViewModelProviders.of(this).get(DetailActivityViewModel::class.java)
        viewModel.GetProduct(prodId)


        val imgProduct = findViewById<ImageView>(R.id.imgProduct)
        val txtTitle = findViewById<TextView>(R.id.titleProduct)
        val txtDescription = findViewById<TextView>(R.id.descriptionProduct)

        viewModel.productSub.observe(this, Observer {
            it?.let {
                Glide.with(this)
                        .load(it.image.src)
                        .into(imgProduct)
                txtTitle.text = it.title
                txtDescription.text = Html.fromHtml(it.body_html)
            }
        })
    }
}

package dan.shopifyproducts.mainpage

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dan.shopifyproducts.PROD_ID
import dan.shopifyproducts.ProductRaw
import dan.shopifyproducts.R
import dan.shopifyproducts.detailpage.DetailActivity

class ProductsAdapter: RecyclerView.Adapter<ProductView>() {
    var items: List<ProductRaw> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_product, parent, false)
        return ProductView(view)
    }

    override fun onBindViewHolder(holder: ProductView, position: Int) {
        holder.set(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun set(products: List<ProductRaw>) {
        items = products
        notifyDataSetChanged()
    }
}


class ProductView(itemView: View): RecyclerView.ViewHolder(itemView) {
    val imgProduct = itemView.findViewById<ImageView>(R.id.imgProduct)
    val titleProduct = itemView.findViewById<TextView>(R.id.titleProduct)
    val descriptionProduct = itemView.findViewById<TextView>(R.id.descriptionProduct)
    lateinit var product: ProductRaw

    init {
        itemView.setOnClickListener({
            val detailActivityIntent = Intent(it.context, DetailActivity::class.java)
            detailActivityIntent.putExtra(PROD_ID, product.id)
            it.context.startActivity(detailActivityIntent)
        })
    }
    fun set(product: ProductRaw) {
        this.product = product
        Glide.with(itemView.context).load(product.image.src).into(imgProduct)
        titleProduct.text = product.title
        descriptionProduct.text = Html.fromHtml(product.body_html)
    }
}
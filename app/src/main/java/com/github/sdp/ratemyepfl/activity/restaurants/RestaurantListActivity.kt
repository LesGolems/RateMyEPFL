package com.github.sdp.ratemyepfl.activity.restaurants

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.ListActivityUtils
import com.github.sdp.ratemyepfl.viewmodel.RestaurantListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class RestaurantListActivity : AppCompatActivity() {

    private lateinit var restaurantAdapter: ReviewableAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewModel: RestaurantListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviewable_list)

        restaurantAdapter =
            ReviewableAdapter { rest -> displayRestaurantReviews(rest as Restaurant) }
        recyclerView = findViewById(R.id.reviewableRecyclerView)
        recyclerView.adapter = restaurantAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        )

        viewModel.getRestaurants().observe(this) {
            it?.let {
                restaurantAdapter.setData(it as MutableList<Reviewable>)
            }
        }

    }

    private fun displayRestaurantReviews(restaurant: Restaurant) {
        val intent = Intent(this, RestaurantReviewListActivity::class.java)
        intent.putExtra(
            RestaurantReviewListActivity.EXTRA_RESTAURANT_JSON,
            Json.encodeToString(restaurant)
        )
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.restaurant_options_menu, menu)
        ListActivityUtils.setUpSearchView(menu, restaurantAdapter, R.id.restaurantSearchView)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.increasingOrder -> {
            restaurantAdapter.sortAlphabetically(true)
            true
        }
        R.id.decreasingOrder -> {
            restaurantAdapter.sortAlphabetically(false)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
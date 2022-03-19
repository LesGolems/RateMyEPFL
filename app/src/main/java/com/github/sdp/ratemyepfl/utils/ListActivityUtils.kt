package com.github.sdp.ratemyepfl.utils

import android.content.Context
import android.view.Menu
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter

class ListActivityUtils {

     companion object{
         fun setUpSearchView(menu : Menu?, adapter: ReviewableAdapter, searchId : Int){
             val searchItem = menu!!.findItem(searchId)
             val searchView = searchItem.actionView as SearchView
             searchView.maxWidth = Int.MAX_VALUE
             searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                 override fun onQueryTextSubmit(query: String?): Boolean {
                     return true
                 }

                 override fun onQueryTextChange(newText: String?): Boolean {
                     adapter.filter.filter(newText)
                     return true
                 }
             })
         }
     }
}
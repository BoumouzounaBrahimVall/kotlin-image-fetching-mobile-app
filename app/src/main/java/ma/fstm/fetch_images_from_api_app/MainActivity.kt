package ma.fstm.fetch_images_from_api_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.fstm.fetch_images_from_api_app.databinding.ActivityMainBinding
import ma.fstm.fetch_images_from_api_app.models.Property
import ma.fstm.fetch_images_from_api_app.network.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
// This class represents the main activity of the application responsible for displaying and managing data.

// Importing necessary libraries and classes
class MainActivity : AppCompatActivity() {

    // Initializing variables
    lateinit var data: MutableList<Property>
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: MyAdapter
    private var mainMenu: Menu? = null
    private lateinit var binding: ActivityMainBinding

    // Lifecycle method called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setting up the data binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initializing the RecyclerView manager
        manager = LinearLayoutManager(this)

        // Setting up the swipe-to-refresh functionality
        binding.swipeRefresh.setOnRefreshListener {
            getAllData()
        }

        // Fetching initial data
        getAllData()
    }

    // Method to show or hide the delete option in the menu
    private fun showHideDelete(show: Boolean) {
        mainMenu?.findItem(R.id.menu_delete)?.isVisible = show
    }

    // Creating the options menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        mainMenu = menu
        menuInflater.inflate(R.menu.main_menu, menu)
        showHideDelete(false)
        return super.onCreateOptionsMenu(menu)
    }

    // Handling menu item selection
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete){
            deleteItem()
        }
        return super.onOptionsItemSelected(item)
    }

    // Method to fetch data from the API
    fun getAllData(){
        Api.retrofitService.getAllData().enqueue(object : Callback<List<Property>> {
            override fun onResponse(
                call: Call<List<Property>>,
                response: Response<List<Property>>
            ) {
                // Handling API response
                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerViewContainer.visibility = View.GONE

                // Stopping swipe-to-refresh animation if it is in progress
                if(binding.swipeRefresh.isRefreshing){
                    binding.swipeRefresh.isRefreshing = false
                }

                if(response.isSuccessful){
                    // Populating RecyclerView with data
                    binding.recyclerView.apply {
                        data = response.body() as MutableList<Property>
                        myAdapter = MyAdapter(data) { show -> showHideDelete(show) }
                        layoutManager = manager
                        adapter = myAdapter

                        // Setting up swipe-to-delete functionality
                        val swipeDelete = object : SwipeToDeleteCallback(this@MainActivity){
                            override fun onSwiped(
                                viewHolder: RecyclerView.ViewHolder,
                                direction: Int
                            ) {
                                myAdapter.deleteItem(viewHolder.adapterPosition)
                            }
                        }

                        val touchHelper = ItemTouchHelper(swipeDelete)
                        touchHelper.attachToRecyclerView(this)
                    }
                }
            }

            override fun onFailure(call: Call<List<Property>>, t: Throwable) {
                // Handling API call failure
                t.printStackTrace()
            }
        })
    }

    // Method to handle item deletion
    fun deleteItem(){
        // Creating an alert dialog for confirmation
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Delete")
        alertBuilder.setMessage("Do you want to delete this item ?")
        alertBuilder.setPositiveButton("Delete"){_,_ ->
            if(::myAdapter.isInitialized){
                myAdapter.deleteSelectedItem()
                showHideDelete(false)
                Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
            }
        }

        alertBuilder.setNegativeButton("No"){_,_ ->

        }

        alertBuilder.setNeutralButton("Cancel"){_,_ ->

        }
        alertBuilder.show()
    }
}

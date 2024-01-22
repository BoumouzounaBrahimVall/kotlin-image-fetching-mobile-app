package ma.fstm.fetch_images_from_api_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ma.fstm.fetch_images_from_api_app.databinding.ListItemBinding
import ma.fstm.fetch_images_from_api_app.models.Property
// This class represents a custom RecyclerView Adapter for handling a list of properties.

// Importing necessary libraries and classes
class MyAdapter(private val data: List<Property>, val showHideDelete: (Boolean) -> Unit) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Initializing variables
    private var listData: MutableList<Property> = data as MutableList<Property>
    var selectedList = mutableListOf<Int>()

    // Inner ViewHolder class for holding references to views
    inner class MyViewHolder(val view: ListItemBinding) : RecyclerView.ViewHolder(view.root) {

        // Method to bind data to the views in the ViewHolder
        fun bind(property: Property, index: Int) {
            view.constraintLayout.visibility = View.VISIBLE
            view.recyclerView.visibility = View.GONE

            // Showing or hiding the button based on property selection
            if (property.selected == true) {
                view.button.visibility = View.VISIBLE
            } else {
                view.button.visibility = View.GONE
            }

            // Setting text and image based on property data
            view.tvTitle.text = property.title
            view.tvDescription.text = property.description
            Glide.with(view.root.context).load(property.image).centerCrop().into(view.imageView)

            // Setting up click listeners for item selection and deselection
            view.constraintLayout.setOnLongClickListener { markSelectedItem(index) }
            view.constraintLayout.setOnClickListener { deselectItem(index) }
        }

        // Method to bind data to views when the RecyclerView is in a horizontal layout
        fun bindRecyclerView(data: List<Property>) {
            view.constraintLayout.visibility = View.GONE
            view.recyclerView.visibility = View.VISIBLE

            // Setting up a horizontal RecyclerView within the item
            val manager: RecyclerView.LayoutManager =
                LinearLayoutManager(view.root.context, LinearLayoutManager.HORIZONTAL, true)
            view.recyclerView.apply {
                val data = data as MutableList<Property>
                var myAdapter = MyAdapter(data) { show -> showHideDelete(show) }
                layoutManager = manager
                adapter = myAdapter
            }
        }
    }

    // Method to deselect an item in the list
    fun deselectItem(index: Int) {
        if (selectedList.contains(index)) {
            selectedList.remove(index)
            listData[index].selected = false
            notifyDataSetChanged()
            showHideDelete(selectedList.isNotEmpty())
        }
    }

    // Method to mark an item as selected
    fun markSelectedItem(index: Int): Boolean {
        // Deselecting all items
        for (item in listData) {
            item.selected = false
        }

        // Marking the selected item
        if (!selectedList.contains(index)) {
            selectedList.add(index)
        }

        // Updating the selected items
        selectedList.forEach {
            listData[it].selected = true
        }

        notifyDataSetChanged()
        showHideDelete(true)
        return true
    }

    // Method to delete selected items
    fun deleteSelectedItem() {
        if (selectedList.isNotEmpty()) {
            listData.removeAll { item -> item.selected == true }
        }
        notifyDataSetChanged()
    }

    // Creating ViewHolder instances
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
        val listItemBinding = ListItemBinding.inflate(v, parent, false)
        return MyViewHolder(listItemBinding)
    }

    // Getting the number of items in the list
    override fun getItemCount(): Int {
        return listData.size
    }

    // Binding data to views in the ViewHolder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (listData[position].horizontal) {
            // If the property is marked as horizontal, bind it to a horizontal RecyclerView
            listData[position].data?.let { holder.bindRecyclerView(it) }
        } else {
            // Otherwise, bind the property to the regular views
            holder.bind(listData[position], position)
        }
    }

    // Method to delete an item at a specific position
    fun deleteItem(index: Int) {
        listData.removeAt(index)
        notifyDataSetChanged()
    }

    // Method to set a new list of items
    fun setItems(items: List<Property>) {
        listData = items as MutableList<Property>
        notifyDataSetChanged()
    }

}

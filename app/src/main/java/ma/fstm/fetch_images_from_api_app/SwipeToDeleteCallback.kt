package ma.fstm.fetch_images_from_api_app

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

// This class is an abstract implementation of ItemTouchHelper.SimpleCallback
// It provides swipe-to-delete functionality with a background color and delete icon.

abstract class SwipeToDeleteCallback(context: Context): ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT
) {

    // Background color for the swipe action
    val backgroundColor = ContextCompat.getColor(context, R.color.colorRecyclerViewDelete)

    // Callback method called when attempting to move an item
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    // Callback method called when drawing the swipe action
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        // Using RecyclerViewSwipeDecorator to create swipe decoration
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addBackgroundColor(backgroundColor)  // Setting background color
            .addActionIcon(R.drawable.ic_delete)  // Setting delete icon
            .create()
            .decorate()

        // Calling the super method for default behavior
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}

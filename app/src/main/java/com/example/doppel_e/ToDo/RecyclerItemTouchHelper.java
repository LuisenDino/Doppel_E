package com.example.doppel_e.ToDo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doppel_e.R;
import com.example.doppel_e.ToDo.Adapter.ToDoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private ToDoAdapter adapter;

    public RecyclerItemTouchHelper(ToDoAdapter adapter){
        super(0, ItemTouchHelper.LEFT );
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction){
        final  int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Eliminar Tarea");
            builder.setMessage("¿Estás seguro que deseas eliminar la tarea?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.deleteTask(position);
                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backGroundCornerOffset = 20;

        icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_delete_24);
        background = new ColorDrawable(Color.RED);
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX<0) {
            int iconLeft = itemView.getRight() - iconMargin-icon.getIntrinsicWidth();
            int iconRigh = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRigh, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) -backGroundCornerOffset, itemView.getTop(),
                    itemView.getRight(), itemView.getBottom());
        }
        else{
            background.setBounds(0,0,0,0);
        }
        background.draw(c);
        icon.draw(c);
    }


}

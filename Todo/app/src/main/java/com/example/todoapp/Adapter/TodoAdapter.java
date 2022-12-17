package com.example.todoapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Api.ApiServices;
import com.example.todoapp.Custom.Debouncer;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.TodoModel;
import com.example.todoapp.MyInterface.ITaskListener;
import com.example.todoapp.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<TodoModel> todoList;
    private ITaskListener iTaskListener;
    private Context context;
    private final Debouncer debouncer = new Debouncer();

    public TodoAdapter(List<TodoModel> todoList, ITaskListener listener) {
        this.todoList = todoList;
        this.iTaskListener = listener;
    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        ViewHolder taskHolder = (ViewHolder) holder;
        TodoModel item = todoList.get(position);
        taskHolder.todoDate.setText(item.getTodoDate());
        taskHolder.todoCheck.setChecked(item.getStatus() != 0);
        taskHolder.todoTitle.setText(item.getTodoTitle());
        taskHolder.todoDes.setText(item.getTodoDes());
        taskHolder.todoType.setText(item.getTodoType());

        SwitchStatus(taskHolder, taskHolder.todoCheck.isChecked());

        taskHolder.todoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                debouncer.debounce(Void.class, new Runnable() {
                    @Override
                    public void run() {
                        ApiServices.apiServices.SwitchStatus(item.getId(), new TodoModel(b ? 1 : 0)).enqueue(new Callback<TodoModel>() {
                            @Override
                            public void onResponse(Call<TodoModel> call, Response<TodoModel> response) {
                                SwitchStatus(taskHolder, response.body().getStatus() != 0);
                            }

                            @Override
                            public void onFailure(Call<TodoModel> call, Throwable t) {
                                Toast.makeText(context, "Failed to switch task, please slow down!", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }, 500, TimeUnit.MILLISECONDS);
            }
        });

        taskHolder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iTaskListener.onTaskClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (todoList != null) {
            return todoList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView itemCard;
        LinearLayout dateLayout;
        LinearLayout itemLayout;
        TextView todoDate;
        CheckBox todoCheck;
        TextView todoTitle;
        TextView todoDes;
        TextView todoType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCard = itemView.findViewById(R.id.itemCard);
            dateLayout = itemView.findViewById(R.id.dateLayout);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            todoDate = itemView.findViewById(R.id.todoDate);
            todoCheck = itemView.findViewById(R.id.todoCheck);
            todoTitle = itemView.findViewById(R.id.todoTitle);
            todoDes = itemView.findViewById(R.id.todoDes);
            todoType = itemView.findViewById(R.id.todoType);
        }
    }

    private void SwitchStatus(ViewHolder holder, boolean status) {
        if (status) {
            holder.dateLayout.setBackgroundResource(R.drawable.border_info_itemdoes_checked);
            holder.itemLayout.setBackgroundResource(R.drawable.bg_item_checked);
            holder.todoDate.setPaintFlags(holder.todoDate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.todoDate.setTypeface(holder.todoDate.getTypeface(), Typeface.ITALIC);

            holder.todoTitle.setPaintFlags(holder.todoTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.todoTitle.setTypeface(holder.todoTitle.getTypeface(), Typeface.ITALIC);

            holder.todoDes.setPaintFlags(holder.todoDes.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.todoDes.setTypeface(holder.todoDes.getTypeface(), Typeface.ITALIC);
            holder.todoDes.setTextColor(Color.parseColor("#F5F5F5"));

            holder.todoType.setPaintFlags(holder.todoType.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.todoType.setTypeface(holder.todoType.getTypeface(), Typeface.ITALIC);
        } else {
            holder.dateLayout.setBackgroundResource(R.drawable.border_info_itemdoes);
            holder.itemLayout.setBackgroundResource(R.drawable.bg_item);
            holder.todoDate.setPaintFlags(holder.todoDate.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.todoDate.setTypeface(holder.todoDate.getTypeface(), Typeface.BOLD);

            holder.todoTitle.setPaintFlags(holder.todoTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.todoTitle.setTypeface(holder.todoTitle.getTypeface(), Typeface.BOLD);

            holder.todoDes.setPaintFlags(holder.todoDes.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.todoDes.setTypeface(Typeface.DEFAULT);
            holder.todoDes.setTextColor(Color.parseColor("#ADADAD"));

            holder.todoType.setPaintFlags(holder.todoType.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.todoType.setTypeface(holder.todoType.getTypeface(), Typeface.BOLD);
        }
    }
}

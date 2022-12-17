package com.example.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.todoapp.Adapter.SpinnerAdapter;
import com.example.todoapp.Api.ApiServices;
import com.example.todoapp.Model.SpinnerItem;
import com.example.todoapp.Model.TodoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionTask extends Activity {
    private SpinnerAdapter spinnerAdapter;
    private List<SpinnerItem> spinnerItemList;
    EditText edtTitle;
    EditText edtDesc;
    EditText edtDate;
    Button btnSaveTask;
    Button btnCancel;
    FloatingActionButton btnDelete;
    Spinner spinner;

    private Intent intent = null;
    private Bundle bundle = null;
    private TodoModel task = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_task);
        intent = new Intent();
        bundle = new Bundle();
//        setStyle(STYLE_NORMAL, R.style.DialogStyle);

        InitWidget();
        GetAction();
    }

    private void GetAction() {
        boolean isUpdate = false;

        final Bundle bd = getIntent().getExtras();
        if (bd != null) {
            isUpdate = true;
            btnDelete.setVisibility(View.VISIBLE);
            task = (TodoModel) bd.getSerializable("task");
            edtTitle.setText(task.getTodoTitle());
            edtDesc.setText(task.getTodoDes());
            edtDate.setText(task.getTodoDate());

            SpinnerAdapter adapter = (SpinnerAdapter) spinner.getAdapter();
            int count = adapter.getCount();

            for (int i = 1; i < count; i++) {
                SpinnerItem item = (SpinnerItem) adapter.getItem(i);

                if (item.getName().equals(task.getTodoType())) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
        // TODO: Handle edit todo
        boolean finalIsUpdate = isUpdate;
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodoModel newTodo = new TodoModel();
                newTodo.setStatus(0);
                newTodo.setTodoTitle(edtTitle.getText().toString());
                newTodo.setTodoDes(edtDesc.getText().toString());
                newTodo.setTodoDate(edtDate.getText().toString());
                newTodo.setTodoType(((SpinnerItem) spinner.getSelectedItem()).getName());

                if (!finalIsUpdate) {
                    intent = new Intent();
                    bundle = new Bundle();
                    HandleAddTodo(newTodo);
                } else {
                    newTodo.setId(task.getId());
                    intent = new Intent();
                    bundle = new Bundle();
                    HandleUpdateTodo(String.valueOf(task.getId()), newTodo);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandleDeleteTodo(task.getId());
            }
        });
    }

    private void InitWidget() {
        edtTitle = findViewById(R.id.edtTitle);
        edtDesc = findViewById(R.id.edtDesc);
        edtDate = findViewById(R.id.edtDate);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel = findViewById(R.id.btnCancel);
        btnDelete = findViewById(R.id.btnDelete);
        spinner = findViewById(R.id.spinner);

        spinnerItemList = new ArrayList<>();
        spinnerItemList.add(new SpinnerItem("All", R.drawable.folder_all));
        spinnerItemList.add(new SpinnerItem("Personal", R.drawable.black_folder));
        spinnerItemList.add(new SpinnerItem("Work", R.drawable.brown_folder));
        spinnerItemList.add(new SpinnerItem("Playing", R.drawable.white_folder));
        spinnerItemList.add(new SpinnerItem("Home", R.drawable.dark_blue_folder));
        spinnerItemList.add(new SpinnerItem("Technical", R.drawable.green_folder));
        spinnerItemList.add(new SpinnerItem("Studies", R.drawable.pink_folder));
        spinnerItemList.add(new SpinnerItem("School", R.drawable.red_folder));
        spinnerItemList.add(new SpinnerItem("Market", R.drawable.teal_folder));
        spinnerItemList.add(new SpinnerItem("Others", R.drawable.yellow_folder));

        spinnerAdapter = new SpinnerAdapter(this, R.layout.spinner_item, spinnerItemList);
        spinner.setAdapter(spinnerAdapter);
    }

    public void HandleAddTodo(TodoModel todoModel) {
        bundle.putString("cmd", "add");

        ApiServices.apiServices.AddTask(todoModel).enqueue(new Callback<TodoModel>() {
            @Override
            public void onResponse(Call<TodoModel> call, Response<TodoModel> response) {
                bundle.putSerializable("response", response.body());
                bundle.putInt("status", 200);
                OnFinish();
            }

            @Override
            public void onFailure(Call<TodoModel> call, Throwable t) {
                bundle.putInt("status", 400);
                OnFinish();
            }
        });
    }

    public void HandleUpdateTodo(String id, TodoModel todoModel) {
        bundle.putString("cmd", "update");

        ApiServices.apiServices.UpdateTask(id, todoModel).enqueue(new Callback<TodoModel>() {
            @Override
            public void onResponse(Call<TodoModel> call, Response<TodoModel> response) {
                bundle.putSerializable("response", response.body());
                bundle.putInt("status", 200);
                OnFinish();
            }

            @Override
            public void onFailure(Call<TodoModel> call, Throwable t) {
                bundle.putInt("status", 400);
                OnFinish();
            }
        });
    }

    private void HandleDeleteTodo(String id) {
        bundle.putString("cmd", "delete");

        ApiServices.apiServices.DeleteTask(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                bundle.putSerializable("response", task);
                bundle.putInt("status", 200);
                OnFinish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                bundle.putInt("status", 400);
                OnFinish();
            }
        });
    }

    private void OnFinish() {
        intent.putExtra("data", bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
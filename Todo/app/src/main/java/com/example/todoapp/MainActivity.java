package com.example.todoapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.todoapp.Adapter.SpinnerAdapter;
import com.example.todoapp.Adapter.TodoAdapter;
import com.example.todoapp.Custom.Debouncer;
import com.example.todoapp.Custom.PaginationScrollListener;
import com.example.todoapp.Model.ResponseModel;
import com.example.todoapp.Model.SpinnerItem;
import com.example.todoapp.Model.TodoModel;
import com.example.todoapp.Api.ApiServices;
import com.example.todoapp.MyInterface.ITaskListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private NestedScrollView scrollView;
    private CardView itemCardLoading;
    private RecyclerView tasksRecyclerView;
    private EditText edtSearch;

    private Spinner spinnerFil, spinnerStatus;
    private SpinnerAdapter spinnerFilAdapter, spinnerStatusAdapter;

    private TodoAdapter todoAdapter;
    private List<TodoModel> todoList;

    private int totalPage = 1;
    private int currentPage = 1;
    private int limitPage = 10;
    private final Debouncer debouncer = new Debouncer();

    private FloatingActionButton btnAddNew;

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Bundle bundle;
                    TodoModel todo;
                    int i;
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        bundle = result.getData().getBundleExtra("data");
                        String cmd = bundle.getString("cmd");
                        todo = (TodoModel) bundle.getSerializable("response");
                        final int count = todoList.size();
                        switch (cmd) {
                            case "add":
                                todoList.add(todo);
                                todoAdapter.notifyItemInserted(todoList.size() - 1);
                                switch (bundle.getInt("status")) {
                                    case 200:
                                        Toast.makeText(MainActivity.this, "Insert item is successful!", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 400:
                                        Toast.makeText(MainActivity.this, "Failed to add new task", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                                break;
                            case "update":
                                for (i = 0; i < count; i++) {
                                    if (String.valueOf(todoList.get(i).getId()).equals(todo.getId())) {
                                        todoList.remove(i);
                                        break;
                                    }
                                }
                                todoList.add(i, todo);
                                todoAdapter.notifyItemChanged(i);
                                switch (bundle.getInt("status")) {
                                    case 200:
                                        Toast.makeText(MainActivity.this, "Update item is successful task", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 400:
                                        Toast.makeText(MainActivity.this, "Failed to update task", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                                break;
                            case "delete":
                                for (i = 0; i < count; i++) {
                                    if (String.valueOf(todoList.get(i).getId()).equals(todo.getId())) {
                                        todoList.remove(i);
                                        break;
                                    }
                                }
                                todoAdapter.notifyItemRemoved(i);
                                switch (bundle.getInt("status")) {
                                    case 200:
                                        Toast.makeText(MainActivity.this, "Remove task is successful", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 400:
                                        Toast.makeText(MainActivity.this, "Failed to delete task", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                                break;
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView = findViewById(R.id.scroll_view);
        itemCardLoading = findViewById(R.id.itemCardLoading);
        tasksRecyclerView = findViewById(R.id.todoList);
        spinnerFil = findViewById(R.id.spinnerFil);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnAddNew = findViewById(R.id.btnAddNew);
        edtSearch = findViewById(R.id.edtSearch);

        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        todoList = new ArrayList<>();

        todoAdapter = new TodoAdapter(todoList, new ITaskListener() {
            @Override
            public void onTaskClick(TodoModel todo) {
                onTaskClickAction(todo);
            }
        });
        tasksRecyclerView.setAdapter(todoAdapter);

        spinnerFilAdapter = new SpinnerAdapter(this, R.layout.spinner_item, SpinnerItem.TypeOptions());
        spinnerStatusAdapter = new SpinnerAdapter(this, R.layout.spinner_item, SpinnerItem.StatusOptions());
        spinnerFil.setAdapter(spinnerFilAdapter);
        spinnerStatus.setAdapter(spinnerStatusAdapter);


        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityResultLauncher.launch(new Intent(MainActivity.this, ActionTask.class));
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    currentPage++;
                    if (currentPage < totalPage) {
                        itemCardLoading.setVisibility(View.VISIBLE);
                        GetAllTasks();
                    }
                }
            }
        });

        spinnerFil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentPage = 1;
                todoList.clear();
                todoAdapter.notifyDataSetChanged();
                GetAllTasks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentPage = 1;
                todoList.clear();
                todoAdapter.notifyDataSetChanged();
                GetAllTasks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currentPage = 1;
                todoList.clear();
                todoAdapter.notifyDataSetChanged();
                GetAllTasks();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void GetAllTasks() {
        Map<String, String> data = new HashMap<>();
        data.put("_page", String.valueOf(currentPage));
        data.put("_limit", String.valueOf(limitPage));
        data.put("type", ((SpinnerItem) spinnerFil.getSelectedItem()).getName());
        data.put("status", ((SpinnerItem) spinnerStatus.getSelectedItem()).getName());
        data.put("search", edtSearch.getText().toString());

        ApiServices.apiServices.GetAllTasks(data).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel body = response.body();
                if (body != null && body.getData() != null && body.getData().size() > 0) {
                    itemCardLoading.setVisibility(View.GONE);

                    totalPage = body.getPagination().get_totalPages();
                    todoList.addAll(body.getData());

                    todoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to get all task", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onTaskClickAction(TodoModel todo) {
        Intent intent = new Intent(MainActivity.this, ActionTask.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("task", todo);
        intent.putExtras(mBundle);
        mActivityResultLauncher.launch(intent);
    }

}
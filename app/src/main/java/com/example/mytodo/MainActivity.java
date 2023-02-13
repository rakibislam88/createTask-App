package com.example.mytodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import kotlin.jvm.internal.Lambda;

public class MainActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> arr = new ArrayList();
    HashMap<String, String> hashMap;
    public String ap="";
    EditText planing, note, category;
    Button createToDo, cancel, ok;
    RecyclerView recyclerView;
    MyAdapter adapter;
    Dialog dialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        createToDo = findViewById(R.id.create_todo);
        recyclerView = findViewById(R.id.task_cyclerView);
        dialog = new Dialog(this);


        createToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask();
                cancel = dialog.findViewById(R.id.cancel);
                ok = dialog.findViewById(R.id.ok);
                planing = dialog.findViewById(R.id.planedit);
                note = dialog.findViewById(R.id.note);
                category = dialog.findViewById(R.id.category);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String planString = planing.getText().toString();
                        Calendar cal = Calendar.getInstance();

                        String monthArr[] = {"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
                        String mn="", h="";
                        String ca = String.valueOf(cal.get(Calendar.YEAR));
                        int m = cal.get(Calendar.MONTH);
                        String dm = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                        int hi = cal.get(Calendar.HOUR);
                        int minute= cal.get(Calendar.MINUTE);
                        String min= String.valueOf(cal.get(Calendar.MINUTE));
                        int a = cal.get(Calendar.AM);
                        showTime(hi, minute);
                        for (int i=0; i<12; i++){
                            if (i<m)
                                mn = monthArr[i];
                        }
                        //if (hi==0) h="12";


                        String timedate = mn+""+dm+", "+String.valueOf(hi)+":"+min+" "+ap;

                        String noteString = note.getText().toString();
                        String categoryString = category.getText().toString();

                        String url = "https://raquib.000webhostapp.com/apps/todo.php?plan="+planString +"&time="+timedate+"&note="+noteString+"&category="+categoryString;
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                toDo();
                                dialog.dismiss();

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                createToDo.setText(error.getMessage());
                            }
                        });
                        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                        queue.add(stringRequest);

                    }

                });


            }
        });

        toDo();

    }

    private void toDo() {
        arr = new ArrayList<>();
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, "https://raquib.000webhostapp.com/apps/todoArr.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int id = Integer.parseInt(jsonObject.getString("id"));
                        String plan = jsonObject.getString("plan");
                        String time = jsonObject.getString("time");
                        String note = jsonObject.getString("note");
                        String category = jsonObject.getString("category");
                        hashMap = new HashMap<>();
                        hashMap.put("id", String.valueOf(id));
                        hashMap.put("plan", plan);
                        hashMap.put("time", time);
                        hashMap.put("note", note);
                        hashMap.put("category", category);
                        arr.add(hashMap);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                adapter = new MyAdapter();
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue2 = Volley.newRequestQueue(this);
        queue2.add(arrayRequest);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = getLayoutInflater();
                View v = layoutInflater.inflate(R.layout.layout2, parent, false);
                return new ViewHolder(v);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

                HashMap<String, String> map = arr.get(position);
                holder.noteT.setText(map.get("note"));
                holder.time.setText(map.get("time"));
                String t = map.get("time");
                String id = map.get("id");


                holder.update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.setContentView(R.layout.layout3);



                        Button button = dialog.findViewById(R.id.ok), cancel = dialog.findViewById(R.id.cancel);
                        EditText planing, note, category;
                        planing = dialog.findViewById(R.id.planedit);
                        note    = dialog.findViewById(R.id.note);
                        category = dialog.findViewById(R.id.category);
                        planing.setText(map.get("plan"));
                        note.setText(map.get("note"));
                        category.setText(map.get("category"));

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String p = planing.getText().toString();
                                String n = note.getText().toString();
                                String c = category.getText().toString();

                                String updateurl = "https://raquib.000webhostapp.com/apps/update.php?id="+id+"&plan="+p+"&time="+t+"&note="+n+"&category="+c;
                                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, updateurl, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        toDo();
                                        dialog.dismiss();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                RequestQueue queue3 = Volley.newRequestQueue(MainActivity.this);
                                queue3.add(stringRequest2);
                                    }
                                });
                        dialog.show();
                    }
                });

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String deleteurl = "https://raquib.000webhostapp.com/apps/delete.php?id="+id;
                        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, deleteurl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                toDo();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        RequestQueue queue4 = Volley.newRequestQueue(MainActivity.this);
                        queue4.add(stringRequest3);
                    }
                });



                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity2.p = map.get("plan");
                        MainActivity2.t = map.get("time");
                        MainActivity2.n = map.get("note");
                        MainActivity2.c = map.get("category");
                        startActivity(new Intent(MainActivity.this, MainActivity2.class));
                    }
                });

            }

            @Override
            public int getItemCount() {
                return arr.size();
            }

            public class ViewHolder extends RecyclerView.ViewHolder {
                TextView noteT, time;
                CheckBox checkBox;
                LinearLayout layout;

                Button update, delete;
                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    noteT = itemView.findViewById(R.id.noteT);
                    time = itemView.findViewById(R.id.timeT);
                    checkBox = itemView.findViewById(R.id.checkbox);
                    layout = itemView.findViewById(R.id.layoutofNote);
                    update = itemView.findViewById(R.id.update);
                    delete = itemView.findViewById(R.id.delete);
                }
            }
        }

    private void createTask() {
        dialog.setContentView(R.layout.layout);

        dialog.show();
    }

    public void showTime(int hour, int min) {
        if (hour == 00) {
            hour += 12;
            ap = "AM";
        } else if (hour == 12) {
            ap = "PM";
        } else if (hour > 12) {
            hour -= 12;
            ap = "PM";
        } else {
            ap = "AM";
        }


    }
}
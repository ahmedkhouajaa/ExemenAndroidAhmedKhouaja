package com.example.exemenahmed;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddData extends AppCompatActivity {

    private EditText nameEditText, descriptionEditText;
    private Button saveButton, viewDataButton;
    private ListView dataListView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);


        db = FirebaseFirestore.getInstance();


        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);
        viewDataButton = findViewById(R.id.viewDataButton);
        dataListView = findViewById(R.id.dataListView);

        nameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        descriptionEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        descriptionEditText.setMaxLines(5);
        descriptionEditText.setSingleLine(false);


        saveButton.setOnClickListener(v -> saveData());


        viewDataButton.setOnClickListener(v -> loadData());
    }

    private void saveData() {
        // Get the text inputted by the user
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        // Validate the input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description)) {
            Toast.makeText(AddData.this, "Please enter both name and description.", Toast.LENGTH_SHORT).show();
            return;
        }

        DataModel dataModel = new DataModel(name, description);

        db.collection("data")  // Replace "data" with your desired collection name
                .add(dataModel)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddData.this, "Data saved successfully", Toast.LENGTH_SHORT).show();

                    nameEditText.setText("");
                    descriptionEditText.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddData.this, "Error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadData() {
        db.collection("data")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> dataList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            DataModel dataModel = documentSnapshot.toObject(DataModel.class);
                            String data = "Name: " + dataModel.getName() + "\nDescription: " + dataModel.getDescription();
                            dataList.add(data);
                        }


                        if (dataList.isEmpty()) {
                            Toast.makeText(AddData.this, "No data found.", Toast.LENGTH_SHORT).show();
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddData.this, android.R.layout.simple_list_item_1, dataList);
                        dataListView.setAdapter(adapter);
                    } else {
                        Log.e("AddData", "Error loading data", task.getException());
                        Toast.makeText(AddData.this, "Error loading data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("AddData", "Error loading data", e);
                    Toast.makeText(AddData.this, "Error loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public static class DataModel {
        private String name;
        private String description;
        private String id;

        public DataModel() {
        }

        // Constructor with parameters
        public DataModel(String name, String description) {
            this.name = name;
            this.description = description;
        }

        // Getters and setters
        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

}
package com.example.exemenahmed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<AddData.DataModel> {

    private final Context context;
    private final List<AddData.DataModel> dataList;
    private final FirebaseFirestore db;

    public CustomAdapter(Context context, List<AddData.DataModel> dataList) {
        super(context, R.layout.list_item, dataList);
        this.context = context;
        this.dataList = dataList;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        AddData.DataModel dataModel = dataList.get(position);

        // Set the item text
        TextView itemText = convertView.findViewById(R.id.itemText);
        itemText.setText("Name: " + dataModel.getName() + "\nDescription: " + dataModel.getDescription());

        // Set the delete button listener
        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> deleteItem(position));

        return convertView;
    }

    private void deleteItem(int position) {
        AddData.DataModel dataModel = dataList.get(position);

        // Delete the item from Firestore using the ID
        db.collection("data")
                .document(dataModel.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    dataList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error deleting item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

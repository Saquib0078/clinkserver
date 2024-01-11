package com.example.mytabdemoapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytabdemoapp.databinding.ActivityContestBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContestActivity extends AppCompatActivity {
    ActivityContestBinding binding;
    private DatabaseReference userReference;
    private FirebaseAuth auth;
    ContestAdapter contestAdapter;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        DatabaseReference contestsRef = FirebaseDatabase.getInstance().getReference().child("contests");
        List<ContestModel> contestList = new ArrayList<>();

        contestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contestList.clear();

                for (DataSnapshot contestSnapshot : dataSnapshot.getChildren()) {
                    ContestModel contest = contestSnapshot.getValue(ContestModel.class);
                    if (contest != null) {
                        contestList.add(contest);
                    }
                }

                contestAdapter = new ContestAdapter(contestList);
                binding.contextrecview.setAdapter(contestAdapter);
                contestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ContestActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
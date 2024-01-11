package com.example.mytabdemoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.mytabdemoapp.databinding.UserprofileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
    FirebaseAuth auth;
    UserprofileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=UserprofileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
      auth=FirebaseAuth.getInstance();
       String uid=auth.getCurrentUser().getUid();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRefrence=reference.child("CreateUser");

        userRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        if (uid.equals(userSnapshot.child("uid").getValue(String.class))) {
                            String name = userSnapshot.child("name").getValue(String.class);
                            String email = userSnapshot.child("email").getValue(String.class);
                            String phone = userSnapshot.child("phone").getValue(String.class);
                            int wallet = userSnapshot.child("wallet").getValue(Integer.class);
                             String key=userSnapshot.child("key").getValue(String.class);

                             Constants.setKEY(key);
                              binding.username.setText(name);
                              binding.email.setText(email);
                              binding.phone.setText(phone);
                              binding.walletBalance.setText(String.valueOf(wallet));
                        }
                    }
                } else {
                    Toast.makeText(UserProfile.this, "not exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur
            }
        });

    }
}
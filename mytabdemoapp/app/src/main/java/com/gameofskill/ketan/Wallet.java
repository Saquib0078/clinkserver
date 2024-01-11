package com.example.mytabdemoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mytabdemoapp.databinding.ActivityWalletBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Wallet extends AppCompatActivity {
   ActivityWalletBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth= FirebaseAuth.getInstance();
        String uid=auth.getCurrentUser().getUid();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRefrence=reference.child("CreateUser");
LoadingDialog loadingDialog=new LoadingDialog(this);
        binding.logoutButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Wallet.this);
            builder.setTitle("Logout")
                    .setMessage("Really want to Logout")
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss()).setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Wallet.this, Authentication.class));
                        finish();

                    });

            builder.show();
        });

        binding.addcash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Wallet.this,CreateDeposit.class));
            }
        });
        binding.withdrawamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Wallet.this,WithdrawActivity.class));
            }
        });
loadingDialog.show();
        userRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        if (uid.equals(userSnapshot.child("uid").getValue(String.class))) {
                            int wallet = userSnapshot.child("wallet").getValue(Integer.class);


                            binding.amount.setText(String.valueOf(wallet));
                        }
                    }
                } else {
                    Toast.makeText(Wallet.this, "not exists", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
loadingDialog.dismiss();            }
        });
    }
}
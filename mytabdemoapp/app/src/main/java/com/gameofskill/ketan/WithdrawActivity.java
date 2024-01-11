package com.example.mytabdemoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mytabdemoapp.databinding.ActivityWithdrawBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class WithdrawActivity extends AppCompatActivity {
ActivityWithdrawBinding binding;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("CreateUser");
        String uid = auth.getCurrentUser().getUid();
        LoadingDialog loadingDialog=new LoadingDialog(this);
        binding.withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                if (uid.equals(userSnapshot.child("uid").getValue(String.class))) {
                                    int wallet = userSnapshot.child("wallet").getValue(Integer.class);
                                    String key = userSnapshot.getKey();
                                    String upiid = binding.uipid.getText().toString();
                                    String amount = binding.amount.getText().toString();

                                    // Validate and handle the input amount
                                    if (amount.isEmpty()) {
                                        // Handle the case where the input is empty
                                        Toast.makeText(WithdrawActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                        return; // Exit the loop and return
                                    }

                                    int am = Integer.parseInt(amount);
                                    if (am < 50) {
                                        // Perform an action when the amount is less than 50
                                        Toast.makeText(WithdrawActivity.this, "Amount is less than 50", Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                        return; // Exit the loop and return
                                    }

                                    if (!upiid.isEmpty()) {
                                        int withdrawalAmount = Integer.parseInt(amount);
                                        if (wallet < 50) {
                                            // Wallet balance is less than 50
                                            Toast.makeText(WithdrawActivity.this, "Withdraw Amount is Less Than 50", Toast.LENGTH_SHORT).show();
                                        } else if (withdrawalAmount > wallet) {
                                            // Withdrawal amount is greater than the wallet balance
                                            Toast.makeText(WithdrawActivity.this, "Insufficient balance for withdrawal", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Create a Firebase reference for withdrawal requests
                                            DatabaseReference withdrawRef = FirebaseDatabase.getInstance().getReference().child("Withdraw-Request");

                                            // Generate a unique key for the new withdrawal request
                                            String withdrawKey = withdrawRef.push().getKey();

                                            // Create a new withdrawal request object
                                            WithdrawModel withdrawModel = new WithdrawModel();
                                            withdrawModel.setUpiId(upiid);
                                            withdrawModel.setAmount(withdrawalAmount);
                                            withdrawModel.setKey(withdrawKey);

                                            // Push the new withdrawal request to Firebase with the generated key
                                            withdrawRef.child(key).child(withdrawKey).setValue(withdrawModel);

                                            // Deduct the withdrawal amount from the wallet
                                            int newWalletBalance = wallet - withdrawalAmount;

                                            // Update the wallet balance in the user's data
                                            userSnapshot.getRef().child("wallet").setValue(newWalletBalance);

                                            // Optionally, show a success message
                                            Toast.makeText(WithdrawActivity.this, "Withdraw Request Sent successfully", Toast.LENGTH_SHORT).show();

                                            // Clear input fields
                                            binding.uipid.setText("");
                                            binding.amount.setText("");
                                        }
                                    } else {
                                        // Handle the case where the UPI ID is empty
                                        Toast.makeText(WithdrawActivity.this, "Please enter a valid UPI ID", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(WithdrawActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database errors
                        loadingDialog.dismiss();
                        Toast.makeText(WithdrawActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
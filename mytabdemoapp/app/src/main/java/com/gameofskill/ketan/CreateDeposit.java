package com.example.mytabdemoapp;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mytabdemoapp.databinding.ActivityCreateDepositBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateDeposit extends AppCompatActivity {
    ActivityCreateDepositBinding binding;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateDepositBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("CreateUser");
        String uid = auth.getCurrentUser().getUid();

        LoadingDialog loadingDialog=new LoadingDialog(this);

        binding.textcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CreateDeposit.this.CLIPBOARD_SERVICE);
                String textToCopy = "bhowate.ketan@ybl";
                ClipData clipData = ClipData.newPlainText("label", textToCopy);
                clipboardManager.setPrimaryClip(clipData);


                Toast.makeText(CreateDeposit.this, "Upi Id Copied", Toast.LENGTH_SHORT).show();
            }
        });
        binding.deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String key = null; // Initialize key
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                if (uid.equals(userSnapshot.child("uid").getValue(String.class))) {
                                    key = userSnapshot.getKey();
                                    break;
                                }
                            }

                            if (key != null) {
                                String txnid = binding.txnid.getText().toString();
                                String upiid = binding.uipid.getText().toString();
                                String amount = binding.amount.getText().toString();

                                if (txnid.length() < 12) {
                                    Toast.makeText(CreateDeposit.this, "Reference Id Should be Only 12 numbers", Toast.LENGTH_SHORT).show();
                                } else {
                                    checkTransactionIdAndProceed(key, txnid, upiid, amount);
                                }
                            } else {
                                Toast.makeText(CreateDeposit.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                            Handler handler = new Handler();

// Define a Runnable to dismiss the loading dialog
                            Runnable dismissRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.dismiss();
                                }
                            };

// Set a delay (in milliseconds) after which the dialog will be dismissed
                            int delayMillis = 2000; // 5000 milliseconds (5 seconds)

// Post the Runnable with the specified delay
                            handler.postDelayed(dismissRunnable, delayMillis);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(CreateDeposit.this,""+ databaseError, Toast.LENGTH_SHORT).show();

                        loadingDialog.dismiss();
                    }
                });
            }
        });
    }

    private void checkTransactionIdAndProceed(String key, String txnid, String upiid, String amount) {
        DatabaseReference userDepositRef = FirebaseDatabase.getInstance().getReference().child("User-Deposit");

        userDepositRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean transactionIdExists = false;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (userSnapshot.child(txnid).exists()) {
                        // The transaction ID already exists for a user
                        transactionIdExists = true;
                        binding.txnid.setText("");
                        break;
                    }
                }

                if (transactionIdExists) {
                    // The transaction ID already exists, show an error
                    Toast.makeText(CreateDeposit.this, "Transaction ID already exists", Toast.LENGTH_SHORT).show();
                } else {

                    DepositModel newDeposit = new DepositModel();
                    newDeposit.setTransactionId(txnid);
                    newDeposit.setUpiId(upiid);
                    newDeposit.setAmount(amount);

                    userDepositRef.child(key).child(txnid).setValue(newDeposit);

                    // Show a success toast
                    Toast.makeText(CreateDeposit.this, "Deposited successfully", Toast.LENGTH_SHORT).show();
                    binding.txnid.setText("");
                    binding.uipid.setText("");
                    binding.amount.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur
            }
        });
    }

}
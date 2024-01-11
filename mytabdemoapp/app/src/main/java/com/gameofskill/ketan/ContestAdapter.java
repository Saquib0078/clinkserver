package com.example.mytabdemoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytabdemoapp.databinding.ItemContestBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.ContestViewHolder> {
    private List<ContestModel> items;
    private TournamentModel tournamentModel;
    private Context context;
    FirebaseAuth auth;
    static String contestKey;

    public ContestAdapter(List<ContestModel> items) {
        this.items = items;
    }

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRefrence = reference.child("CreateUser");

    private boolean isJoined = false; // Initially, not joined

    public void setButtonState(boolean joined) {
        isJoined = joined;
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }

    @NonNull
    @Override
    public ContestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemContestBinding binding = ItemContestBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ContestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContestViewHolder holder, int position) {
        auth = FirebaseAuth.getInstance();


        final ContestModel model = items.get(position);
        holder.binding.entry.setText(String.valueOf(model.getEntry()));
        holder.binding.pricepool.setText(String.valueOf(model.getPoolprize()));
        holder.binding.contestlimit.setText(String.valueOf(model.getContestLimit()));
        holder.binding.spotsRemaining.setText(String.valueOf(model.getJoinedUsers()));


        holder.binding.joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = auth.getCurrentUser().getUid();

                userRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                if (userId.equals(userSnapshot.child("uid").getValue(String.class))) {
                                    String Userkey = userSnapshot.getKey();
                                    int wallet = userSnapshot.child("wallet").getValue(Integer.class);
                                    final String contestId = model.getKey(); // Contest ID
                                    String matchId = Constants.getMATCHID();
                                    DatabaseReference contestRef = FirebaseDatabase.getInstance().getReference()
                                            .child("contests").child(contestId);
                                    DatabaseReference contestMatchesRef = FirebaseDatabase.getInstance().getReference()
                                            .child("contests-joined")
                                            .child(contestId)
                                            .child("matches")
                                            .child(matchId)
                                            .child("users").child(Userkey);
                                    Toast.makeText(context, "hello"+contestRef.child("name").equals(userSnapshot.child(Userkey).child("name")), Toast.LENGTH_SHORT).show();
//                                    if(contestRef.child("name").equals(userSnapshot.child(Userkey).child("name")))
                                    contestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                String userName = userSnapshot.child("name").getValue(String.class);

                                                int ContestLimit = snapshot.child("contestLimit").getValue(Integer.class);
                                                int JoinedUsers = snapshot.child("joinedUsers").getValue(Integer.class);
                                                DatabaseReference contestJoinedRef = FirebaseDatabase.getInstance().getReference()
                                                        .child("contests-joined")
                                                        .child(contestId)
                                                        .child("matches")
                                                        .child(matchId)
                                                        .child("users");
//                                                contestJoinedRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                                        if (snapshot.exists()) {
//
//                                                            // Iterate through joined users to check if the user has already joined
//                                                            for (DataSnapshot joinedSnapshot : snapshot.getChildren()) {
//                                                                if (Userkey.equals(joinedSnapshot.getKey())) {
//
//                                                                    holder.binding.joinbtn.setText("Joined");
//                                                                    holder.binding.joinbtn.setBackgroundColor(Color.GRAY);
//                                                                }
//                                                            }
//
//
//                                                        }
//                                                    }
//
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                                    }
//                                                });
                                                if (JoinedUsers < ContestLimit) {
                                                    if (model.getEntry() <= wallet) {
                                                        int newAmount = wallet - model.getEntry();
                                                        userSnapshot.getRef().child("wallet").setValue(newAmount);

                                                        // Assuming contestMatchesRef and contestRef are defined correctly
                                                        contestMatchesRef.child("name").setValue(userName);
//                                                        contestMatchesRef.child("points").setValue(Constants.getPoints());
                                                        int newJoined = JoinedUsers + 1;
                                                        contestRef.child("joinedUsers").setValue(newJoined);


//                                                        contestJoinedRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                                if(snapshot.exists()){
//                                                                    for(DataSnapshot joinedSnapshot:snapshot.getChildren()){
//                                                                        if(Userkey.equals(joinedSnapshot.getKey())){
//                                                                            holder.binding.joinbtn.setText("Joined"); // Set the button text
//                                                                            holder.binding.joinbtn.setBackgroundColor(Color.GRAY);
//                                                                        }
//                                                                    }
//                                                                }
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                                            }
//                                                        });



                                                        Toast.makeText(context, "Contest Joined Successfully", Toast.LENGTH_SHORT).show();
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                        builder.setMessage("Contest Joined Successfully You Have Joined Contest of Rs"+""+model.getEntry())
                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        // Handle the "OK" button click if needed
                                                                        dialog.dismiss(); // Close the dialog
                                                                    }
                                                                });

// Create and show the AlertDialog
                                                        builder.create().show();

                                                    } else {
                                                        Toast.makeText(context, "Recharge Your Wallet", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(context, "Contest is Full", Toast.LENGTH_SHORT).show();
                                                }

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


//

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ContestViewHolder extends RecyclerView.ViewHolder {
        ItemContestBinding binding;

        public ContestViewHolder(ItemContestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

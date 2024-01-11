package com.example.mytabdemoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytabdemoapp.databinding.ItemContestBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRefrence=reference.child("CreateUser");
    DatabaseReference contestsRef = FirebaseDatabase.getInstance().getReference().child("contests");

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
        auth=FirebaseAuth.getInstance();

        String uid=auth.getCurrentUser().getUid();

      final  ContestModel model = items.get(position);
        holder.binding.entry.setText(String.valueOf(model.getEntry()));
        holder.binding.pricepool.setText(String.valueOf(model.getPoolprize()));


        holder.binding.joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    userRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    String userUid = userSnapshot.child("uid").getValue(String.class);
                                    if (uid.equals(userUid)) {
                                        int amount = userSnapshot.child("wallet").getValue(Integer.class);

                                        if (model.getEntry() <= amount) {
                                            amount -= model.getEntry();
                                            userSnapshot.getRef().child("joined").setValue(true);
                                            userSnapshot.getRef().child("wallet").setValue(amount);

                                            // Mark the user as joined in SharedPreferences
                                            String contestKey = model.getKey();
                                            String matchid=Constants.getMATCHID();
                                            // Push the user ID to the specific contest
                                            DatabaseReference contestMatchesRef = FirebaseDatabase.getInstance().getReference().child("contests-joined").child(contestKey).child("matches");
                                            contestMatchesRef.child(matchid).child(userSnapshot.getKey()).child("joined").setValue(true);
                                            holder.binding.joinbtn.setText("Joined");
                                            holder.binding.joinbtn.setBackgroundColor(Color.GRAY);

//userid  contestid  tournamentmodel.getkey


                                        } else {
                                            Toast.makeText(context, "Recharge your wallet", Toast.LENGTH_SHORT).show();
                                            context.startActivity(new Intent(context, Wallet.class));
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle any errors that occur
                        }
                    });

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "key:::"+contestKey, Toast.LENGTH_SHORT).show();
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

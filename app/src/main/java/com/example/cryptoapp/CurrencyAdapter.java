package com.example.cryptoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder>{

    private Context context;
    private List<Currency> currencyList;
    private Map<String, String> faves;
    FirebaseUser user;
    DatabaseReference dr;

    public CurrencyAdapter(Context context, List<Currency> currencyList) {
        this.context = context;
        this.currencyList = currencyList;
        faves = new HashMap<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dr = FirebaseDatabase.getInstance().getReference("users");
    }
    @NonNull

    @Override
    public CurrencyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_item, parent, false);
        return new CurrencyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyHolder holder, int position) {
        Currency currency = currencyList.get(position);
        holder.name.setText(currency.getName());
        holder.name.setTag(currency.getId());
        holder.status.setText(currency.getStatus());
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    public class CurrencyHolder extends RecyclerView.ViewHolder {
        TextView name, status;
        ImageButton fave;
        public CurrencyHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            fave = itemView.findViewById(R.id.fave);
            fave.setTag("unfavorite");
            fave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((String) fave.getTag()).equals("favorite")) {
                        fave.setImageResource(R.drawable.ic_unfavorites);
                        fave.setTag("unfavorite");
                        faves.remove(name.getTag().toString());
                        dr.child(user.getUid()).child("favorites").setValue(faves);
                    }
                    else {
                        fave.setImageResource(R.drawable.ic_favorites);
                        fave.setTag("favorite");
                        faves.put(name.getTag().toString(), name.getText().toString());
                        dr.child(user.getUid()).child("favorites").setValue(faves);
                    }
                }
            });
        }
    }
}

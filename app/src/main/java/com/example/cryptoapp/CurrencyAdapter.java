package com.example.cryptoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder> implements Filterable {

    private Context context;
    private List<Currency> currencyList;
    private List<Currency> currencyListFiltered;
    private Map<String, String> faves;
    private Boolean isFave;
    FirebaseUser user;
    DatabaseReference dr;

    public CurrencyAdapter(Context context, List<Currency> currencyList, Map<String, String> map, Boolean curfav) {
        this.context = context;
        this.currencyList = currencyList;
        currencyListFiltered = new ArrayList<>(this.currencyList);
        faves = map;
        isFave = curfav;
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
        Currency currency = currencyListFiltered.get(position);
        holder.name.setText(currency.getName());
        holder.name.setTag(currency.getId());
        holder.status.setText(currency.getStatus());
        holder.status.setTag(currency);
        if (faves.containsKey(currency.getId())) {
            holder.fave.setImageResource(R.drawable.ic_favorites);
            holder.fave.setTag("favorite");
        }
        else {
            holder.fave.setImageResource(R.drawable.ic_unfavorites);
            holder.fave.setTag("unfavorite");
        }
    }

    @Override
    public int getItemCount() {
        return currencyListFiltered.size();
    }

    public class CurrencyHolder extends RecyclerView.ViewHolder {
        TextView name, status;
        ImageButton fave;
        public CurrencyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            fave = itemView.findViewById(R.id.fave);

            fave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((String) fave.getTag()).equals("favorite")) {
                        fave.setImageResource(R.drawable.ic_unfavorites);
                        fave.setTag("unfavorite");
                        faves.remove(name.getTag().toString());
                        currencyListFiltered.remove(status.getTag());
                    }
                    else {
                        fave.setImageResource(R.drawable.ic_favorites);
                        fave.setTag("favorite");
                        faves.put(name.getTag().toString(), name.getText().toString());
                    }
                    dr.child(user.getUid()).child("favorites").setValue(faves);
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return currencyFilter;
    }

    private Filter currencyFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Currency> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filterList.addAll(currencyList);
            } else {
                String filterstr = charSequence.toString().toLowerCase().trim();

                for (Currency row: currencyList) {
                    if (row.getName().toLowerCase().contains(filterstr) || row.getId().toLowerCase().contains(filterstr)) {
                        filterList.add(row);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            currencyListFiltered.clear();
            currencyListFiltered.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}

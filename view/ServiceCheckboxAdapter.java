package com.example.tiemchuixe.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.example.tiemchuixe.R;
import com.example.tiemchuixe.model.DichVu;

public class ServiceCheckboxAdapter extends RecyclerView.Adapter<ServiceCheckboxAdapter.ServiceViewHolder> {

    private List<DichVu> serviceList;
    private List<DichVu> selectedServices;
    private Context context;

    public ServiceCheckboxAdapter(Context context, List<DichVu> serviceList, List<DichVu> selectedServices) {
        this.context = context;
        this.serviceList = serviceList;
        this.selectedServices = selectedServices;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_checkbox, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        DichVu service = serviceList.get(position);
        holder.checkBoxService.setText(service.getTenDichVu());
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.textViewServicePrice.setText(formatter.format(service.getGiaTien()));

        holder.checkBoxService.setChecked(selectedServices.contains(service));

        holder.checkBoxService.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedServices.contains(service)) {
                    selectedServices.add(service);
                }
            } else {
                selectedServices.remove(service);
            }
            // Notify activity to update total price
            if (context instanceof OnServiceSelectedListener) {
                ((OnServiceSelectedListener) context).onServiceSelectionChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxService;
        TextView textViewServicePrice;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxService = itemView.findViewById(R.id.checkBoxService);
            textViewServicePrice = itemView.findViewById(R.id.textViewServicePrice);
        }
    }

    public interface OnServiceSelectedListener {
        void onServiceSelectionChanged();
    }
} 
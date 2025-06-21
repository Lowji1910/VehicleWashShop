package com.example.tiemchuixe.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.tiemchuixe.R;
import com.example.tiemchuixe.controller.DichVuDAO;
import com.example.tiemchuixe.model.DichVu;
import com.example.tiemchuixe.view.AddEditServiceActivity;
import com.example.tiemchuixe.view.LoginActivity;
import com.example.tiemchuixe.view.ServiceExpandableListAdapter;

public class ServiceListActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private Button buttonAddService;
    private DichVuDAO dichVuDAO;
    private ServiceExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<DichVu>> listDataChild;
    private String loggedInVaiTro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        expandableListView = findViewById(R.id.expandableListView);
        buttonAddService = findViewById(R.id.buttonAddService);

        dichVuDAO = new DichVuDAO(this);
        loggedInVaiTro = LoginActivity.getLoggedInVaiTro(this);

        // Only show Add button if user is QuanLy
        if (!"QuanLy".equals(loggedInVaiTro)) {
            buttonAddService.setVisibility(View.GONE);
        }

        buttonAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceListActivity.this, AddEditServiceActivity.class);
                startActivity(intent);
            }
        });

        prepareListData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareListData();
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        dichVuDAO.open();
        List<DichVu> allServices = dichVuDAO.getAllDichVu();
        dichVuDAO.close();

        // Group services by vehicle type
        Map<String, List<DichVu>> servicesByType = new HashMap<>();
        for (DichVu service : allServices) {
            String vehicleType = service.getLoaiXe();
            // Skip services with null vehicle type to prevent NullPointerException during sorting
            if (vehicleType == null) {
                continue; 
            }
            if (!servicesByType.containsKey(vehicleType)) {
                servicesByType.put(vehicleType, new ArrayList<>());
            }
            servicesByType.get(vehicleType).add(service);
        }

        // Add headers and child data
        List<String> sortedHeaders = new ArrayList<>();
        for (String header : servicesByType.keySet()) {
            if (header != null) {
                sortedHeaders.add(header);
            }
        }
        java.util.Collections.sort(sortedHeaders);

        for (String vehicleType : sortedHeaders) {
            listDataHeader.add(vehicleType);
            listDataChild.put(vehicleType, servicesByType.get(vehicleType));
        }

        listAdapter = new ServiceExpandableListAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(listAdapter);

        // Only allow editing if user is QuanLy
        if ("QuanLy".equals(loggedInVaiTro)) {
            expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
                DichVu selectedService = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                Intent intent = new Intent(ServiceListActivity.this, AddEditServiceActivity.class);
                intent.putExtra("maDV", selectedService.getMaDV());
                startActivity(intent);
                return true;
            });
        }
    }
} 
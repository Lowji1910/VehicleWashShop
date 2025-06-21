package com.example.tiemchuixe.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.tiemchuixe.view.TicketAdapter;
import com.example.tiemchuixe.controller.PhieuRuaXeDAO;
import com.example.tiemchuixe.model.PhieuRuaXe;
import android.widget.Toast;
import android.widget.ListView;
import com.example.tiemchuixe.R;

public class ListTicketActivity extends AppCompatActivity implements TicketAdapter.OnTicketClickListener {
    private RecyclerView recyclerView;
    private TicketAdapter adapter;
    private PhieuRuaXeDAO phieuRuaXeDAO;
    private List<PhieuRuaXe> ticketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ticket);

        // Thêm nút back
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_list_ticket);
        }

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTickets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo DAO
        phieuRuaXeDAO = new PhieuRuaXeDAO(this);
        phieuRuaXeDAO.open();

        // Khởi tạo danh sách và adapter
        ticketList = new ArrayList<>();
        adapter = new TicketAdapter(this, ticketList, phieu -> {
            // Mở màn hình chi tiết phiếu với startActivityForResult
            Intent intent = new Intent(this, TicketDetailActivity.class);
            intent.putExtra("MA_PHIEU", phieu.getMaPhieu());
            startActivityForResult(intent, 1);
        });
        recyclerView.setAdapter(adapter);

        // Load dữ liệu
        loadTickets();
    }

    private void loadTickets() {
        ticketList = phieuRuaXeDAO.getTicketsByStatusExcludingCompleted("Hoàn thành");
        adapter.updateData(ticketList);
    }

    @Override
    public void onTicketClick(PhieuRuaXe phieu) {
        // TODO: Implement ticket detail view
        // Intent intent = new Intent(this, TicketDetailActivity.class);
        // intent.putExtra("MA_PHIEU", phieu.getMaPhieu());
        // startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (phieuRuaXeDAO != null) {
            phieuRuaXeDAO.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Cập nhật lại danh sách phiếu
            loadTicketList();
        }
    }

    private void loadTicketList() {
        phieuRuaXeDAO.open();
        List<PhieuRuaXe> phieuList = phieuRuaXeDAO.getTicketsByStatusExcludingCompleted("Hoàn thành");
        phieuRuaXeDAO.close();
        adapter.updateData(phieuList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại danh sách khi quay lại màn hình
        loadTicketList();
    }

    private void getAllTickets() {
        List<PhieuRuaXe> ticketList = phieuRuaXeDAO.getAllTickets();
        // Lọc bỏ các phiếu đã hoàn thành
        List<PhieuRuaXe> filteredList = new ArrayList<>();
        for (PhieuRuaXe ticket : ticketList) {
            if (!ticket.getTrangThai().equals("Hoàn thành")) {
                filteredList.add(ticket);
            }
        }
        adapter.updateData(filteredList);
    }
} 
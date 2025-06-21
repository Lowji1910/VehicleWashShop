package com.example.tiemchuixe.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;
import com.example.tiemchuixe.R;
import com.example.tiemchuixe.model.PhieuRuaXe;
import com.example.tiemchuixe.controller.PhieuRuaXeDAO;

public class RevenueActivity extends AppCompatActivity {
    private RadioGroup radioGroupPeriod;
    private Button buttonSelectDate;
    private TextView textViewTotalRevenue;
    private RecyclerView recyclerViewCompletedTickets;
    private TextView emptyView;
    private Date selectedDate;
    private SimpleDateFormat dateFormat;
    private DecimalFormat currencyFormat;
    private PhieuRuaXeDAO phieuRuaXeDAO;
    private TicketAdapter ticketAdapter;
    private NumberFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        // Khởi tạo các view
        radioGroupPeriod = findViewById(R.id.radioGroupPeriod);
        buttonSelectDate = findViewById(R.id.buttonSelectDate);
        textViewTotalRevenue = findViewById(R.id.textViewTotalRevenue);
        recyclerViewCompletedTickets = findViewById(R.id.recyclerViewCompletedTickets);
        emptyView = findViewById(R.id.emptyView);

        // Khởi tạo DAO
        phieuRuaXeDAO = new PhieuRuaXeDAO(this);

        // Khởi tạo các định dạng
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        currencyFormat = new DecimalFormat("#,### VNĐ");
        formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // Thiết lập RecyclerView
        recyclerViewCompletedTickets.setLayoutManager(new LinearLayoutManager(this));
        ticketAdapter = new TicketAdapter(this, new ArrayList<>(), null);
        recyclerViewCompletedTickets.setAdapter(ticketAdapter);

        // Mặc định chọn ngày hiện tại
        selectedDate = new Date();
        updateDateButtonText();

        // Xử lý sự kiện chọn ngày
        buttonSelectDate.setOnClickListener(v -> showDatePickerDialog());

        // Xử lý sự kiện thay đổi khoảng thời gian
        radioGroupPeriod.setOnCheckedChangeListener((group, checkedId) -> updateRevenueAndCompletedTickets());

        // Cập nhật doanh thu và danh sách phiếu
        updateRevenueAndCompletedTickets();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (phieuRuaXeDAO != null) {
            phieuRuaXeDAO.close();
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                selectedDate = calendar.getTime();
                updateDateButtonText();
                updateRevenueAndCompletedTickets();
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDateButtonText() {
        String formattedDate = dateFormat.format(selectedDate);
        buttonSelectDate.setText(formattedDate);
    }

    private void updateRevenueAndCompletedTickets() {
        phieuRuaXeDAO.open();
        double totalRevenue = 0;
        List<PhieuRuaXe> completedTickets = new ArrayList<>();

        // Lấy ngày được chọn
        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.setTime(selectedDate);

        // Format ngày tháng theo định dạng yyyy-MM-dd cho SQLite
        SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sqlMonthFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        SimpleDateFormat sqlYearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        String selectedDateStr = sqlDateFormat.format(selectedCalendar.getTime());
        String selectedMonthStr = sqlMonthFormat.format(selectedCalendar.getTime());
        String selectedYearStr = sqlYearFormat.format(selectedCalendar.getTime());

        if (radioGroupPeriod.getCheckedRadioButtonId() == R.id.radioButtonDaily) {
            totalRevenue = phieuRuaXeDAO.getDailyRevenue(selectedDateStr);
            completedTickets = phieuRuaXeDAO.getCompletedTicketsForDate(selectedDateStr);
        } else if (radioGroupPeriod.getCheckedRadioButtonId() == R.id.radioButtonMonthly) {
            totalRevenue = phieuRuaXeDAO.getMonthlyRevenue(selectedMonthStr);
            completedTickets = phieuRuaXeDAO.getCompletedTicketsForMonth(selectedMonthStr);
        } else if (radioGroupPeriod.getCheckedRadioButtonId() == R.id.radioButtonYearly) {
            totalRevenue = phieuRuaXeDAO.getYearlyRevenue(selectedYearStr);
            completedTickets = phieuRuaXeDAO.getCompletedTicketsForYear(selectedYearStr);
        }

        // Hiển thị tổng doanh thu
        textViewTotalRevenue.setText("Tổng doanh thu: " + formatter.format(totalRevenue));

        // Hiển thị danh sách phiếu hoàn thành
        if (completedTickets.isEmpty()) {
            recyclerViewCompletedTickets.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerViewCompletedTickets.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            ticketAdapter.updateData(completedTickets);
        }

        phieuRuaXeDAO.close();
    }
} 
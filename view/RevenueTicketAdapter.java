package com.example.tiemchuixe.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import com.example.tiemchuixe.R;
import com.example.tiemchuixe.model.PhieuRuaXe;
import com.example.tiemchuixe.model.KhachHang;
import com.example.tiemchuixe.controller.KhachHangDAO;
import com.example.tiemchuixe.controller.PhieuRuaXeDAO;

public class RevenueTicketAdapter extends RecyclerView.Adapter<RevenueTicketAdapter.TicketViewHolder> {
    private List<PhieuRuaXe> ticketList;
    private Context context;
    private KhachHangDAO khachHangDAO;
    private PhieuRuaXeDAO phieuRuaXeDAO;
    private NumberFormat formatter;
    private SimpleDateFormat dateFormat;

    public RevenueTicketAdapter(Context context, List<PhieuRuaXe> ticketList) {
        this.context = context;
        this.ticketList = ticketList;
        this.khachHangDAO = new KhachHangDAO(context);
        this.phieuRuaXeDAO = new PhieuRuaXeDAO(context);
        this.formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("vi", "VN"));
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_revenue_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        PhieuRuaXe ticket = ticketList.get(position);
        KhachHang khachHang = khachHangDAO.getKhachHangById(ticket.getMaKH());

        holder.textViewBienSoXe.setText(ticket.getBienSoXe());
        holder.textViewKhachHang.setText(khachHang != null ? 
            khachHang.getTenKhachHang() + " - " + khachHang.getSoDienThoai() : "Không có thông tin");
        holder.textViewNgayTao.setText(ticket.getNgayTao());
        holder.textViewTotalAmount.setText(formatter.format(ticket.getTongTien()));
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public void updateData(List<PhieuRuaXe> newList) {
        this.ticketList = newList;
        notifyDataSetChanged();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBienSoXe;
        TextView textViewKhachHang;
        TextView textViewNgayTao;
        TextView textViewTotalAmount;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBienSoXe = itemView.findViewById(R.id.textViewBienSoXe);
            textViewKhachHang = itemView.findViewById(R.id.textViewKhachHang);
            textViewNgayTao = itemView.findViewById(R.id.textViewNgayTao);
            textViewTotalAmount = itemView.findViewById(R.id.textViewTotalAmount);
        }
    }
} 
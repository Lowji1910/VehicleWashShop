package com.example.tiemchuixe.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import com.example.tiemchuixe.R;
import com.example.tiemchuixe.model.PhieuRuaXe;
import com.example.tiemchuixe.model.KhachHang;
import com.example.tiemchuixe.controller.KhachHangDAO;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private List<PhieuRuaXe> ticketList;
    private Context context;
    private KhachHangDAO khachHangDAO;
    private OnTicketClickListener listener;

    public interface OnTicketClickListener {
        void onTicketClick(PhieuRuaXe phieu);
    }

    public TicketAdapter(Context context, List<PhieuRuaXe> ticketList, OnTicketClickListener listener) {
        this.context = context;
        this.ticketList = ticketList;
        this.listener = listener;
        this.khachHangDAO = new KhachHangDAO(context);
        this.khachHangDAO.open();
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phieu_rua_xe, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        PhieuRuaXe phieu = ticketList.get(position);
        KhachHang khachHang = khachHangDAO.getKhachHangById(phieu.getMaKH());

        holder.textViewBienSoXe.setText(phieu.getBienSoXe());
        holder.textViewKhachHang.setText(khachHang != null ? khachHang.getTenKhachHang() : "Không xác định");
        holder.textViewNgayTao.setText(phieu.getNgayTao());
        
        // Format số tiền theo định dạng tiền tệ Việt Nam
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.textViewTongTien.setText(formatter.format(phieu.getTongTien()));
        
        // Hiển thị trạng thái với màu sắc tương ứng
        String trangThai = phieu.getTrangThai();
        switch (trangThai) {
            case "Chưa hoàn thành":
                holder.textViewTrangThai.setText(R.string.status_waiting);
                holder.textViewTrangThai.setBackgroundResource(android.R.color.holo_orange_light);
                break;
            case "Đang rửa":
                holder.textViewTrangThai.setText(R.string.status_washing);
                holder.textViewTrangThai.setBackgroundResource(android.R.color.holo_blue_light);
                break;
            case "Hoàn thành":
                holder.textViewTrangThai.setText(R.string.status_completed);
                holder.textViewTrangThai.setBackgroundResource(android.R.color.holo_green_light);
                break;
            case "Đã hủy":
                holder.textViewTrangThai.setText(R.string.status_cancelled);
                holder.textViewTrangThai.setBackgroundResource(android.R.color.holo_red_light);
                break;
            default:
                holder.textViewTrangThai.setBackgroundResource(android.R.color.darker_gray);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTicketClick(phieu);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public void updateData(List<PhieuRuaXe> newList) {
        this.ticketList = newList;
        notifyDataSetChanged();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBienSoXe, textViewKhachHang, textViewNgayTao, textViewTongTien, textViewTrangThai;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBienSoXe = itemView.findViewById(R.id.textViewBienSoXe);
            textViewKhachHang = itemView.findViewById(R.id.textViewKhachHang);
            textViewNgayTao = itemView.findViewById(R.id.textViewNgayTao);
            textViewTongTien = itemView.findViewById(R.id.textViewTongTien);
            textViewTrangThai = itemView.findViewById(R.id.textViewTrangThai);
        }
    }
} 
package com.example.oldwounds.adapter;

import android.content.pm.PackageManager;
import android.hardware.camera2.params.OisSample;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oldwounds.R;
import com.example.oldwounds.domain.OneTimeDetails;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by Politness Chen on 2019/10/23--14:44
 * desc:
 */
public class UseTimeDetailAdapter extends RecyclerView.Adapter<UseTimeDetailAdapter.ViewHolder> {

    private List<OneTimeDetails> mOneTimeDetailInfoList;
    private PackageManager packageManager;

    public UseTimeDetailAdapter(ArrayList<OneTimeDetails> mOneTimeDetailInfoList) {
        this.mOneTimeDetailInfoList = mOneTimeDetailInfoList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        packageManager = parent.getContext().getPackageManager();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.used_time_detail_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OneTimeDetails oneTimeDetails = mOneTimeDetailInfoList.get(position);
        holder.tv_index.setText(position + 1 + "");
        try {
            holder.iv_app_icon.setImageDrawable(packageManager.getApplicationIcon(mOneTimeDetailInfoList.get(position).getPkgName()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        holder.tv_start_use_time.setText(mOneTimeDetailInfoList.get(position).getStartTime());
        holder.tv_stop_use_time.setText(mOneTimeDetailInfoList.get(position).getStopTime());
        long time = mOneTimeDetailInfoList.get(position).getUseTime()/1000;
        holder.tv_total_use_time.setText(time +"s / " + DateUtils.formatElapsedTime(time));
    }

    @Override
    public int getItemCount() {
        return mOneTimeDetailInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_index;
        ImageView iv_app_icon;
        TextView tv_start_use_time;
        TextView tv_stop_use_time;
        TextView tv_total_use_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_index = itemView.findViewById(R.id.tv_index);
            iv_app_icon = itemView.findViewById(R.id.iv_app_icon);
            tv_start_use_time = itemView.findViewById(R.id.tv_start_use_time);
            tv_stop_use_time = itemView.findViewById(R.id.tv_stop_use_time);
            tv_total_use_time = itemView.findViewById(R.id.tv_total_use_time);
        }
    }
}

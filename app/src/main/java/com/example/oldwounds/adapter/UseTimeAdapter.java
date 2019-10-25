package com.example.oldwounds.adapter;

import android.app.usage.UsageStats;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oldwounds.R;
import com.example.oldwounds.domain.UsedInfo;
import com.example.oldwounds.utils.UseTimeDataManager;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by Politness Chen on 2019/10/22--11:28
 * desc:
 */
public class UseTimeAdapter extends RecyclerView.Adapter<UseTimeAdapter.ViewHolder>{

    ArrayList<UsedInfo> mUsedInfoList;
    PackageManager packageManager;
    UseTimeDataManager mUseTimeDataManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UseTimeAdapter(Context context, ArrayList<UsedInfo> mUsedInfoList){
        this.mUsedInfoList = mUsedInfoList;
        mUseTimeDataManager = UseTimeDataManager.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        packageManager = parent.getContext().getPackageManager();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.used_time_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsedInfo info = mUsedInfoList.get(position);
        holder.tv_index.setText(position + 1 + "");
        final String name = info.getmPackageName();
        try {
            holder.iv_app_icon.setImageDrawable(packageManager.getApplicationIcon(name));
            PackageInfo packageInfo = packageManager.getPackageInfo(name,PackageManager.GET_ACTIVITIES);
            //app的名字
            String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            holder.tv_app_name.setText(appName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tv_use_count.setText(info.getmUsedCount() + "");
        long time = getTotalTimeFromUsage(name)/1000;
        holder.tv_use_time.setText(time + "s/" + DateUtils.formatElapsedTime(time));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewIetmClick != null) {
                    onRecyclerViewIetmClick.onItemClick(v,name);
                }
            }
        });
    }

    private OnRecyclerViewIetmClick onRecyclerViewIetmClick;
    //设置监听事件
    public interface OnRecyclerViewIetmClick{
        void onItemClick(View view, String pkg);
    }

    public void setOnItemClickListener(OnRecyclerViewIetmClick listener) {
        this.onRecyclerViewIetmClick = listener;
    }

    @Override
    public int getItemCount() {
        return mUsedInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_index;
        ImageView iv_app_icon;
        TextView tv_app_name;
        TextView tv_use_count;
        TextView tv_use_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_index = itemView.findViewById(R.id.tv_index);
            iv_app_icon = itemView.findViewById(R.id.iv_app_icon);
            tv_use_count = itemView.findViewById(R.id.tv_use_count);
            tv_use_time = itemView.findViewById(R.id.tv_use_time);
            tv_app_name = itemView.findViewById(R.id.tv_app_name);
        }
    }

    /*private long calculateUseTime(ArrayList<OneTimeDetails> list, String pkg) {
        long useTime = 0;
        for (int i = 0; i < list.size() ; i++) {
            if (list.get(i).getPkgName().equals(pkg)) {
                useTime += list.get(i).getUseTime();
            }
        }

        return useTime;
    }*/

    /**
     * 使用的次数
     * @param pkg
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private long getTotalTimeFromUsage(String pkg) {
        UsageStats stats = mUseTimeDataManager.getUsageStats(pkg);
        //对于一个应用，位于前台，被用户使用的总时长
        return stats == null ? 0 : stats.getTotalTimeInForeground();
    }
}

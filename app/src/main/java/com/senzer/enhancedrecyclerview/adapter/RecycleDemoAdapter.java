package com.senzer.enhancedrecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.senzer.enhancedrecyclerview.R;
import com.senzer.enhancedrecyclerview.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * ProjectName: PurchaseRecycleAdapter
 * Description: 购买ItemAdapter
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 2016/8/30 9:47
 */
@SuppressWarnings("ALL")
public class RecycleDemoAdapter extends RecyclerView.Adapter<RecycleDemoAdapter.ItemViewHolder> {
    protected Context mContext;

    // data list
    protected List mDatasource;

    public RecycleDemoAdapter(Context context, List data) {
        this.mContext = context;
        this.mDatasource = data;
    }

    @Override
    public int getItemCount() {
        return (null == mDatasource ? 0 : mDatasource.size());
//        return AppConstants.TEST_LIST_ITEM_COUNT;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 算父类宽度与高度
        View itemView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.list_item_recycle_demo, parent, false);
        // 不算父类宽度与高度
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null);
        ItemViewHolder viewHolder = new ItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        User data = (User) mDatasource.get(position);
        if (null != data) {
            fillData(holder, data, position);
        }
    }

    private void fillData(ItemViewHolder holder, User data, int position) {
        holder.tvName.setText(data.getName());
    }

    /**
     * refresh data and add more data
     *
     * @param datas
     * @param loadMore true: load more data, false: refresh data
     */
    public void setDataSource(List datas, boolean loadMore) {
        if (loadMore) {
            if (null != datas) {
                if (null == mDatasource) {
                    mDatasource = new ArrayList<>();
                }

                mDatasource.addAll(datas);
            }
        } else {
            mDatasource = datas;
        }

        notifyDataSetChanged();
//        notifyItemRangeChanged(0, getItemCount());
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}

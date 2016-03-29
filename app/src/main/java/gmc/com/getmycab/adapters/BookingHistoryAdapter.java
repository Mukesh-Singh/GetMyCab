package gmc.com.getmycab.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.bean.BookingHistoryBean;

/**
 * Created by Baba on 9/30/2015.
 */
public class BookingHistoryAdapter extends BaseAdapter {
    private final Context mContext;
    List<BookingHistoryBean> mDataList;
    private LayoutInflater mInflater;

    public BookingHistoryAdapter (Context context,List<BookingHistoryBean>  list){
        mContext=context;
        mInflater = (LayoutInflater) this.mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDataList=list;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.booking_history_row, null);

            holder.mAmt=(TextView)convertView.findViewById(R.id.booking_history_row_amt);
            holder.mDate=(TextView)convertView.findViewById(R.id.booking_history_row_date);
            holder.mDestination=(TextView)convertView.findViewById(R.id.booking_history_row_destination);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        BookingHistoryBean b= mDataList.get(position);
        holder.mAmt.setText("Adv. Paid: "+b.getAdvance());
        holder.mDestination.setText("Booking ID: #"+b.getOrderId());
        holder.mDate.setText(""+ AppUtil.formatedDate(b.getDate())+"");
        //holder.advance.setText("Adv. Paid: "+b.getAdvance());

        return convertView;
    }

    public class ViewHolder{
        TextView mDate,mDestination,mAmt,advance;
    }
}

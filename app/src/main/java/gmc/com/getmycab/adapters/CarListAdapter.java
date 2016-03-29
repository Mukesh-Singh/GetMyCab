package gmc.com.getmycab.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.activity.BookingConfirmationActivity;
import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.activity.CabSearchResultActivity;
import gmc.com.getmycab.bean.Car;

/**
 * Created by mukesh on 7/9/15.
 */
public class CarListAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    List<Car> carList;
    public CarListAdapter(Context context, List<Car> carList){
        mContext=context;
        mInflater = (LayoutInflater) this.mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.carList=carList;

    }
    @Override
    public int getCount() {
        return carList.size();
    }

    @Override
    public Car getItem(int i) {
        return carList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.cab_search_result_row_item, null);
            holder.carName=(TextView)convertView.findViewById(R.id.cab_search_result_item_cab_name);
            holder.perhorprice=(TextView)convertView.findViewById(R.id.cab_search_result_item_rate);
            holder.seat=(TextView)convertView.findViewById(R.id.cab_search_result_item_seat);
            holder.totalprice=(TextView)convertView.findViewById(R.id.cab_search_result_item_total);
            holder.faredetails=(TextView)convertView.findViewById(R.id.cab_search_result_item_fare_details);
            holder.booknow=(TextView)convertView.findViewById(R.id.cab_search_result_item_booknow);

            holder.farePerkm =(TextView)convertView.findViewById(R.id.booking_confirmation_price_fareperkkm);
            holder.estimatedCharge =(TextView)convertView.findViewById(R.id.booking_confirmation_price_estimatedkmCharged);
            holder.additionalCoset =(TextView)convertView.findViewById(R.id.booking_confirmation_price_additionalCost);
            holder.distanceDetails =(TextView)convertView.findViewById(R.id.booking_confirmation_price_distanceDetails);
            holder.noteText =(TextView)convertView.findViewById(R.id.booking_confirmation_price_notes);
            //holder.priceDetailsLayou=(LinearLayout)convertView.findViewById(R.id.booking_confirmation_price_detailslayout);
            holder.estimatedDistance=(TextView)convertView.findViewById(R.id.booking_confirmation_estimateddistance);
            holder.faredetailsLayout=(LinearLayout)convertView.findViewById(R.id.booking_confirmation_price_detailslayout);
            holder.distanceDetailsLayout=(LinearLayout)convertView.findViewById(R.id.layout_distance_details_ll);
            holder.cabfareLayout =(LinearLayout)convertView.findViewById(R.id.cab_fair_details_ll);

            holder.cab_fareperkm=(LinearLayout)convertView.findViewById(R.id.booking_confirmation_details_cabFareDetails_FarePerKmTitle);
            holder.cab_estimatedkm=(LinearLayout)convertView.findViewById(R.id.booking_confirmation_details_cabFareDetails_estimatedKmCharged);
            holder.cab_fixedcost=(LinearLayout)convertView.findViewById(R.id.fixed_cost_layout);
            holder.fixedcosttext=(TextView)convertView.findViewById(R.id.fixed_cost_text);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.faredetailsLayout.setVisibility(View.GONE);
        Car car= carList.get(position);

        String st="Seats: ("+car.getCarDetails().getNumSeats()+"+1)";
        SpannableString spannable= new SpannableString(st);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 7, st.length(), 0);
        holder.seat.setText(spannable);

        String ac= car.getCarDetails().getAcType();
        String carname=car.getCarDetails().getCarModel();
        if (ac.equalsIgnoreCase("1")){
            carname=carname+" (AC)";
        }
        else
            carname=carname+" (NON AC)";

        holder.carName.setText(carname);

        if (car.getPriceDetails().getFare()!=null && car.getPriceDetails().getFare().length()>0) {
            holder.perhorprice.setText("Rs. " + car.getPriceDetails().getFare() + " per km");
            holder.totalprice.setText("RS. "+car.getPriceDetails().getBasePrice());
        }
        else {
            holder.totalprice.setText("RS. "+car.getPriceDetails().getTotalPrice());
        }




        holder.faredetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(mContext, BookingConfirmationActivity.class);
//                intent.putExtra("position",position);
//                intent.putExtra("from","fare");
//                mContext.startActivity(intent);
                if (holder.faredetailsLayout.getVisibility()==View.VISIBLE) {
                    holder.faredetailsLayout.setVisibility(View.GONE);

                }
                else {

                    holder.faredetailsLayout.setVisibility(View.VISIBLE);
                    if (position==carList.size()-1) {
                        if (mContext instanceof CabSearchResultActivity){
                            CabSearchResultActivity a=(CabSearchResultActivity)mContext;
                            a.scrollList();
                        }
                    }
                }
            }
        });
        holder.booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, BookingConfirmationActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("from","book");
                mContext.startActivity(intent);
            }
        });
        setPriceDetails(holder,car);


        return convertView;
    }


    private class ViewHolder {

      TextView carName,perhorprice,seat,totalprice,faredetails,booknow;
        TextView farePerkm, estimatedCharge, additionalCoset, distanceDetails, noteText;
        TextView estimatedDistance,fixedcosttext;
        LinearLayout faredetailsLayout,distanceDetailsLayout,cabfareLayout;
        LinearLayout cab_fareperkm,cab_estimatedkm,cab_fixedcost;




    }


    private void setPriceDetails(ViewHolder holder,Car car) {
        try {
            holder.distanceDetails.setVisibility(View.GONE);
            holder.distanceDetailsLayout.setVisibility(View.GONE);

            //for out station
            if (car.getPriceDetails().getFare()!=null && car.getPriceDetails().getFare().length()>0) {
                holder.cab_estimatedkm.setVisibility(View.VISIBLE);
                holder.cab_fareperkm.setVisibility(View.VISIBLE);
                holder.cab_fixedcost.setVisibility(View.GONE);
                holder.farePerkm.setText("Rs. " + getReadableString(car.getPriceDetails().getFare()) + " / Km");
                holder.estimatedCharge.setText("" + getReadableString(car.getPriceDetails().getDistance() + ""));
                String addCost = "*    DA Rs " + getReadableString(car.getPriceDetails().getBaseDriverCharge()) + " per Day " +
                        "and if he drives between\n"
                        +"      10pm to 5am then Rs " + getReadableString(car.getPriceDetails().getGmcDriverCharge()) + " per night.\n" +
                        "*    Service Tax @ 5.80%.\n" +
                        "*    Toll tax, State tax, Parking charge as \n"
                        +"      applicable." ;//+
                        //"*    State tax (Estimated State Tax is Rs. " + getReadableString(car.getStateTaxDetails().getStateTax()) + ").";

                holder.additionalCoset.setText(addCost);
                String dist = "";
                for (int i = 0; i < car.getPriceDetails().getDistanceDetails().size(); i++) {
                    dist = dist+car.getPriceDetails().getDistanceDetails().get(i).getSourceCity() + "    to    " +
                            car.getPriceDetails().getDistanceDetails().get(i).getDestinationCity() + "       " + car.getPriceDetails().getDistanceDetails().get(i).getDistance()+" Km\n";
                }
                if (dist.length() > 0) {
                    holder.distanceDetailsLayout.setVisibility(View.VISIBLE);
                    holder.distanceDetails.setVisibility(View.VISIBLE);
                    holder.distanceDetails.setText(dist);
                } else {
                    holder.distanceDetails.setText("");
                    holder.distanceDetails.setVisibility(View.GONE);
                    holder.distanceDetailsLayout.setVisibility(View.GONE);
                    //findViewById(R.id.booking_confirmation_price_distanceDetailsTitle).setVisibility(View.GONE);
                }


//                String fstcity = "";
//                fstcity=  AppUtil.getPreference(AppConstants.CAB_SEARCH_FROM_TO).split("->")[0].replace("Outstation,","");
//                if (car.getPriceDetails().getDistanceDetails() != null && car.getPriceDetails().getDistanceDetails().size() > 0)
//                    fstcity = car.getPriceDetails().getDistanceDetails().get(0).getSourceCity();

                String note = //"*    Driver Allowance would take care of his food and stay.\n"+
                         "*    Min. " + getReadableString(car.getPriceDetails().getMinimumKmPerDay()) + " kms charge per day \n"
                                 +"      from 12 am to 11.59 pm.\n"
                        //+ "*    One day means one Calender day (12 midnight to 12 midnight).\n"
                        + "*    AC will be switched off in hill areas.\n"
                        //+ "*    Local travel will not allowed in " + fstcity + ".\n"
                        + "*    Local travel usage is not allowed.\n";
                        //+ "*    Taxi will not be driven on unpaved roads.";

                holder.noteText.setText(note);
            }
            else {
                //Oneway
                holder.cab_estimatedkm.setVisibility(View.GONE);
                holder.cab_fareperkm.setVisibility(View.GONE);
                holder.cab_fixedcost.setVisibility(View.VISIBLE);

                holder.fixedcosttext.setText("Rs. "+getReadableString(car.getPriceDetails().getTotalPrice()));
                String addCost =
                        "*    Service tax @ 5.80%.\n" +
                        "*    Parking charge as applicable.\n" +
                        "*    Express highway toll (in case of Delhi Agra drop only)";


                holder.additionalCoset.setText(addCost);
                String note = //"*    Extra Pickup / Drop is chargeable at Rs. 500 per point.\n"
                          "*    AC will be switched off in hilly areas.\n"+
                          "*    Extra pick up / drop in certain limit \n"+
                          "      shall be charged @ Rs 500 per point."
                        //+ "*    Taxi will not be driven on unpaved roads.\n"
                        //+ "*    Extra Pickup / Drop point must be in certain limit"
                        ;

                holder.noteText.setText(note);

//                findViewById(R.id.booking_confirmation_details_cabFareDetails_FarePerKmTitle).setVisibility(View.GONE);
//                findViewById(R.id.booking_confirmation_details_cabFareDetails_estimatedKmCharged).setVisibility(View.GONE);




            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    private String getReadableString(String str){
        if (str==null || str.length()==0)
            return "0";
        else
            return str;
    }

}

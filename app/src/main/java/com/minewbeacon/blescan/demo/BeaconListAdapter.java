package com.minewbeacon.blescan.demo;

import com.minewbeacon.blescan.demo.MyAlarmManager.*;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.MinewBeacon;
import com.yuliwuli.blescan.demo.R;

import java.util.ArrayList;
import java.util.List;



public class BeaconListAdapter extends RecyclerView.Adapter<BeaconListAdapter.MyViewHolder> {

    private List<MinewBeacon> mMinewBeacons = new ArrayList<>();
    private double DistanceThreshold = 6.0;


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.main_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setDataAndUi(mMinewBeacons.get(position));
    }

    @Override
    public int getItemCount() {
        if (mMinewBeacons != null) {
            return mMinewBeacons.size();
        }
        return 0;
    }

    public void setData(List<MinewBeacon> minewBeacons) {
        this.mMinewBeacons = minewBeacons;

//        notifyItemRangeChanged(0,minewBeacons.size());
        notifyDataSetChanged();

    }

    public void setItems(List<MinewBeacon> newItems) {
//        validateItems(newItems);


        int startPosition = 0;
        int preSize = 0;
        if (this.mMinewBeacons != null) {
            preSize = this.mMinewBeacons.size();

        }
        if (preSize > 0) {
            this.mMinewBeacons.clear();
            notifyItemRangeRemoved(startPosition, preSize);
        }
        this.mMinewBeacons.addAll(newItems);
        notifyItemRangeChanged(startPosition, newItems.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //private final TextView mDevice_mac;
        private MinewBeacon mMinewBeacon;
        private final TextView mDevice_temphumi;
        private final TextView mDevice_name;
        //private final TextView mDevice_uuid;
        private final TextView mDevice_other;

        double firstRssi = 1.0;
        double secondRssi = 1.0;
        double firstDistance = 0.0;
        double secondDistance = 0.0;


        public double predict(String originDistance,String Rssi) {
            Double distance = Double.parseDouble(originDistance);
            if(firstRssi > 0) {
                firstRssi = Double.parseDouble(Rssi);
                firstDistance = distance;
                return distance;
            } else if(secondRssi > 0) {
                secondRssi = Double.parseDouble(Rssi);
                secondDistance = distance;
                return distance;
            }
            double res = - 1.0;
            if((Double.parseDouble(Rssi) > secondRssi) && (secondRssi > firstRssi)) {
                res = secondDistance - (firstDistance - secondDistance) / 2;
            } else if((Double.parseDouble(Rssi) < secondRssi) && (secondRssi < firstRssi)) {
                res = secondDistance + (secondDistance - firstDistance) / 2;
            }
            firstRssi = secondRssi;
            firstDistance = secondDistance;
            secondRssi = Double.parseDouble(Rssi);
            secondDistance = distance;
            if(res < 0) {
                return distance;
            }
            return res;
        }
        public MyViewHolder(View itemView) {
            super(itemView);
            mDevice_name = (TextView) itemView.findViewById(R.id.device_name);
            //mDevice_uuid = (TextView) itemView.findViewById(R.id.device_uuid);
            mDevice_other = (TextView) itemView.findViewById(R.id.device_other);
            mDevice_temphumi = (TextView) itemView.findViewById(R.id.device_temphumi);
            //mDevice_mac = (TextView) itemView.findViewById(R.id.device_mac);
        }

        public void setDataAndUi(MinewBeacon minewBeacon) {
            mMinewBeacon = minewBeacon;
            mDevice_name.setText(mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue());
            /*mDevice_mac.setText(mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_MAC).getStringValue());
            mDevice_uuid.setText("UUID:" + mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_UUID).getStringValue());
            String battery = mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_BatteryLevel).getStringValue();
            int batt = Integer.parseInt(battery);
            if (batt > 100) {
                batt = 100;
            }*/
            String  distance = MainActivity.getDistance(mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getStringValue());

            double distance_value = Double.parseDouble(distance);
            double estimatedDistance = predict(distance,mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getStringValue());

            double show_distance = Math.floor(distance_value * 10000)/10000;
            double show_estimated = Math.floor(estimatedDistance * 10000)/10000;
            String format = String.format("Rssi:%s Distance:%s Esitimated Distance:%s",
                    mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getStringValue(),
                    show_distance,
                    show_estimated);
            mDevice_other.setText(format);

            if (mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Humidity).getFloatValue() == 0 &&
                    mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Temperature).getFloatValue() == 0) {
                mDevice_temphumi.setVisibility(View.GONE);
            } else {
                mDevice_temphumi.setVisibility(View.VISIBLE);
                String temphumi = String.format("Temperature:%s ℃   Humidity:%s ",
                        mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Temperature).getFloatValue() + "",
                        mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Humidity).getFloatValue() + "");

                mDevice_temphumi.setText(temphumi + "%");
            }
            if(estimatedDistance <= DistanceThreshold) {
                this.itemView.setBackgroundColor(Color.parseColor("#ff0000"));
            } else {
                this.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }
    }
}

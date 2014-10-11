package com.circa.hackzurich.circa;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;

public class InfoActivity extends Activity {
    private TextView mTextView;
    private WearableListView mWearableListView;
    String[] name=null;
    Integer[] image=null;

    Node node;
    GoogleApiClient googleApiClient;
    public static final String START_ACTIVITY_PATH = "/start/SendActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        /*final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });*/

        //googleApiClient = getApi();
        //googleApiClient.connect();

        //node = getNode();
        name= new String[]{"Free Wi-Fi", "Dangerous zone", "Other"};
        image=new Integer[]{R.drawable.common_signin_btn_icon_dark,R.drawable.go_to_phone_00186,
                R.drawable.go_to_phone_00186};

        mWearableListView = (WearableListView) findViewById(R.id.info_list_view);
        //setadapter to listview
        mWearableListView.setAdapter(new InfoWearableListViewAdapter(this));
        //on item click
        mWearableListView.setClickListener(new WearableListView.ClickListener() {
            @Override
            public void onClick(WearableListView.ViewHolder viewHolder) {
                Toast.makeText(getApplicationContext(), "" + viewHolder.getPosition()
                        , Toast.LENGTH_LONG).show();
                sendInfo(viewHolder.getPosition());
                finish();
            }

            @Override
            public void onTopEmptyRegionClick() {

            }
        });
    }

    private GoogleApiClient getApi()
    {
        return new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
    }

    private Node getNode() {
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
        return nodes.getNodes().iterator().next();
    }

    private void sendInfo(int kindId)
    {
        MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                googleApiClient, node.getId(), START_ACTIVITY_PATH, null).await();
        if (!result.getStatus().isSuccess()) {
            Log.e("FUCK", "ERROR: failed to send Message: " + result.getStatus());
        }

    }

    //List View Adapter
    private final class InfoWearableListViewAdapter extends
            WearableListView.Adapter {
        private final Context mContext;
        private final LayoutInflater mInflater;

        private InfoWearableListViewAdapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup
                                                                      parent, int viewType)
        {
            return new WearableListView.ViewHolder(
                    mInflater.inflate(R.layout.info_list_item, null));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder,
                                     int position) {
            TextView view=(TextView)holder.itemView.findViewById(R.id.time_text);
            view.setText(name[position]);
            //holder.itemView.setTag(position);
            ImageView img=(ImageView)holder.itemView.findViewById(R.id.circle);
            img.setImageResource(image[position]);
        }

        @Override
        public int getItemCount() {
            return name.length;
        }
    }
}

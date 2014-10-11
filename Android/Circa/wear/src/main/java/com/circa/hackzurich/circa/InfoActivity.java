package com.circa.hackzurich.circa;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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
    static final String MESSAGE_ID = "/send/new_alert";
    private TextView mTextView;
    private WearableListView mWearableListView;
    String[] name=null;
    Integer[] image=null;

    Node node;
    GoogleApiClient googleApiClient;

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

        new LoginToGoogleTask().execute();
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

    private void sendInfo(final int kindId)
    {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                byte[] bytes = new byte[1];
                bytes[0] = (byte)kindId;
                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                        googleApiClient, node.getId(), MESSAGE_ID, bytes).await();
                if (!result.getStatus().isSuccess()) {
                    Log.e("FUCK", "ERROR: failed to send Message: " + result.getStatus());
                }
                finish();
                return null;
            }
        }.execute();


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

    private class LoginToGoogleTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            googleApiClient = getApi();
            googleApiClient.blockingConnect();


            node = getNode();
            return null;
        }

        protected void onPostExecute(Void result) {
            mWearableListView = (WearableListView) findViewById(R.id.info_list_view);
            //setadapter to listview
            mWearableListView.setAdapter(new InfoWearableListViewAdapter(getBaseContext()));
            name= new String[]{"Free Wi-Fi", "Dangerous zone", "Other"};
            image=new Integer[]{R.drawable.common_signin_btn_icon_dark,R.drawable.go_to_phone_00186,
                    R.drawable.go_to_phone_00186};

            //on item click
            mWearableListView.setClickListener(new WearableListView.ClickListener() {
                @Override
                public void onClick(WearableListView.ViewHolder viewHolder) {
                    Toast.makeText(getApplicationContext(), "" + viewHolder.getPosition()
                            , Toast.LENGTH_LONG).show();
                    sendInfo(viewHolder.getPosition());
                }

                @Override
                public void onTopEmptyRegionClick() {

                }
            });
        }
    }
}

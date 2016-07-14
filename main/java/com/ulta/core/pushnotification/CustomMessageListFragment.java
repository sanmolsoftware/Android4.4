//package com.ulta.core.pushnotification;
//
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.view.View;
//import android.widget.AdapterView;
//
//import com.ulta.R;
//import com.urbanairship.UAirship;
//import com.urbanairship.messagecenter.MessageListFragment;
//import com.urbanairship.messagecenter.MessageViewAdapter;
//import com.urbanairship.richpush.RichPushMessage;
//
//public class CustomMessageListFragment extends MessageListFragment {
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        getAbsListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Handle item clicks
//                UAirship.shared().getInbox().startMessageActivity(getMessage(position).getMessageId());
//            }
//        });
//    }
//
//    @NonNull
//    @Override
//    protected MessageViewAdapter createMessageViewAdapter() {
//
//       // return new MessageViewAdapter(getContext(), R.layout.custom_item_view) {
//            @Override
//            protected void bindView(View view, RichPushMessage message, int position) {
//                // bind the message to the custom view inflated from R.layout.custom_item_view
//            }
//        };
//
//    }
//}
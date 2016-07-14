package com.ulta.core.pushnotification;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.urbanairship.UAirship;
import com.urbanairship.messagecenter.MessageFragment;
import com.urbanairship.richpush.RichPushInbox;
import com.urbanairship.richpush.RichPushMessage;

public class CustomMessageActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String messageId = null;

        // Handle the "com.urbanairship.VIEW_RICH_PUSH_MESSAGE" intent action with the message
        // ID encoded in the intent's data in the form of "message:<MESSAGE_ID>
        if (getIntent() != null && getIntent().getData() != null
                && RichPushInbox.VIEW_MESSAGE_INTENT_ACTION.equals(getIntent().getAction())) {
            messageId = getIntent().getData().getSchemeSpecificPart();
        }

        RichPushMessage message = UAirship.shared().getInbox().getMessage(messageId);
        if (message == null) {
            // Message is no longer available
            finish();
            return;
        }

        if (savedInstanceState == null) {
            MessageFragment messageFragment = MessageFragment.newInstance(messageId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, messageFragment)
                    .commit();
        }

        setTitle(message.getTitle());
    }
}

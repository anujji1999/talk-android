/*
 * Nextcloud Talk application
 *
 * @author Mario Danic
 * Copyright (C) 2017-2018 Mario Danic <mario@lovelyhq.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.nextcloud.talk.adapters.messages;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;

import com.nextcloud.talk.R;
import com.nextcloud.talk.application.NextcloudTalkApplication;
import com.nextcloud.talk.models.json.chat.ChatMessage;
import com.nextcloud.talk.utils.DisplayUtils;
import com.stfalcon.chatkit.messages.MessageHolders;

import java.util.Map;

import androidx.core.view.ViewCompat;

public class MagicSystemMessageViewHolder extends MessageHolders.IncomingTextMessageViewHolder<ChatMessage> {

    public MagicSystemMessageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(ChatMessage message) {
        super.onBind(message);

        Resources resources = NextcloudTalkApplication.getSharedApplication().getResources();

        Drawable bubbleDrawable = DisplayUtils.getMessageSelector(resources.getColor(R.color.white_two),
                resources.getColor(R.color.transparent),
                resources.getColor(R.color.white_two), R.drawable.shape_grouped_incoming_message);
        ViewCompat.setBackground(bubble, bubbleDrawable);

        Spannable messageString = new SpannableString(message.getText());

        Context context = NextcloudTalkApplication.getSharedApplication().getApplicationContext();
        if (message.getMessageParameters() != null && message.getMessageParameters().size() > 0) {
            for (String key : message.getMessageParameters().keySet()) {
                Map<String, String> individualHashMap = message.getMessageParameters().get(key);
                int color;
                if (individualHashMap.get("type").equals("user") || individualHashMap.get("type").equals("guest")) {

                    if (individualHashMap.get("id").equals(message.getActiveUserId())) {
                        color = context.getResources().getColor(R.color.nc_incoming_text_mention_you);
                    } else {
                        color = context.getResources().getColor(R.color.nc_incoming_text_mention_others);
                    }

                    messageString = DisplayUtils.searchAndColor(message.getText(),
                            messageString, "@" + individualHashMap.get("name"), color);
                }
            }
        }

        text.setText(messageString);
    }
}

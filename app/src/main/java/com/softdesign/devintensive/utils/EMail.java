/*
 * Copyright (C) 2013 xDevStudio
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.   
 *  
 * */

package com.softdesign.devintensive.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Class for sending a e-mail using default e-mail client.
 * 
 * @author Mikhail Malakhov
 * 
 * */
public class EMail {

	public static void send(Context context, String mailTo, String subject, 
			String msgText) {
		
		/* Creating Intent for Action ACTION_SENDTO for sending a e-mail */		
		Intent intent = new Intent(Intent.ACTION_SENDTO, 
				Uri.fromParts("mailto", mailTo, null));
		
		/* Adding a Subject to e-mail */
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);

		/* Adding a message body (text) */
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		
		/* Start the Intent for creating e-mail into default e-mail client */
		context.startActivity(intent);
	}	
	
}

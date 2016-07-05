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
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Class for working with a Keyboard.
 *  
 * @author Malakhov_M
 * 
 * */
public class MyKeyboard {
	
	/**
	 * Returns a link to the input method manager object.
	 * 
	 * 	@param context current application context
	 * 
	 *  @return The input method manager object
	 *  
	 * */
	public static InputMethodManager getInputMethodManager(Context context) {
		return (InputMethodManager) context.getSystemService(
				Context.INPUT_METHOD_SERVICE);
	}	
	
	/**
	 * Hide the Keyboard. Request to hide the soft input window from the
     * context of the window that is currently accepting input.
     * 
     * 	@param context current application context 
     * 	@param view the view that is making the request
     *  
	 * */
	public static void hide(Context context, View view) {		
		MyKeyboard.getInputMethodManager(context).hideSoftInputFromWindow(
				view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);	
	}
	
	/**
	 * Show the Keyboard. Explicitly request that the current input method's
     * soft input area be shown to the user.
     * 
     * 	@param context current application context 
     * 	@param view the view that is making the request
     *  
	 * */
	public static void show(Context context, View view) {
		 MyKeyboard.getInputMethodManager(context).showSoftInput(view, 
				InputMethodManager.SHOW_IMPLICIT);
	}	

}

/*
 * Copyright (C) 2014 xDevStudio
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
import android.telephony.SmsManager;

//TODO Нужно проверить!

/**
 * Класс для отпарвки коротких текстовых сообщений (SMS). Сообщения можно 
 * отправлять как через стандартное приложение, так и напрямую.
 * 
 * @author Mikhail Malakhov
 * 
 * */
public class SmsSender {

	/**
	 * Возвращает ссылку на системный объект менеджера sms ({@code SmsManager}).
	 * 
	 * @return Системный объект менеджера sms сообщений.
	 * 
	 * */
	public static SmsManager getManager() { return SmsManager.getDefault(); }

	/**
	 * Отправляет текстовое сообщение на номер {@code number}.
	 * 
	 * @param number номер телефона на который будет отправленно сообщение
	 * @param text текст sms сообщения
	 * 
	 * @return {@code True} если не было ошибок при отправке сообщения.
	 * 
	 * @throws IllegalArgumentException если {@code number} не является номером 
	 * телефона
	 * @throws IllegalArgumentException если {@code text} равен {@code null}
	 *   
	 * */
	public static boolean send(String number, String text) {
		
		/* Проверка номера */
		if (!SmsSender.isNumber(number))
			throw new IllegalArgumentException("Number is invalid");				
		
		/* Проверка текста сообщения */
		if (text == null)
			throw new IllegalArgumentException("Text is null");			
		
		/* Получение ссылки на системный объект менеджера sms */
		SmsManager smsManager = SmsManager.getDefault();

		/* Отправка сообщения */
		try {
			smsManager.sendTextMessage(number, null, text, null, null);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
		
	}
	
	/**
	 * Отправляет текстовые сообщения на номер {@code number}.
	 * 
	 * @param number номер телефона на который будет отправленно сообщение
	 * @param texts массив строк для отправки, каждый элемент массива будет 
	 * отправлен в отдельном sms сообщении
	 * 
	 * @return {@code -1} если не было ошибок при отправке сообщений или индекс 
	 * элемента массива {@code texts}, при отправке которого ошибка возникла.
	 * 
	 * @throws IllegalArgumentException если {@code text} равен {@code null}
	 * @throws IllegalArgumentException если {@code text} пустой массив
	 *   
	 * */
	public static int send(String number, String... texts) {
				
		/* Проверка массива texts на null */
		if (texts == null) 
			throw new IllegalArgumentException("Texts is null");
		
		/* Проверка что массив texts не пустой */
		if (texts.length == 0)
			throw new IllegalArgumentException("Texts is empty");
		
		/* Переменная для хранения результата метода */
		int ret = -1;
		
		/* Отправка сообщений */
		for (int i = 0; i < texts.length; i++)			 
			if (!SmsSender.send(number, texts[i])) {
				ret = i;
				break;
			}
		
		/* Возврат результата */
		return ret;
	}
	
	/**
	 * Отправляет текстовое сообщение на номера {@code numbers}.
	 * 
	 * @param numbers массив номеров телефонов на которые будет отправленно 
	 * сообщение, на каждый из номеров будет отправленно одно сообщение
	 * @param text текст sms сообщения
	 * 
	 * @return {@code -1} если не было ошибок при отправке сообщений или индекс 
	 * элемента массива {@code numbers}, при отправке которого ошибка возникла.
	 * 
	 * @throws IllegalArgumentException если {@code numbers} равен {@code null}
	 * @throws IllegalArgumentException если {@code numbers} пустой массив
	 *   
	 * */	
	public static int sendTo(String text, String... numbers) {
			
		/* Проверка массива номеров на null */
		if (numbers == null)
			throw new IllegalArgumentException("Numbers is null");
		
		/* Проверка что массив номеров не пустой */
		if (numbers.length == 0) 
			throw new IllegalArgumentException("Numbers is empty");
		
		/* Переменная для хранения результата метода */
		int ret = -1;
		
		/* Отправка сообщения на все номера из массива */
		for (int i = 0; i < numbers.length; i++)
			if (!SmsSender.send(numbers[i], text)) {
				ret = i;
				break;
			}
			
		/* Возврат результата */
		return ret;
		
	}
		
	/**
	 * Отправляет текстовое сообщение с помощью другого приложения. 
	 *
	 * @param text текст sms сообщения
	 * @param context текущий контекст
	 * 
	 * @throws IllegalArgumentException если {@code context} равен {@code null}
	 * @throws IllegalArgumentException если {@code text} равен {@code null}
	 * 
	 * */
	public static void send(Context context, String text) {		
		
		/* Checking a context object */
		if (context == null) 
			throw new IllegalArgumentException("Context is null");
		
		/* Checking a text object */
		if (text == null)
			throw new IllegalArgumentException("Text is null");		
		
		/* Create an Intent object */ 
		Intent intent = new Intent(Intent.ACTION_VIEW);			
		
		/* Set Intent Type (to send SMS) */
		intent.setType("vnd.android-dir/mms-sms");
		
		/* Put data to intent */
		intent.putExtra("sms_body", text);
		 
		/* Start Activity */
		context.startActivity(intent);		
		
	}
	
	/**
	 * Проверяет, является ли строка {@code number} номером телефона.
	 * 
	 * @param number строка для проверки
	 * 
	 * @return {@code True} если строка {@code number} является номером 
	 * телефона, иначе {@code false}
	 * 
	 * */
	public static boolean isNumber(String number) {						
		
		/* Переменная для хранения результата метода */
		boolean ret = true;
		
		/* Проверка number на null */
		if (number == null) return !ret;
		
		/* Проверка на отсутствие некорректных символов в строке номена */
		// TODO Добавить проверку строки номера на наличие только цифр
								
		/* Возврат результата */
		return ret;
		
	}

}

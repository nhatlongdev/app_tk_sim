package com.example.computer.thongkesimcard.configs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class GlobalValue {
	public static JSONArray jsonDataSim;
	public static JSONObject jsonSimSelected;
	public static String deviceId="";
	public static String providerId = "";

	/*cake data sim*/
	public static JSONObject jsonDataSimCake = null;

	/*cake data card da nap*/
	public static JSONObject jsonDataCardLoaded = null;

	/*Cache thong tin nap the theo sim*/
	public static JSONObject jsonNapCake = null;

	/*value check t.hop dang chon get card hay get ok card*/
	public static boolean isGetOkCard = false;

	/*bien check trang thai the nap cuoi cung da update server chua*/
	public static boolean isUpdateToServer = true;

	/*cake data suggest card*/
	public static List<String> arrSuggestCodeCard = null;

	/*cake data suggest code customer*/
	public static List<String> arrSuggestCustomer = null;

	/*cake value card current getCard*/
	public static String cardCodeCurrent = "";

	/*biến lưu số card lấy được lần gần nhất*/
	public static int soCardLayLanGanNhat = 0;

}

package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class WorkThread extends Thread {

    private int now_year, now_month, now_day , now_hour, now_minute;

    public void FutureWorkThread(int hour , int minute){
        LocalDate now_time = LocalDate.now();
        int get_year = now_time.getYear();
        int get_month = now_time.getMonthValue();
        int get_day  = now_time.getDayOfMonth();

        now_year = get_year;
        now_month = get_month;
        now_day = get_day;
        now_hour = hour;
        now_minute = minute;
    }

    public void run(){
        try{
            sleep(timeUntil(now_year, now_month, now_day ,now_hour ,now_minute));
            System.out.println("wake up !!!!!!");
            System.out.println("new branch create test 2st");
            System.out.println("new branch create test 3st");
            System.out.println("new branch create test 4st");
            System.out.println("new branch create test 6st merge 충돌 테스트");
        }catch (InterruptedException  e){
            e.getMessage();
        }
    }

    public List<Map<String,Object>> work() throws IOException {
        System.out.println("working ~~~");
        List<Map<String,Object>> resultList = null;
        new Thread(r).start();
        return resultList = getMessage();

    }

    public long timeUntil(int year , int month, int day, int hour , int minute){
        System.out.println(year+" "+month+" "+day+" "+hour+" "+minute);
        Date now = new Date();
        Calendar until = Calendar.getInstance();
        until.set(Calendar.YEAR, year);
        until.set(Calendar.MONTH, month-1);
        until.set(Calendar.DAY_OF_WEEK, day-1);
        until.set(Calendar.HOUR_OF_DAY,hour);
        until.set(Calendar.MINUTE, minute);
        until.set(Calendar.SECOND,0);

        Date until_time = until.getTime();
        long sleep  =  until_time.getTime() - now.getTime();

        long seconds = TimeUnit.MILLISECONDS.toSeconds(sleep);
        System.out.println("wake up time   => " + seconds);
        return  sleep;
    }

    public List<Map<String, Object>> getMessage() throws IOException {
        List<Map<String,Object>> resultList = new ArrayList<Map<String, Object>>();

        StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/6410000/busarrivalservice/v2/getBusArrivalListv2"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("format","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8") ); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("serviceKey","UTF-8") + "=" +"WGQIT5NrsgBmzWYajkZZlOYS9tf8E6yNihUQiFNg3Dvl6mwxhpu0V%2FlzjwuBI6hKvtxpgQuAvOFhyqs90U7cww%3D%3D" ); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("stationId","UTF-8") + "=" + URLEncoder.encode("227000521", "UTF-8")); //정류소 id
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setDoOutput(true);
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;

        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        System.out.println(sb.toString());

        JSONObject object = new JSONObject(sb.toString());
        JSONArray jsonArray = object.getJSONObject("response").getJSONObject("msgBody").getJSONArray("busArrivalList");

        for(int i = 0 ; i<jsonArray.length(); i++){
            Map<String,Object> map = new HashMap<String, Object>();
            System.out.println(jsonArray.get(i));
            object =  jsonArray.getJSONObject(i);
            int busnum = object.getInt("routeName");
            int value = object.getInt("predictTime1");
            System.out.println("busnum:"+ busnum +"/"+ "time"+ value);
            map.put("busnum",busnum);
            map.put("time",value);
            System.out.println("map: "+ map);
            resultList.add(map);
            System.out.println("%%%%%%%%% List = > "+ resultList);
        }
        return resultList;
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(60000);  //1분마다 자동 닫힘
                System.out.println("dispose  => "+ JOptionPane.getRootFrame());
                JOptionPane.getRootFrame().dispose();
                System.out.println("Thread: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };
}

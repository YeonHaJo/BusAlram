package org.example;

import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.rmi.MarshalledObject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class BusAlram extends Thread implements ActionListener {
    List<Map<String,Object>> resultList = null;
    int busnum_first = 0;
    int busnum_second =0;
    int time_value_first =0;
    int time_value_second =0;

    String time = "";
    public BusAlram(){
        JFrame f = new JFrame("집으로");
        f.setSize(400,200);
        f.setLocation(100,100);
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);

        f.setLayout(new FlowLayout(FlowLayout.CENTER,20,30));

        String[]name = {"5시30분","5시45분","6시00분","6시15분","6시30분"};
        for(int i = 0; i<name.length; i++){
            JButton jb =  new JButton(name[i]);
            jb.setSize(140,50);
            jb.addActionListener(this);
            jb.setLocation(100,100);
            f.add(jb);
        }

        f.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        time = e.getActionCommand();
        switch (time) {
            case "5시30분":
                AlramStart(15,44);
                break;
            case "5시45분":
                AlramStart(17,40);
                break;
            case "6시00분":
                AlramStart(17,55);
                break;
            case "6시15분":
                AlramStart(18,10);
                break;
            case "6시30분":
                AlramStart(18,25);
                break;
        }

    }

    public static void main(String[] args) {
        new BusAlram();
    }

    public void AlramStart (int hour, int minute){
        WorkThread workThread = new WorkThread();
        workThread.FutureWorkThread(hour,minute);
        workThread.run();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    LocalTime now = LocalTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm분:ss초");
                    String time = now.format(formatter);
                    resultList = workThread.work();
                    busnum_first =(int) resultList.get(0).get("busnum");
                    time_value_first = (int) resultList.get(0).get("time");
                    busnum_second = (int) resultList.get(1).get("busnum");
                    time_value_second = (int) resultList.get(1).get("time");
                    String message= "현재시각 : "+time+"\n"+"도착 버스: "+ busnum_first +" 번"+ "  /  " + "도착 시간: " + time_value_first +" 분"+"\n" + "도착버스: " + busnum_second +" 번"+ "  /  " + "도착시간: " + time_value_second + " 분" ;
                    JOptionPane pane = new JOptionPane(message,JOptionPane.PLAIN_MESSAGE);
                    JDialog d = pane.createDialog((JFrame)null,"집가자");
                    d.setLocation(1634,865);
                    d.setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 60000); // 1분마다 실행
    }

}

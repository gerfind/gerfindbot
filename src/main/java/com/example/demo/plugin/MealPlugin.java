package com.example.demo.plugin;

import a3lib.SuperPlugin;
import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.event.message.CQPrivateMessageEvent;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class MealPlugin extends SuperPlugin {
    ArrayList places_to_eat = new ArrayList();
    Random r = new Random();
    String message = "恰啥插件用法：\n/meal\n/meal add [餐厅]\n/meal del [餐厅序号]\n/meal list";//[学校英文缩写]\n/meal add [学校英文缩写] [选项名]
    int turn = 1;

    public MealPlugin()
    {
        plugin_name = "MealPlugin";
        try
        {
            FileInputStream fileInputStream = new FileInputStream(new File("data/dining_hall_list.txt"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            int ch;
            String place = bufferedReader.readLine();
            while(place != null)
            {
                places_to_eat.add(place);
                place = bufferedReader.readLine();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int onPrivateMessage(CoolQ cq, CQPrivateMessageEvent event)
    {
        if(!is_enabled)
            return MESSAGE_IGNORE;
        long user_id = event.getUserId();
        return MESSAGE_IGNORE;
    }

    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event)
    {
        if(!is_enabled)
            return MESSAGE_IGNORE;
        String msg = event.getMessage();
        long group_id = event.getGroupId();
        if(msg.length()<5)
            return MESSAGE_IGNORE;
        if(msg.substring(0,5).equals("/meal")){
            String[] args = msg.split(" ");
            if(args.length<2){
                Random r =new Random();
                int hall_number = r.nextInt(places_to_eat.size());
                cq.sendGroupMsg(group_id, "我觉得你下顿饭应该去"+places_to_eat.get(hall_number) + "吃", false);
                return MESSAGE_BLOCK;
            }
            else if(args.length==2){
                switch(args[1]){
                    case "list":
                        String str0 = "";
                        for(int i=0; i< places_to_eat.size(); i++){
                            str0 += places_to_eat.get(i) + " ";
                        }
                        cq.sendGroupMsg(group_id, "当前可以去恰饭的地方：" + str0, false);
                        return MESSAGE_BLOCK;
                    case "save":
                        try {
                            OutputStream f = new FileOutputStream(new File("data/dining_hall_list.txt"));
                            for (int i = 0; i < places_to_eat.size(); i++) {
                                if (i < places_to_eat.size() - 1) {
                                    try {
                                        String str = places_to_eat.get(i) + "\n";
                                        f.write(str.getBytes());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    String str1 = (String) places_to_eat.get(i);
                                    try {
                                        f.write(str1.getBytes());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            try {
                                f.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        cq.sendGroupMsg(group_id, "保存成功", false);
                        return MESSAGE_BLOCK;
                    case "help":
                        cq.sendGroupMsg(group_id,message,false);
                        return MESSAGE_BLOCK;
                }
            }
            else if(args.length==3){
                switch (args[1]){
                    case "add":
                        places_to_eat.add(args[2]);
                        cq.sendGroupMsg(group_id,"成功添加！以后可以去" + args[2] + "恰饭啦！",false);
                        return MESSAGE_BLOCK;
                    case "del":
                            int index = Integer.parseInt(args[2]);
                            String removed_place = (String) places_to_eat.get(index);
                            places_to_eat.remove(index);
                            cq.sendGroupMsg(group_id,"成功删除！以后不能去"+removed_place+"恰饭了呜呜",false);
                            return MESSAGE_BLOCK;
                }
            }
        }
        return MESSAGE_IGNORE;
    }
}

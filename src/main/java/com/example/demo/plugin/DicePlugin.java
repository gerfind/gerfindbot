package com.example.demo.plugin;

import a3lib.SuperPlugin;
import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.event.message.CQPrivateMessageEvent;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DicePlugin extends SuperPlugin {
    Random r = new Random();
    String message = "用法：/dice [🎲面数] [🎲个数](如果无面数默认6面(最大为1000面)，无个数默认1个(最大为100个))";

    public DicePlugin(){
        plugin_name = "RollDice";
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
        /*if(msg.length()<5)
            return MESSAGE_IGNORE;*/
        if(msg.equals("/dice help"))
        {
            cq.sendGroupMsg(group_id, message,false);
            return MESSAGE_BLOCK;
        }
        if(msg.substring(0,5).equals("/dice"))
        {
            String[] args = msg.split(" ");
            if(args.length==1)
            {
                int point = r.nextInt(6);
                cq.sendGroupMsg(group_id,"你扔了 1 个 6 面🎲，其点数为 "+String.valueOf(point+1)+"",false);
                return MESSAGE_BLOCK;
            }
            if(args.length == 2 && Integer.parseInt(args[1])>0 && Integer.parseInt(args[1])<=1000 ){
                int bound = Integer.parseInt(args[1]);
                int point = r.nextInt(Integer.parseInt(args[1]));
                cq.sendGroupMsg(group_id,"你扔了 1 个 "+bound+" 面🎲，其点数为 "+String.valueOf(point+1),false);
                return MESSAGE_BLOCK;
            }
            if(args.length == 3 && Integer.parseInt(args[1])>0 && Integer.parseInt(args[1])<=100 && Integer.parseInt(args[2])>1 && Integer.parseInt(args[2])<=100){
                int bound = Integer.parseInt(args[1]);
                int num = Integer.parseInt(args[2]);
                String out = "";
                int sum = 0;
                for(int i=0; i<num; i++){
                    int point = r.nextInt(bound) + 1;
                    out = out.concat(String.valueOf(point) + " ");
                    sum += point;
                }
                cq.sendGroupMsg(group_id,"你扔了 " + num + " 个 "+bound+ " 面🎲\n" + "其点数分别为：" + out +"\n" + "总和为: " + sum, false);
                return  MESSAGE_BLOCK;
            }
            /*switch(args[1])
            {
                case "dice":
                    int point = r.nextInt(6);
                    int hide = r.nextInt(100);
                    if(hide == 99)
                    {
                        cq.sendGroupMsg(group_id,"恭喜你掷出了隐藏数值114514！\n>  <！",false);
                        return MESSAGE_BLOCK;
                    }
                    cq.sendGroupMsg(group_id,"你掷出的点数O.O是： " + String.valueOf(point+1),false);
                    return MESSAGE_BLOCK;
                case "boom":
                    int alive = r.nextInt(10);
                    if(alive == 0)
                    {
                        cq.sendGroupMsg(group_id,"boom!你死le！\n该轮轮盘进行至第 "+String.valueOf(turn)+" 轮\n/roll boom 开启新一轮俄罗斯转盘>  <",false);
                        turn = 1;
                        return MESSAGE_BLOCK;
                    }
                    cq.sendGroupMsg(group_id,"恭喜你在第 " + String.valueOf(turn) +" 轮轮盘中存活O.O\n/roll boom 进行下一轮> <",false);
                    turn++;
                    return MESSAGE_BLOCK;
                default:
                    cq.sendGroupMsg(group_id, message,false);
                    return MESSAGE_BLOCK;
            }*/
        }
        return MESSAGE_IGNORE;
    }
}

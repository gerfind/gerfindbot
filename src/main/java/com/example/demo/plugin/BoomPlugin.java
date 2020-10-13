package com.example.demo.plugin;

import a3lib.SuperPlugin;
import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.event.message.CQPrivateMessageEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;
import net.lz1998.cq.utils.CQCode;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

@Component
public class BoomPlugin extends SuperPlugin {
    String message = "用法：/boom";
    Random r = new Random();
    int turn = 1;

    public BoomPlugin() {
        plugin_name = "BoomPlugin";
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("data/dining_hall_list.txt"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            int ch;
            String place = bufferedReader.readLine();
            while (place != null) {
                place = bufferedReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onPrivateMessage(CoolQ cq, CQPrivateMessageEvent event) {
        if (!is_enabled)
            return MESSAGE_IGNORE;
        long user_id = event.getUserId();
        return MESSAGE_IGNORE;
    }

    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event) {
        if (!is_enabled)
            return MESSAGE_IGNORE;

        String msg = event.getMessage();
        long group_id = event.getGroupId();
        if (msg.length() < 5)
            return MESSAGE_IGNORE;
        if (msg.equals("/boom help")) {
            cq.sendGroupMsg(group_id, message, false);
            return MESSAGE_BLOCK;
        } else if (msg.substring(0, 5).equals("/boom")) {
            int alive = r.nextInt(6);
            if (alive == 4||alive == 3) {
                cq.sendGroupMsg(group_id, "boom!你死le！\n该轮轮盘进行至第 " + String.valueOf(turn) + " 轮\n/boom 开启新一轮俄罗斯转盘", false);
                turn = 1;
                return MESSAGE_BLOCK;
            }
            cq.sendGroupMsg(group_id, "恭喜你在第 " + String.valueOf(turn) + " 轮轮盘中存活\n/boom 进行下一轮", false);
            turn++;
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }
}

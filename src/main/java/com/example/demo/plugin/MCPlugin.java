package com.example.demo.plugin;
import a3lib.SuperPlugin;
import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.event.message.CQPrivateMessageEvent;
import net.lz1998.cq.robot.CoolQ;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class MCPlugin extends SuperPlugin {
    String message = "MCServer插件用法:/mc [ip:port](若不填默认查询Gerfind的服务器)\n可以查询服务器的在线人数等信息";
    String request_url = "https://api.imlazy.ink/mcapi/json.php?name=Minecraft_Server&host=";
    public MCPlugin() {
        plugin_name = "MineCraftServerPlugin";
    }


    @Override
    public int onPrivateMessage(CoolQ cq, CQPrivateMessageEvent event) {
        if (!is_enabled)
            return MESSAGE_IGNORE;
        long user_id = event.getUserId();
        return MESSAGE_IGNORE;
    }

    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event){
        if(!is_enabled)
            return MESSAGE_IGNORE;
        String msg = event.getMessage();
        long group_id = event.getGroupId();
        if(msg.equals("/mc help"))
        {
            cq.sendGroupMsg(group_id, message,false);
            return MESSAGE_BLOCK;
        }
        else if(msg.substring(0, 3).equals("/mc")){
            String[] args = msg.split(" ");
            String ip_port = "";
            try{ ip_port = args[1]; }
            catch (Exception e){
                ip_port = "";
            }
            try
            {
                URL url = new URL(request_url+ip_port);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                String raw = new BufferedReader (new InputStreamReader (httpURLConnection.getInputStream(), StandardCharsets.UTF_8 )).readLine();
                String json = raw.substring ( 1,raw.length () -1);
                JSONObject jsonObject = new JSONObject(json);
                String server_status = "查询的服务器以及端口："+ip_port+"\n服务器的状态："+jsonObject.getString("status")+"\n服务器在线人数："
                        +jsonObject.getInt ("players_online")+"/"+jsonObject.getInt (  "players_max")+
                        "\n服务器版本："+jsonObject.getString ( "version" );

                cq.sendGroupMsg(group_id,server_status,false);

                httpURLConnection.disconnect();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return MESSAGE_BLOCK;
        }

        return MESSAGE_BLOCK;
    }
}

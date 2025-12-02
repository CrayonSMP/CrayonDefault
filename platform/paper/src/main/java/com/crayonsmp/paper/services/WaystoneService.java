package com.crayonsmp.paper.services;

import com.crayonsmp.paper.CrayonDefault;
import com.crayonsmp.paper.listener.WaystoneListener;
import com.crayonsmp.paper.object.Waystone;
import com.crayonsmp.paper.utils.config.ConfigUtil;
import com.crayonsmp.paper.utils.config.SConfig;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class WaystoneService {
    public SConfig config;

    @Getter
    public List<Waystone> waystones = new ArrayList<>();

    public void init(CrayonDefault instance){
        config = ConfigUtil.getConfig("waystone-config", instance);

        initConfig();
        readWaystones();

        instance.getServer().getPluginManager().registerEvents(new WaystoneListener(), instance);
    }

    public void initConfig(){
        if(!config.getFile().exists()){
            config.setDefault("waystone-id", "default:bench");
            config.save();
        }
    }

    public void readWaystones(){
        waystones.clear();
        if(config.getFile().exists()){
            List<Waystone> list = (List<Waystone>) config.getList("waystones");
            if(list != null){
                waystones.addAll(list);
            }
        }
    }

    public void saveWaystones(){
        config.set("waystones", waystones);
        config.save();
    }

    public void addWaystone(Waystone waystone){
        waystones.add(waystone);
        saveWaystones();
    }

    public void removeWaystone(Waystone waystone){
        waystones.remove(waystone);
        saveWaystones();
    }

}

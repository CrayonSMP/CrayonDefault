package com.crayonsmp.paper.waystone;

import com.crayonsmp.api.ICrayonDefault;
import com.crayonsmp.api.waystone.IWaystone;
import com.crayonsmp.api.waystone.IWaystoneService;
import com.crayonsmp.paper.CrayonDefault;
import com.crayonsmp.paper.listener.WaystoneListener;
import com.crayonsmp.paper.util.config.ConfigurationUtil;
import com.crayonsmp.paper.util.config.Configuration;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class WaystoneService implements IWaystoneService {
    private Configuration config;
    private List<IWaystone> waystones = new ArrayList<>();

    @Override
    public void init(ICrayonDefault instance){
        JavaPlugin plugin = (JavaPlugin) instance;
        config = ConfigurationUtil.getConfig("waystone-config", plugin);
        initConfig();
        readWaystones();
        plugin.getServer().getPluginManager().registerEvents(new WaystoneListener(), plugin);
    }

    @Override
    public void initConfig(){
        if(!config.getFile().exists()){
            config.setDefault("waystone-id", "default:bench");
            config.save();
        }
    }

    @Override
    public void readWaystones(){
        waystones.clear();
        if(config.getFile().exists()){
            List<IWaystone> list = (List<IWaystone>) config.getList("waystones");
            if(list != null){
                waystones.addAll(list);
            }
        }
    }

    @Override
    public void saveWaystones(){
        config.set("waystones", waystones);
        config.save();
    }

    @Override
    public void addWaystone(IWaystone waystone){
        waystones.add(waystone);
        saveWaystones();
    }

    @Override
    public void removeWaystone(IWaystone waystone){
        waystones.remove(waystone);
        saveWaystones();
    }

}

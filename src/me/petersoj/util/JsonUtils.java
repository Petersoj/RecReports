package me.petersoj.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.petersoj.report.Report;
import me.petersoj.report.ReportPlayer;
import me.petersoj.util.adapters.LocationAdapter;
import me.petersoj.util.adapters.ReportPlayerAdapter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonUtils {

    // Below are the variables for Gson generics to use during [de]serialization
    public static final Type MAP_REPORT_PLAYER_LOCATION;
    public static final Type ARRAYLIST_INTEGER;
    public static final Type ARRAYLIST_REPORT;
    public static final Type MAP_REPORT_PLAYER_BOOLEAN;
    public static final Type ARRAYLIST_REPORT_PLAYER;
    public static final Type MAP_EQUIPMENT_TYPE;

    private static final Gson gson; // So I don't have to create a new Gson instance every time.

    // Gson adapter instances
    private static final LocationAdapter LOCATION_ADAPTER = new LocationAdapter(true, true);
    private static final ReportPlayerAdapter REPORT_PLAYER_ADAPTER = new ReportPlayerAdapter();

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(Location.class, LOCATION_ADAPTER)
                .registerTypeAdapter(ReportPlayer.class, REPORT_PLAYER_ADAPTER)
                .create();

        // TypeToken has to be anonymous for some speciality.
        MAP_REPORT_PLAYER_LOCATION = new TypeToken<HashMap<ReportPlayer, Location>>() {
        }.getType();
        ARRAYLIST_INTEGER = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ARRAYLIST_REPORT = new TypeToken<ArrayList<Report>>() {
        }.getType();
        MAP_REPORT_PLAYER_BOOLEAN = new TypeToken<HashMap<ReportPlayer, Boolean>>() {
        }.getType();
        ARRAYLIST_REPORT_PLAYER = new TypeToken<ArrayList<ReportPlayer>>() {
        }.getType();
        MAP_EQUIPMENT_TYPE = new TypeToken<HashMap<ReportPlayer, HashMap<Integer, ItemStack>>>() {
        }.getType();
    }

    public static Gson getGson() {
        return gson;
    }
}

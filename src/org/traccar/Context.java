/*
 * Copyright 2015 - 2017 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ning.http.client.AsyncHttpClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.util.URIUtil;
import org.traccar.database.CalendarManager;
import org.traccar.database.CommandsManager;
import org.traccar.database.AttributesManager;
import org.traccar.database.BaseObjectManager;
import org.traccar.database.ConnectionManager;
import org.traccar.database.DataManager;
import org.traccar.database.DeviceManager;
import org.traccar.database.DriversManager;
import org.traccar.database.IdentityManager;
import org.traccar.database.LdapProvider;
import org.traccar.database.MediaManager;
import org.traccar.database.NotificationManager;
import org.traccar.database.PermissionsManager;
import org.traccar.database.GeofenceManager;
import org.traccar.database.GroupsManager;
import org.traccar.database.StatisticsManager;
import org.traccar.database.UsersManager;
import org.traccar.events.MotionEventHandler;
import org.traccar.events.OverspeedEventHandler;
import org.traccar.geocoder.AddressFormat;
import org.traccar.geocoder.BingMapsGeocoder;
import org.traccar.geocoder.FactualGeocoder;
import org.traccar.geocoder.GeocodeFarmGeocoder;
import org.traccar.geocoder.GisgraphyGeocoder;
import org.traccar.geocoder.GoogleGeocoder;
import org.traccar.geocoder.MapQuestGeocoder;
import org.traccar.geocoder.NominatimGeocoder;
import org.traccar.geocoder.OpenCageGeocoder;
import org.traccar.geocoder.Geocoder;
import org.traccar.geolocation.UnwiredGeolocationProvider;
import org.traccar.helper.Log;
import org.traccar.model.Attribute;
import org.traccar.model.BaseModel;
import org.traccar.model.Calendar;
import org.traccar.model.Command;
import org.traccar.model.Device;
import org.traccar.model.Driver;
import org.traccar.model.Geofence;
import org.traccar.model.Group;
import org.traccar.model.Notification;
import org.traccar.model.User;
import org.traccar.geolocation.GoogleGeolocationProvider;
import org.traccar.geolocation.GeolocationProvider;
import org.traccar.geolocation.MozillaGeolocationProvider;
import org.traccar.geolocation.OpenCellIdGeolocationProvider;
import org.traccar.notification.EventForwarder;
import org.traccar.notification.JsonTypeEventForwarder;
import org.traccar.notification.MultiPartEventForwarder;
import org.traccar.reports.model.TripsConfig;
import org.traccar.smpp.SmppClient;
import org.traccar.web.WebServer;

public final class Context {

    private Context() {
    }

    private static Config config;

    public static Config getConfig() {
        return config;
    }

    private static boolean loggerEnabled;

    public static boolean isLoggerEnabled() {
        return loggerEnabled;
    }

    private static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    private static IdentityManager identityManager;

    public static IdentityManager getIdentityManager() {
        return identityManager;
    }

    private static DataManager dataManager;

    public static DataManager getDataManager() {
        return dataManager;
    }

    private static LdapProvider ldapProvider;

    public static LdapProvider getLdapProvider() {
        return ldapProvider;
    }

    private static MediaManager mediaManager;

    public static MediaManager getMediaManager() {
        return mediaManager;
    }

    private static UsersManager usersManager;

    public static UsersManager getUsersManager() {
        return usersManager;
    }

    private static GroupsManager groupsManager;

    public static GroupsManager getGroupsManager() {
        return groupsManager;
    }

    private static DeviceManager deviceManager;

    public static DeviceManager getDeviceManager() {
        return deviceManager;
    }

    private static ConnectionManager connectionManager;

    public static ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    private static PermissionsManager permissionsManager;

    public static PermissionsManager getPermissionsManager() {
        return permissionsManager;
    }

    private static Geocoder geocoder;

    public static Geocoder getGeocoder() {
        return geocoder;
    }

    private static GeolocationProvider geolocationProvider;

    public static GeolocationProvider getGeolocationProvider() {
        return geolocationProvider;
    }

    private static WebServer webServer;

    public static WebServer getWebServer() {
        return webServer;
    }

    private static ServerManager serverManager;

    public static ServerManager getServerManager() {
        return serverManager;
    }

    private static GeofenceManager geofenceManager;

    public static GeofenceManager getGeofenceManager() {
        return geofenceManager;
    }

    private static CalendarManager calendarManager;

    public static CalendarManager getCalendarManager() {
        return calendarManager;
    }

    private static NotificationManager notificationManager;

    public static NotificationManager getNotificationManager() {
        return notificationManager;
    }

    private static VelocityEngine velocityEngine;

    public static VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    private static final AsyncHttpClient ASYNC_HTTP_CLIENT = new AsyncHttpClient();

    public static AsyncHttpClient getAsyncHttpClient() {
        return ASYNC_HTTP_CLIENT;
    }

    private static EventForwarder eventForwarder;

    public static EventForwarder getEventForwarder() {
        return eventForwarder;
    }

    private static AttributesManager attributesManager;

    public static AttributesManager getAttributesManager() {
        return attributesManager;
    }

    private static DriversManager driversManager;

    public static DriversManager getDriversManager() {
        return driversManager;
    }

    private static CommandsManager commandsManager;

    public static CommandsManager getCommandsManager() {
        return commandsManager;
    }

    private static StatisticsManager statisticsManager;

    public static StatisticsManager getStatisticsManager() {
        return statisticsManager;
    }

    private static SmppClient smppClient;

    public static SmppClient getSmppManager() {
        return smppClient;
    }

    private static MotionEventHandler motionEventHandler;

    public static MotionEventHandler getMotionEventHandler() {
        return motionEventHandler;
    }

    private static OverspeedEventHandler overspeedEventHandler;

    public static OverspeedEventHandler getOverspeedEventHandler() {
        return overspeedEventHandler;
    }

    private static TripsConfig tripsConfig;

    public static TripsConfig getTripsConfig() {
        return tripsConfig;
    }

    public static TripsConfig initTripsConfig() {
        return new TripsConfig(
                config.getLong("report.trip.minimalTripDistance", 500),
                config.getLong("report.trip.minimalTripDuration", 300) * 1000,
                config.getLong("report.trip.minimalParkingDuration", 300) * 1000,
                config.getLong("report.trip.minimalNoDataDuration", 3600) * 1000,
                config.getBoolean("report.trip.useIgnition"),
                config.getBoolean("event.motion.processInvalidPositions"),
                config.getDouble("event.motion.speedThreshold", 0.01));
    }

    public static Geocoder initGeocoder() {
        String type = config.getString("geocoder.type", "google");
        String url = config.getString("geocoder.url");
        String key = config.getString("geocoder.key");
        String language = config.getString("geocoder.language");

        String formatString = config.getString("geocoder.format");
        AddressFormat addressFormat;
        if (formatString != null) {
            addressFormat = new AddressFormat(formatString);
        } else {
            addressFormat = new AddressFormat();
        }

        int cacheSize = config.getInteger("geocoder.cacheSize");
        switch (type) {
            case "nominatim":
                return new NominatimGeocoder(url, key, language, cacheSize, addressFormat);
            case "gisgraphy":
                return new GisgraphyGeocoder(url, cacheSize, addressFormat);
            case "mapquest":
                return new MapQuestGeocoder(url, key, cacheSize, addressFormat);
            case "opencage":
                return new OpenCageGeocoder(url, key, cacheSize, addressFormat);
            case "bingmaps":
                return new BingMapsGeocoder(url, key, cacheSize, addressFormat);
            case "factual":
                return new FactualGeocoder(url, key, cacheSize, addressFormat);
            case "geocodefarm":
                return new GeocodeFarmGeocoder(key, language, cacheSize, addressFormat);
            default:
                return new GoogleGeocoder(key, language, cacheSize, addressFormat);
        }
    }

    public static void init(String[] arguments) throws Exception {

        config = new Config();
        if (arguments.length <= 0) {
            throw new RuntimeException("Configuration file is not provided");
        }

        config.load(arguments[0]);

        loggerEnabled = config.getBoolean("logger.enable");
        if (loggerEnabled) {
            Log.setupLogger(config);
        }

        objectMapper = new ObjectMapper();
        objectMapper.setConfig(
                objectMapper.getSerializationConfig().without(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
        if (Context.getConfig().getBoolean("mapper.prettyPrintedJson")) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }

        if (config.hasKey("database.url")) {
            dataManager = new DataManager(config);
        }

        if (config.getBoolean("ldap.enable")) {
            ldapProvider = new LdapProvider(config);
        }

        if (config.hasKey("media.path")) {
            mediaManager = new MediaManager(config);
        }

        if (dataManager != null) {
            usersManager = new UsersManager(dataManager);
            groupsManager = new GroupsManager(dataManager);
            deviceManager = new DeviceManager(dataManager);
        }

        identityManager = deviceManager;

        if (config.getBoolean("geocoder.enable")) {
            geocoder = initGeocoder();
        }

        if (config.getBoolean("geolocation.enable")) {
            initGeolocationModule();
        }

        if (config.getBoolean("web.enable")) {
            webServer = new WebServer(config, dataManager.getDataSource());
        }

        permissionsManager = new PermissionsManager(dataManager, usersManager);

        connectionManager = new ConnectionManager();

        tripsConfig = initTripsConfig();

        if (config.getBoolean("event.enable")) {
            initEventsModule();
        }

        serverManager = new ServerManager();

        if (config.getBoolean("event.forward.enable")) {
            if (Context.getConfig().getBoolean("event.forward.payloadAsParamMode")) {
                eventForwarder = new MultiPartEventForwarder();
            } else {
                eventForwarder = new JsonTypeEventForwarder();
            }
        }

        attributesManager = new AttributesManager(dataManager);

        driversManager = new DriversManager(dataManager);

        commandsManager = new CommandsManager(dataManager);

        statisticsManager = new StatisticsManager();

        if (config.getBoolean("sms.smpp.enable")) {
            smppClient = new SmppClient();
        }

    }

    private static void initGeolocationModule() {

        String type = config.getString("geolocation.type", "mozilla");
        String url = config.getString("geolocation.url");
        String key = config.getString("geolocation.key");

        switch (type) {
            case "google":
                geolocationProvider = new GoogleGeolocationProvider(key);
                break;
            case "opencellid":
                geolocationProvider = new OpenCellIdGeolocationProvider(key);
                break;
            case "unwired":
                geolocationProvider = new UnwiredGeolocationProvider(url, key);
                break;
            default:
                geolocationProvider = new MozillaGeolocationProvider(key);
                break;
        }
    }

    private static void initEventsModule() {

        geofenceManager = new GeofenceManager(dataManager);
        calendarManager = new CalendarManager(dataManager);
        notificationManager = new NotificationManager(dataManager);
        Properties velocityProperties = new Properties();
        velocityProperties.setProperty("file.resource.loader.path",
                Context.getConfig().getString("templates.rootPath", "templates") + "/");
        velocityProperties.setProperty("runtime.log.logsystem.class",
                "org.apache.velocity.runtime.log.NullLogChute");

        String address;
        try {
            address = config.getString("web.address", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            address = "localhost";
        }

        String webUrl = URIUtil.newURI("http", address, config.getInteger("web.port", 8082), "", "");
        webUrl = Context.getConfig().getString("web.url", webUrl);
        velocityProperties.setProperty("web.url", webUrl);

        velocityEngine = new VelocityEngine();
        velocityEngine.init(velocityProperties);

        motionEventHandler = new MotionEventHandler(tripsConfig);
        overspeedEventHandler = new OverspeedEventHandler(
                Context.getConfig().getLong("event.overspeed.minimalDuration") * 1000,
                Context.getConfig().getBoolean("event.overspeed.notRepeat"));
    }

    public static void init(IdentityManager testIdentityManager) {
        config = new Config();
        objectMapper = new ObjectMapper();
        identityManager = testIdentityManager;
    }

    public static <T extends BaseModel> BaseObjectManager<T> getManager(Class<T> clazz) {
        if (clazz.equals(Device.class)) {
            return (BaseObjectManager<T>) deviceManager;
        } else if (clazz.equals(Group.class)) {
            return (BaseObjectManager<T>) groupsManager;
        } else if (clazz.equals(User.class)) {
            return (BaseObjectManager<T>) usersManager;
        } else if (clazz.equals(Calendar.class)) {
            return (BaseObjectManager<T>) calendarManager;
        } else if (clazz.equals(Attribute.class)) {
            return (BaseObjectManager<T>) attributesManager;
        } else if (clazz.equals(Geofence.class)) {
            return (BaseObjectManager<T>) geofenceManager;
        } else if (clazz.equals(Driver.class)) {
            return (BaseObjectManager<T>) driversManager;
        } else if (clazz.equals(Command.class)) {
            return (BaseObjectManager<T>) commandsManager;
        } else if (clazz.equals(Notification.class)) {
            return (BaseObjectManager<T>) notificationManager;
        }
        return null;
    }

}

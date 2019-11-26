package com.detatech.vitaluser.Utils;
import com.onesignal.OneSignal;

public class OneSignalApplication extends XuberApplication {
    @Override
    public void onCreate() {
        super.onCreate();

//         OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

//        OneSignal.startInit(this)
//                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
//                .setNotificationReceivedHandler( new MyNotificationReceivedHandler() )
//                .init();
    }
}

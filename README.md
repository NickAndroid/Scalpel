![Logo](art/logo.jpg)

# Scalpel
Enhanced auto injection framework for Android

### Latest version
[ ![Download](https://api.bintray.com/packages/nickandroid/maven/scalpel/images/download.svg) ](https://bintray.com/nickandroid/maven/scalpel/_latestVersion)

### General Features
- Auto find views, int, String, bool, array...
- OnClick listener, action, args.
- Auto bind/unbind AIDL service.
- Auto (un)register receiver.
- Auto find System services, PowerManager, TelephonyManager, etc
- Auto require permission (for SDK above M).
- Auto require full screen for Activity.
- Auto recycle fields for Activity.
- Bean support.
- Auto require root.

### Usage

Add dependencies
``` java
dependencies {
    compile 'com.nick.scalpel:scalpel:1.0.3'
}
```

1. Configurations customize:
``` java
@ContextConfiguration(xmlRes = R.xml.context_scalpel_demo)
public class MyApplication extends ScalpelApplication {
    @Override
    protected void onConfigScalpel(Scalpel scalpel) {
        super.onConfigScalpel(scalpel);
        scalpel.addFieldWirer(new CustomWirer());
    }
}
```

2. Use auto activity or wire things manually
``` java
public class MainActivity extends ScalpelAutoActivity {}
```
``` java
public class MyFragment extends ScalpelAutoFragment {}
```
``` java
@AutoRequireRoot(mode = AutoRequireRoot.Mode.Async, callback = "this")
public class MyService extends ScalpelAutoService {}
```

``` java
public class ViewHolder {
    @FindView(id = R.id.toolbar)
    Toolbar toolbar;

    @FindView(id = R.id.fab)
    @OnTouch(action = "showSnack", args = {"Hello, I am a fab!", "Nick"})
    FloatingActionButton fab;

    @FindView(id = R.id.hello)
    @OnClick(listener = "mokeListener")
    TextView hello;

    @FindInt(id = R.integer.size)
    int size;

    @FindColor(id = R.color.colorAccent)
    int color;

    @FindString(id = R.string.app_name)
    String text;

    @FindBool(id = R.bool.boo)
    boolean bool;

    @FindStringArray(id = R.array.strs)
    String[] strs;

    @FindIntArray(id = R.array.ints)
    int[] ints;

    @AutoWired
    PowerManager pm;

    @AutoWired
    TelephonyManager tm;

    @AutoWired
    NotificationManager nm;

    @AutoWired
    AccountManager accountManager;

    @AutoWired
    ActivityManager am;

    @AutoWired
    AlarmManager alarmManager;

    @BindService(action = "com.nick.service", pkg = "com.nick.scalpeldemo", callback = "this"
            , autoUnbind = false)
    IMyAidlInterface mService;

    @RegisterReceiver(actions = {Intent.ACTION_SCREEN_ON, Intent.ACTION_SCREEN_OFF, "com.nick.service.bind"}
            , autoUnRegister = false)
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Scalpel.Demo", "onReceive, intent = " + intent.getAction());
        }
    };

    @FindBitmap(idRes = R.drawable.bitmap)
    Bitmap bitmap;

    @Custom
    Object custom;

    @RetrieveBean
    EmptyConObject emptyConObject;

    @RetrieveBean
    ContextConsObject contextConsObject;

    @RetrieveBean
    ContextConsObject contextConsObject2;

    @RetrieveBean(id = R.id.context_obj)
    ContextConsObject contextConsObjectStrict;

    @RetrieveBean
    RecyclerManager mRecyclerManager;

    @AutoWired
    StorageManager sManager;
```

### Example
``` java
@AutoRequestFullScreen(viewToTriggerRestore = R.id.hello)
@AutoRequirePermission(requestCode = 100, permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CALL_PHONE})
public class MainActivity extends ScalpelAutoActivity implements AutoBind.Callback {
    @BindService(action = "com.nick.service", pkg = "com.nick.scalpeldemo", callback = "this"
            , autoUnbind = true)
    IMyAidlInterface mService;

    @RegisterReceiver(actions = {Intent.ACTION_SCREEN_ON, Intent.ACTION_SCREEN_OFF, "com.nick.service.bind"}
            , autoUnRegister = true)
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Scalpel.Demo", "onReceive, intent = " + intent.getAction());
        }
    };

    @FindBitmap(idRes = R.drawable.bitmap)
    @AutoRecycle
    Bitmap bitmap;
}
```

# Scalpel
Auto wired framework for Android

### Latest version
[ ![Download](https://api.bintray.com/packages/nickandroid/maven/scalpel/images/download.svg) ](https://bintray.com/nickandroid/maven/scalpel/_latestVersion)

### Features
- Auto find views, int, String, bool, array...
- OnClick listener, action, args.
- Auto bind/unbind AIDL service.
- Auto (un)register receiver.
- Auto find System services, PowerManager, TelephonyManager, etc
- Auto require permission (for SDK above M).
- Auto require full screen for Activity.
- Auto recycle fields for Activity.
- System service, IPowerManager, etc(mainly for root-ed devices).

### Usage

Add dependencies
``` java
dependencies {
    compile 'com.nick.scalpel:scalpel:0.7'
}
```

1. Configurations customize:
``` java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Scalpel.getDefault().config(Configuration.builder().autoFindIfNull(true).debug(true).logTag("Scalpel").build());
    }
}
```

2. Use auto activity or wire things manually
``` java
public class MainActivity extends ScalpelAutoActivity {}

public class MyFragment extends ScalpelAutoFragment {}

public class MyService extends ScalpelAutoService {}
```

``` java
public class ViewHolder {
    @AutoFound(id = R.id.toolbar) // Same as @AutoFound(id = R.id.toolbar, type = Type.Auto)
            Toolbar toolbar;

    @AutoFound(id = R.id.fab)
    @OnClick(listener = "mokeListener")
    FloatingActionButton fab;

    @OnClick(listener = "mokeListener")
    @AutoFound(id = R.id.hello)
    TextView hello;

    @AutoFound(id = R.integer.size, type = AutoFound.Type.Integer)
    int size;

    @AutoFound(id = R.color.colorAccent, type = AutoFound.Type.Color)
    int color;

    @AutoFound(id = R.string.app_name, type = AutoFound.Type.String)
    String text;

    @AutoFound(id = R.bool.boo, type = AutoFound.Type.Bool)
    boolean bool;

    @AutoFound(id = R.array.strs)
    String[] strs;

    @AutoFound(id = R.array.ints, type = AutoFound.Type.Auto)
    int[] ints;

    @AutoFound
    PowerManager pm;

    @AutoFound
    TelephonyManager tm;

    @AutoFound
    NotificationManager nm;

    @AutoFound
    AccountManager accountManager;

    @AutoFound
    ActivityManager am;

    @AutoFound
    AlarmManager alarmManager;

    private View.OnClickListener mokeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(v, "Replace with your own actions", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };

    ViewHolder(Context context) {
        View rootV = LayoutInflater.from(context).inflate(R.layout.activity_main, null);
        Scalpel.getDefault().wire(rootV, this);

        log(toolbar, fab, hello, size, color, text, bool, strs, ints, am, pm, tm, nm, accountManager, alarmManager);
    }
}
```

### Example
``` java
@AutoRequestFullScreen(viewToTriggerRestore = R.id.hello)
@AutoRequirePermission(requestCode = 100, permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CALL_PHONE})
public class MainActivity extends ScalpelAutoActivity implements AutoBind.Callback {

    @AutoFound(id = R.id.toolbar, type = AutoFound.Type.View)
    Toolbar toolbar;

    @AutoFound(id = R.id.fab)
    @OnTouch(action = "showSnack", args = {"Hello, I am a fab!", "Nick"})
    FloatingActionButton fab;

    @AutoFound(id = R.id.hello)
    @OnClick(listener = "mokeListener")
    TextView hello;

    @AutoFound(id = R.integer.size, type = AutoFound.Type.Integer)
    int size;

    @AutoFound(id = R.color.colorAccent, type = AutoFound.Type.Color)
    int color;

    @AutoFound(id = R.string.app_name, type = AutoFound.Type.String)
    String text;

    @AutoFound(id = R.bool.boo, type = AutoFound.Type.Bool)
    boolean bool;

    @AutoFound(id = R.array.strs, type = AutoFound.Type.StringArray)
    String[] strs;

    @AutoFound(id = R.array.ints, type = AutoFound.Type.IntArray)
    int[] ints;

    @AutoFound
    PowerManager pm;

    @AutoFound
    TelephonyManager tm;

    @AutoFound
    NotificationManager nm;

    @AutoFound
    AccountManager accountManager;

    @AutoFound
    ActivityManager am;

    @AutoFound
    AlarmManager alarmManager;

    @AutoBind(action = "com.nick.service", pkg = "com.nick.scalpeldemo", callback = "this"
            , autoUnbind = true)
    IMyAidlInterface mService;

    @AutoRegister(actions = {Intent.ACTION_SCREEN_ON, Intent.ACTION_SCREEN_OFF, "com.nick.service.bind"}
            , autoUnRegister = true)
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Scalpel.Demo", "onReceive, intent = " + intent.getAction());
        }
    };

    @AutoFound(id = R.drawable.bitmap)
    @AutoRecycle
    Bitmap bitmap;

    @SystemService
    IPowerManager powerManager;
}
```

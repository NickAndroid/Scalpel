# Scalpel
Enhanced auto injection framework for Android

### Latest version
[ ![Download](https://api.bintray.com/packages/nickandroid/maven/scalpel/images/download.svg) ](https://bintray.com/nickandroid/maven/scalpel/_latestVersion)

### General Features
- Auto find views, int, String, bool, array...
- OnClick listener, action, args.
- Bind/unbind AIDL service.
- (Un)Register receiver.
- Find System services, PowerManager, TelephonyManager, etc
- Require permission (for SDK above M).
- Require full screen for Activity.
- Recycle fields for Activity.
- Bean support.
- Require root.

### Usage

Add dependencies
```
dependencies {
    compile 'com.nick.scalpel:scalpel:_latestVersion'
}
```

### Samples

*ContextConfiguration support*

*  Declear in Appliction
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
*  Define beans in xml
``` Xml
<Beans xmlns:app="http://schemas.android.com/apk/res-auto">
    <Bean
        app:clz="com.nick.scalpeldemo.EmptyConObject"
        app:nickname="empty" />
    <Bean
        app:clz="com.nick.scalpeldemo.ContextConsObject"
        app:identify="@+id/context_obj"
        app:nickname="context" />
</Beans>
```

*Extends auto components*
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

*Or wire manually*
``` java
public class ViewHolder {
    
    annotations...
    
    ViewHolder(Context context) {
        View rootV = LayoutInflater.from(context).inflate(R.layout.activity_main, null);
        Scalpel.getInstance().wire(rootV, this);
    }
```

``` java
@RequestFullScreen(viewToTriggerRestore = R.id.hello)
@RequirePermission(requestCode = 100, permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CALL_PHONE})
public class MainActivity extends Activity implements AutoBind.Callback {
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

@Override
public void onStart() {
    Scalpel.getInstance().wire(this);
}
```

# Scalpel
Auto wired framework for Android

### Features
1. AutoFound views, int, String, bool, array...
2. OnClick listener, action, args.

### Usage
1. Configurations customize:
``` java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Scalpel.getDefault().config(Configuration.builder().autoFindIfNull(true).debug(true).logTag("Scalpel").build());
    }
}
``` java

2. Use auto activity or wire things manually
``` java
public class MainActivity extends ScalpelAutoActivity {}
``` java

``` java
public class ViewHolder {
    @AutoFound(id = R.id.toolbar) // Same as @AutoFound(id = R.id.toolbar, type = Type.Auto)
            Toolbar toolbar;

    @AutoFound(id = R.id.fab)
    @OnClick(listener = "mokeListener")
    FloatingActionButton fab;

    ViewHolder(Context context) {
        View rootV = LayoutInflater.from(context).inflate(R.layout.activity_main, null);
        Scalpel.getDefault().wire(rootV, this);
        Scalpel.getDefault().wire(context, this);

        log(toolbar, fab, hello, size, color, text, bool, strs, ints);
    }
}
``` java

### Example
``` java
public class MainActivity extends ScalpelAutoActivity {

    @AutoFound(id = R.id.toolbar, type = Type.View)
    Toolbar toolbar;

    @AutoFound(id = R.id.fab)
    @OnClick(action = "showSnack", args = {"Hello, I am a fab!", "Nick"})
    FloatingActionButton fab;

    @AutoFound(id = R.id.hello)
    @OnClick(listener = "mokeListener")
    TextView hello;

    @AutoFound(id = R.integer.size, type = Type.Integer)
    int size;

    @AutoFound(id = R.color.colorAccent, type = Type.Color)
    int color;

    @AutoFound(id = R.string.app_name, type = Type.String)
    String text;

    @AutoFound(id = R.bool.boo, type = Type.Bool)
    boolean bool;

    @AutoFound(id = R.array.strs, type = Type.StringArray)
    String[] strs;

    @AutoFound(id = R.array.ints, type = Type.IntArray)
    int[] ints;

    private View.OnClickListener mokeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);

        hello.setTextSize(size);
        hello.setTextColor(color);
        hello.setText(text + "-" + bool + "-" + Arrays.toString(strs) + "-" + Arrays.toString(ints));

        new ViewHolder(this);
    }

    public void showSnack(String content, String owner) {
        Snackbar.make(getWindow().getDecorView(), owner + ": " + content, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
```

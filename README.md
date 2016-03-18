# Scalpel
Auto wired framework for Android

### Sample
``` java
public class MainActivity extends ScalpelAutoActivity {

    @AutoFound(id = R.id.toolbar)
    Toolbar toolbar;
    @AutoFound(id = R.id.fab)
    FloatingActionButton fab;
    @AutoFound(id = R.id.hello)
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        hello.setTextSize(size);
        hello.setTextColor(color);
        hello.setText(text + "-" + bool + "-" + Arrays.toString(strs) + "-" + Arrays.toString(ints));
    }
```

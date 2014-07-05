package fr.northborders.AnimationPlayground.DrawPath;

import android.app.Activity;
import android.os.Bundle;
import fr.northborders.AnimationPlayground.R;

/**
 * Created by thibaultguegan on 29/05/2014.
 */
public class ProgressActivity extends Activity {

    private ProgressView progressView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressView = (ProgressView) findViewById(R.id.progress);

        setContentView(R.layout.progress_activity);
    }
}
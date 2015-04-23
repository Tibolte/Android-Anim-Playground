package fr.northborders.AnimationPlayground.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import fr.northborders.AnimationPlayground.DrawPath.ProgressActivity;
import fr.northborders.AnimationPlayground.DrawPathSvg.DrawPathSvgActivity;
import fr.northborders.AnimationPlayground.DynamicForm.DynamicFormActivity;
import fr.northborders.AnimationPlayground.ElasticDownload.ElasticDownloadActivity;
import fr.northborders.AnimationPlayground.FragmentAnimation.FragmentAnimationActivity;
import fr.northborders.AnimationPlayground.R;

/**
 * Created by thibaultguegan on 29/05/2014.
 */
public class MainActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
    }

    public void startProgress(View v)
    {
        Intent intent = new Intent(this, ProgressActivity.class);
        startActivity(intent);
    }

    public void startSvg(View v)
    {
        Intent intent = new Intent(this, DrawPathSvgActivity.class);
        startActivity(intent);
    }

    public void startDynamicForm(View v)
    {
        Intent intent = new Intent(this, FragmentAnimationActivity.class);
        startActivity(intent);
    }

    public void startElasticDownload(View v)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Toast.makeText(getApplicationContext(), "Only available for Lollipop (Android 5.0 or greater)!!! =)",
                    Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, ElasticDownloadActivity.class);
            startActivity(intent);
        }
    }
}
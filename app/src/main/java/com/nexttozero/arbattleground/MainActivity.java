package com.nexttozero.arbattleground;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final String DEBUG = "debug";
    private PointerDrawable pointer = new PointerDrawable();
    private boolean isTracking;
    private boolean isHitting;
    private ModelLoader modelLoader;
    //declaration of ARFragment
    private ArFragment fragment;


    private ImageView titlesplash;



    LinearLayout quickSelectLL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(DEBUG, "started:");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragment = (ArFragment)
                getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

        fragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
            fragment.onUpdate(frameTime);
            onUpdate();
        });

        modelLoader = new ModelLoader(new WeakReference<>(this));

        //Initialization of Quick Select
        quickSelectLL = findViewById(R.id.gallery_layout);
        initializeQuickSelect();

        //Initialization of Title Splash banner
        titlesplash = (ImageView) findViewById(R.id.titlesplash_imageview);
        TitleSplashFade();
        titlesplash.setVisibility(View.INVISIBLE);




        Log.d(DEBUG, "OnCreate Completed:");

    }


    private void onUpdate() {
        boolean trackingChanged = updateTracking();
        View contentView = findViewById(android.R.id.content);
        if (trackingChanged) {
            if (isTracking) {
                contentView.getOverlay().add(pointer);
            } else {
                contentView.getOverlay().remove(pointer);
            }
            contentView.invalidate();
        }

        if (isTracking) {
            boolean hitTestChanged = updateHitTest();
            if (hitTestChanged) {
                pointer.setEnabled(isHitting);
                contentView.invalidate();
            }
        }

    }

    private boolean updateTracking() {
        Frame frame = fragment.getArSceneView().getArFrame();
        boolean wasTracking = isTracking;
        isTracking = frame != null &&
                frame.getCamera().getTrackingState() == TrackingState.TRACKING;
        return isTracking != wasTracking;
    }


    private boolean updateHitTest() {
        Frame frame = fragment.getArSceneView().getArFrame();
        android.graphics.Point pt = getScreenCenter();
        List<HitResult> hits;
        boolean wasHitting = isHitting;
        isHitting = false;
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    isHitting = true;
                    break;
                }
            }
        }
        return wasHitting != isHitting;
    }

    private android.graphics.Point getScreenCenter() {
        View vw = findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth() / 2, vw.getHeight() / 2);
    }


    private void initializeQuickSelect() {


        ImageView Dragon = new ImageView(this);
        Dragon.setImageResource(R.drawable.dragon_icon);
        Dragon.setContentDescription("Dragon");
        Dragon.setOnClickListener(view -> {
            addObject(Uri.parse("Dragon.sfb"));
        });
        quickSelectLL.addView(Dragon);

        ImageView Mimic = new ImageView(this);
        Mimic.setImageResource(R.drawable.mimic_icon);
        Mimic.setContentDescription("Mimic");
        Mimic.setOnClickListener(view -> {
            addObject(Uri.parse("Mimic.sfb"));
        });
        quickSelectLL.addView(Mimic);

        ImageView Mindflayer = new ImageView(this);
        Mindflayer.setImageResource(R.drawable.mindflayer_icon);
        Mindflayer.setContentDescription("Mindflayer");
        Mindflayer.setOnClickListener(view -> {
            addObject(Uri.parse("Mindflayer.sfb"));
        });
        quickSelectLL.addView(Mindflayer);

        ImageView Zombie = new ImageView(this);
        Zombie.setImageResource(R.drawable.zombie_icon);
        Zombie.setContentDescription("Zombie");
        Zombie.setOnClickListener(view -> {
            addObject(Uri.parse("Zombie.sfb"));
        });
        quickSelectLL.addView(Zombie);
    }

    private void addObject(Uri model) {
        Frame frame = fragment.getArSceneView().getArFrame();
        android.graphics.Point pt = getScreenCenter();
        List<HitResult> hits;
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    modelLoader.loadModel(hit.createAnchor(), model);
                    break;

                }
            }
        }

        quickSelectLL.setVisibility(View.INVISIBLE);

    }

    public void addNodeToScene(Anchor anchor, ModelRenderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(fragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        fragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();


    }

    public void onException(Throwable throwable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(throwable.getMessage())
                .setTitle("Codelab error!");
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }

    public void QuickSelectVisible(View view) {


        if (quickSelectLL.getVisibility() == View.INVISIBLE) {
            quickSelectLL.setVisibility(View.VISIBLE);

        } else {

            quickSelectLL.setVisibility(View.INVISIBLE);

        }

    }


    public void GalleryLaunch(View view) {


        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);


    }


    public void TitleSplashFade() {


        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        titlesplash.startAnimation(myFadeInAnimation);


    }


    public void ClearScene(View view) {

        /**
         fragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);


         final FragmentTransaction clear = getSupportFragmentManager().beginTransaction();
         clear.detach(fragment);
         clear.attach(fragment);
         clear.commit();
         **/

        //The above code recreates the sceneform fragement. Currently a work in progress, as I'm trying to find a better way to clear nodes.
        //Currently obliterates the sceneform fragment and then fails to recreate it.


    }
}

package com.nexttozero.arbattleground;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
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

    //Gallery Visibility
    ImageButton galleryButton;
    LinearLayout galleryLL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(DEBUG, "started:");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /**  FloatingActionButton fab = findViewById(R.id.btn_settings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

       **/
        fragment = (ArFragment)
                getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

        fragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
            fragment.onUpdate(frameTime);
            onUpdate();
        });


        modelLoader = new ModelLoader(new WeakReference<>(this));






        //creation of galleryButton ImageButton. For use in GalleryVisible method.
        ImageButton galleryButton = findViewById(R.id.addmodel_button);
        galleryLL = findViewById(R.id.gallery_layout);
        initializeGallery();
        titlesplash= (ImageView)findViewById(R.id.titlesplash_imageview);
        TitleSplashFade();


        Log.d(DEBUG, "OnCreate Completed:");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        return new android.graphics.Point(vw.getWidth()/2, vw.getHeight()/2);
    }



    private void initializeGallery() {


        ImageView Dragon = new ImageView(this);
        Dragon.setImageResource(R.drawable.dragon_icon);
        Dragon.setContentDescription("Dragon");
        Dragon.setOnClickListener(view ->{addObject(Uri.parse("Dragon.sfb"));});
        galleryLL.addView(Dragon);

        ImageView Mimic = new ImageView(this);
        Mimic.setImageResource(R.drawable.mimic_icon);
        Mimic.setContentDescription("Mimic");
        Mimic.setOnClickListener(view ->{addObject(Uri.parse("Mimic.sfb"));});
        galleryLL.addView(Mimic);

        ImageView Mindflayer = new ImageView(this);
        Mindflayer.setImageResource(R.drawable.mindflayer_icon);
        Mindflayer.setContentDescription("Mindflayer");
        Mindflayer.setOnClickListener(view ->{addObject(Uri.parse("Mindflayer.sfb"));});
        galleryLL.addView(Mindflayer);

        ImageView Zombie = new ImageView(this);
        Zombie.setImageResource(R.drawable.zombie_icon);
        Zombie.setContentDescription("Zombie");
        Zombie.setOnClickListener(view ->{addObject(Uri.parse("Zombie.sfb"));});
        galleryLL.addView(Zombie);
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

        galleryLL.setVisibility(View.INVISIBLE);

    }

    public void addNodeToScene(Anchor anchor, ModelRenderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(fragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        fragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();
    }

    public void onException(Throwable throwable){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(throwable.getMessage())
                .setTitle("Codelab error!");
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }


    public void GalleryVisible(View view) {

        titlesplash.setVisibility(View.INVISIBLE);

        if(galleryLL.getVisibility() == View.INVISIBLE){
            galleryLL.setVisibility(View.VISIBLE);

        }
        else{

            galleryLL.setVisibility(View.INVISIBLE);

        }


    }


    public void TitleSplashFade(){


        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        titlesplash.startAnimation(myFadeInAnimation); //Set animation to your Imag



    }
}

package dev.webfx.demo.demofx;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.DemoFX;
import com.chrisnewland.demofx.effect.IEffect;
import com.chrisnewland.demofx.effect.addon.FadeOutAddOnEffect;
import com.chrisnewland.demofx.effect.addon.RotateAddOnEffect;
import com.chrisnewland.demofx.effect.effectfactory.IEffectFactory;
import com.chrisnewland.demofx.effect.fake3d.SnowfieldSprite;
import com.chrisnewland.demofx.effect.fake3d.StarfieldSprite;
import com.chrisnewland.demofx.effect.fractal.FractalRings;
import com.chrisnewland.demofx.effect.fractal.Mandelbrot;
import com.chrisnewland.demofx.effect.fractal.Sierpinski;
import com.chrisnewland.demofx.effect.shape.Chord;
import com.chrisnewland.demofx.effect.shape.Glowboard;
import com.chrisnewland.demofx.effect.shape.SineLines;
import com.chrisnewland.demofx.effect.spectral.Equaliser2D;
import com.chrisnewland.demofx.effect.sprite.Spin;
import com.chrisnewland.demofx.effect.sprite.Tiles;
import com.chrisnewland.demofx.effect.text.TextFlash;
import com.chrisnewland.demofx.effect.text.TextRing;
import com.chrisnewland.demofx.effect.text.TextWaveSprite;
import com.chrisnewland.demofx.effect.text.WordSearch;
import dev.webfx.platform.resource.Resource;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DemoFXApplication extends Application {
    private final StackPane root = new StackPane();
    private final Scene scene = new Scene(root, 1600, 1200);
    private DemoFX introDemo, actualDemo;
    private boolean started;
    long t1, t2, t3, t4, t5, t6, t7, t8, t9, t10;

    @Override
    public void start(Stage stage) {
        stage.setTitle("DemoFX");
        stage.setScene(scene);
        stage.show();
        introDemo = newIntroDemo();
        actualDemo = newActualDemo();
        root.getChildren().setAll(introDemo.getPane());
        introDemo.runDemo();
        root.setOnMousePressed(e -> { // Using setOnMousePressed() because sound doesn't start on iPad if using setOnMouseClicked()
            if (!started)
                introDemo.stopDemo();
            else {
                actualDemo.stopDemo();
                actualDemo = newActualDemo();
            }
            root.getChildren().setAll(actualDemo.getPane());
            actualDemo.runDemo();
            started = true;
        });
    }

    private DemoConfig newDemoConfig(String audioResource) {
        DemoConfig demoConfig = new DemoConfig(scene.getWidth(), scene.getHeight());
        demoConfig.setAudioFilename(Resource.toUrl(audioResource, DemoFXApplication.class));
        return demoConfig;
    }

    private DemoFX newIntroDemo() {
        return new DemoFX(newDemoConfig(null), (IEffectFactory) demoConfig -> dev.webfx.platform.util.collection.Collections.listOf(
                new WordSearch(demoConfig, "Animation using DemoFX\n\nA JavaFX Canvas library\n\nby Chris Newland"),
                new TextWaveSprite(demoConfig, new String[] {"Click to play"}, demoConfig.getHeight() - 200, 1, 5, true)
        ));
    }

    private DemoFX newActualDemo() {
        return new DemoFX(newDemoConfig("DemoFX3.mp3"), (IEffectFactory) demoConfig -> dev.webfx.platform.util.collection.Collections.listOf(
                scheduleEffect(new StarfieldSprite(demoConfig), 0, t1 = 15820),
                scheduleEffect(new RotateAddOnEffect(new FractalRings(demoConfig), 23920, -1, t2 = 32000, 1), t1, t3 = 39900),
                scheduleEffect(new Mandelbrot(demoConfig), t3, t6 = 80000),
                scheduleEffect(new Sierpinski(demoConfig), t2, t4 = 47850),
                scheduleEffect(new TextWaveSprite(demoConfig, new String[] {"Realtime Mandelbrot computation"}, demoConfig.getHeight() - 200, 0.8, 10), t4 + 2000, t5),
                //scheduleEffect(new Checkerboard(demoConfig), t5, t6 = 80000),
                scheduleEffect(new Chord(demoConfig, Color.ORANGE), t4, t5 = 64000),
                scheduleEffect(new Glowboard(demoConfig), t6, t7 = 96000),
                scheduleEffect(new SineLines(demoConfig), t6, t7),
                scheduleEffect(new Equaliser2D(demoConfig), t5, t7),
                scheduleEffect(new Tiles(demoConfig), t7, t8 = 160200),
                scheduleEffect(new Spin(demoConfig), t8, t9 = 192000),
                scheduleEffect(new TextRing(demoConfig, new TextRing.RingData[] {
                        new TextRing.RingData("Coded    in    Java    and    JavaFX", 300, 0.15, 1, 3, 2)}), t8 + 5000, t9),
                scheduleEffect(new FadeOutAddOnEffect(new WordSearch(demoConfig, "Amazing work\n\nThank you Chris Newland\n\nalias @chriswhocodes"), 2500), t9, t10 = 264000 - 2500),
                scheduleEffect(new FadeOutAddOnEffect(new SnowfieldSprite(demoConfig), 2500), t9 + 8000, t10),
                scheduleEffect(new TextFlash(demoConfig, "Thank you for watching", false, 150, 200, 150), t10, t10 + 2500)
        ));
    }

    private IEffect scheduleEffect(IEffect effect, long start, long stop) {
        if (start > 0)
            effect.setStartOffsetMillis(start);
        if (stop > 0)
            effect.setStopOffsetMillis(stop);
        return effect;
    }
}
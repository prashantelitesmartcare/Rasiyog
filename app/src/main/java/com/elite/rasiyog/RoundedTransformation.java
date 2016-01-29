package com.elite.rasiyog;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;

public class RoundedTransformation implements
com.squareup.picasso.Transformation {
private final int radius;
private final int margin; // dp

// radius is corner radii in dp
// margin is the board in dp
public RoundedTransformation(final int radius, final int margin) {
this.radius = radius;
this.margin = margin;
}

public Bitmap transform(final Bitmap source) {
final Paint paint = new Paint();
paint.setAntiAlias(true);
paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
        Shader.TileMode.CLAMP));

Bitmap output = Bitmap.createBitmap(source.getWidth(),
        source.getHeight(), Bitmap.Config.ARGB_8888);
Canvas canvas = new Canvas(output);
canvas.drawRoundRect(new RectF(margin, margin, source.getWidth()
        - margin, source.getHeight() - margin), radius, radius, paint);

if (source != output) {
    source.recycle();
}
Paint paint1 = new Paint();      
paint1.setColor(Color.WHITE);
paint1.setStyle(Style.STROKE);
paint1.setAntiAlias(true);
paint1.setStrokeWidth(3);
canvas.drawCircle((source.getWidth() - margin)/2, (source.getHeight() - margin)/2, radius-2, paint);
return output;
}


public String key() {
return "rounded";
}
}

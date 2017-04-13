package atlant.moviesapp.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Korisnik on 11.04.2017..
 */

public class ImageRatio extends ImageView
{
    public ImageRatio(Context context)
    {
        super(context);
    }

    public ImageRatio(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ImageRatio(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getMeasuredWidth(), (int)(getMeasuredWidth() * 1.3214f));
    }
}
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by tom on 05/11/2017.
 */

public class customTextView extends android.support.v7.widget.AppCompatTextView{
    public customTextView(Context context) {
        super(context);
        setFont();
    }
    public customTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public customTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "Asgalt-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}


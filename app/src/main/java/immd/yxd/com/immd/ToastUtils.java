package immd.yxd.com.immd;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by a on 2016/5/13.
 */
public class ToastUtils {
    private static Toast toast;


    public static void showToast(Context context, final String msg){
        if(toast == null){
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }
}

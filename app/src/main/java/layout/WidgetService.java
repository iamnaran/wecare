package layout;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by NaRan on 7/14/17.
 */

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new WidgetRemoteViewsFactory(this.getApplicationContext(), intent));
    }
}

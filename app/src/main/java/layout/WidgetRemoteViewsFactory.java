package layout;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.naran.wecare.Models.Event;
import com.naran.wecare.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class WidgetRemoteViewsFactory implements RemoteViewsFactory {
    private Context context = null;
    private List<Event> eventList;

    private String[] mobileArray = {"1","2","3","4","5","6","7","8","9" };
    private List<String> widgetList = new ArrayList<>();

    WidgetRemoteViewsFactory(Context context, Event intent)
    {
        this.context = context;

    }

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
    }


    protected void updateWidgetListView()
    {
        List<String> convertedToList = new ArrayList<>(Arrays.asList(mobileArray));
        this.widgetList = convertedToList;
    }

    @Override
    public int getCount()
    {
        return widgetList.size();
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        Log.d("WidgetCreatingView", "WidgetCreatingView");
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.list_view_row);
        final Event event = eventList.get(position);

        remoteView.setTextViewText(R.id.label, event.getEvent_name());

        return remoteView;
    }

    @Override
    public int getViewTypeCount()
    {
        // TODO Auto-generated method stub
        return widgetList.size();
    }

    @Override
    public boolean hasStableIds()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        updateWidgetListView();
    }

    @Override
    public void onDataSetChanged()
    {
        // TODO Auto-generated method stub
        updateWidgetListView();
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        widgetList.clear();
    }
}

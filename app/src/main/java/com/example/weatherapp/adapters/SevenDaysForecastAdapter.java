package com.example.weatherapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.activity.MainActivity;
import com.example.weatherapp.data.WeatherContract.WeatherEntity;
import com.example.weatherapp.models.DailyWeather;
import com.example.weatherapp.utilities.SunshineDateUtils;

public class SevenDaysForecastAdapter extends RecyclerView.Adapter<SevenDaysForecastAdapter.ItemViewHolder> {
    private static final String TAG = "SevenDaysForecastAdapter";
    private final ItemClickHandler itemClickHandler;
    private final Context mContext;
    private Cursor mCursor;

    @SuppressLint("LongLogTag")
    public SevenDaysForecastAdapter(Context context, ItemClickHandler clickHandler) {
        mContext = context;
        itemClickHandler = clickHandler;
    }

    @SuppressLint("LongLogTag")
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @SuppressLint("LongLogTag")
    @Override
    public int getItemCount() {
        if (mCursor == null)
            return 0;
        return mCursor.getCount();
    }

    /**
     * Swaps the cursor used by the ForecastAdapter for its weather data. This method is called by
     * MainActivity after a load has finished, as well as when the Loader responsible for loading
     * the weather data is reset. When this method is called, we assume we have a completely new
     * set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param cursor the new cursor to use as ForecastAdapter's data source
     */

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public interface ItemClickHandler {
        void onClick(long date);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date;
        TextView tempMinMax;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.item_date);
            tempMinMax = itemView.findViewById(R.id.item_min_max_temp);

            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            mCursor.moveToPosition(position);

            long dateInMills = mCursor.getLong(MainActivity.POSITION_DATE);
            String dateString = SunshineDateUtils.getDateFromUTCTimestamp(dateInMills);

            date.setText(dateString);
            tempMinMax.setText(mCursor.getDouble(MainActivity.POSITION_TEMP_MAX) + ", " +
                    mCursor.getDouble(MainActivity.POSITION_TEMP_MIN));
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onClick(View v) {
            int adapterPosition = getAbsoluteAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long dateInMills = mCursor.getLong(MainActivity.POSITION_DATE);
            itemClickHandler.onClick(dateInMills);
        }
    }
}

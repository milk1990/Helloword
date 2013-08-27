package com.loveplusplus.zhengzhou.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

import com.loveplusplus.zhengzhou.R;
import com.loveplusplus.zhengzhou.provider.BusContract.BusLineStation;
import com.loveplusplus.zhengzhou.provider.BusContract.Favorite;
import com.loveplusplus.zhengzhou.ui.GpsWaitingActivity;

public class UpListFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {

	SimpleCursorAdapter mAdapter;


	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		Cursor cursor = (Cursor) mAdapter.getItem(position);

		String waitStation = cursor.getString(cursor.getColumnIndex(BusLineStation.STATION_NAME));
		String direct = cursor.getString(cursor.getColumnIndex(BusLineStation.DIRECT));
		String sno = cursor.getString(cursor.getColumnIndex(BusLineStation.SNO));
		String lineName = cursor
				.getString(cursor.getColumnIndex(BusLineStation.LINE_NAME));

		// 保存到数据库
		ContentValues values = new ContentValues();
		values.put(Favorite.BUS_NAME, lineName);
		values.put(Favorite.DIRECT, direct);
		values.put(Favorite.SNO, sno);
		values.put(Favorite.STATION_NAME, waitStation);
		getActivity().getContentResolver().insert(Favorite.CONTENT_URI, values);

		Intent intent = new Intent(getActivity(), GpsWaitingActivity.class);
		intent.putExtra("lineName", lineName);
		intent.putExtra("ud", direct);
		intent.putExtra("sno", sno);
		intent.putExtra("hczd", waitStation);
		startActivity(intent);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mAdapter = new SimpleCursorAdapter(activity,
				R.layout.activity_stations_item, null, new String[] {
				BusLineStation.SNO, BusLineStation.STATION_NAME }, new int[] { R.id.sno,
						R.id.station_name }, 0);
		setListAdapter(mAdapter);

		Loader<Cursor> loader = getLoaderManager().getLoader(0);

		if (null == loader) {
			getLoaderManager().initLoader(0, getArguments(), this);
		} else {
			getLoaderManager().restartLoader(0, getArguments(), this);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		Activity activity = getActivity();
		Intent intent = activity.getIntent();
		return new CursorLoader(activity, intent.getData(), null, "direct=?",
				new String[] { "0" }, "sno");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> data) {
		mAdapter.swapCursor(null);
	}

}
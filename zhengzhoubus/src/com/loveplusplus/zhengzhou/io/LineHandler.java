package com.loveplusplus.zhengzhou.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loveplusplus.zhengzhou.io.model.Line;
import com.loveplusplus.zhengzhou.provider.BusContract;
import com.loveplusplus.zhengzhou.util.Lists;

public class LineHandler extends JSONHandler {

	public LineHandler(Context context) {
		super(context);
	}

	@Override
	public ArrayList<ContentProviderOperation> parse(String json)
			throws IOException {
		final ArrayList<ContentProviderOperation> batch = Lists.newArrayList();

		List<Line> busLines = new Gson().fromJson(json,
				new TypeToken<List<Line>>() {
				}.getType());

		for (Line line : busLines) {
			parseBusLine(line, batch);
		}
		return batch;
	}

	private void parseBusLine(Line busLine,
			ArrayList<ContentProviderOperation> batch) {
		ContentProviderOperation.Builder builder = ContentProviderOperation
				.newInsert(BusContract.BusLine.CONTENT_URI);

		builder.withValue(BusContract.BusLine.CARFARE, busLine.carfare);
		builder.withValue(BusContract.BusLine.DEPT_NAME, busLine.deptName);
		builder.withValue(BusContract.BusLine.END_STATION, busLine.endStation);
		builder.withValue(BusContract.BusLine.FIRST_TIME, busLine.firstTime);
		builder.withValue(BusContract.BusLine.LINE_NAME, busLine.lineName);
		builder.withValue(BusContract.BusLine.START_STATION,
				busLine.startStation);

		batch.add(builder.build());
	}

}

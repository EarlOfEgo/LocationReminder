package com.htwgkonstanz.locationreminder;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class TasksOverlay extends ItemizedOverlay<OverlayItem> {
	private final ArrayList<OverlayItem> mOverlays;

	public TasksOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		mOverlays = new ArrayList<OverlayItem>();
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
}

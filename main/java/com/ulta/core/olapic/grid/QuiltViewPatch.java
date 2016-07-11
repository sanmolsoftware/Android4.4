package com.ulta.core.olapic.grid;

public class QuiltViewPatch implements Comparable {
	public int width_ratio;
	public int height_ratio;

	public QuiltViewPatch(int width_ratio, int height_ratio) {
		this.width_ratio = width_ratio;
		this.height_ratio = height_ratio;
	}

	private static QuiltViewPatch create(Size size) {
		switch (size) {
		case Big:
			return new QuiltViewPatch(2, 2);
		case Small:
			return new QuiltViewPatch(1, 1);
		case Tall:
			return new QuiltViewPatch(1, 2);
		}

		return new QuiltViewPatch(1, 1);
	}

	public int getHeightRatio() {
		return this.height_ratio;
	}

	public int getWidthRatio() {
		return this.width_ratio;
	}

//	public static QuiltViewPatch create(int view_count) {
//
//		if (view_count == 0)
//			return new QuiltViewPatch(2, 2);
//		else if ((view_count % 11) == 0)
//			return new QuiltViewPatch(2, 2);
//		else if ((view_count % 4) == 0)
//			return new QuiltViewPatch(1, 2);
//		else
//			return new QuiltViewPatch(1, 1);
//
//	}

	private enum Size {
		Big, Small, Tall
	}

	// public static QuiltViewPatch init9(int position, int column) {
	//
	// return create(Size.Small);
	// }

	public static QuiltViewPatch init(int position, int column) {
		return nextPatchFor3ColumnLayout(position);
	}

	private static QuiltViewPatch nextPatchFor3ColumnLayout(int position) {
		switch (position % 9) {
		case 0:
		case 1:
		case 2:
			return create(Size.Small);
		case 3:
			return create(Size.Big);
		case 4:
		case 5:
			return create(Size.Small);
		case 6:
			return create(Size.Small);
		case 7:
			return create(Size.Big);
		case 8:
			return create(Size.Small);
		}

		return create(Size.Small);
	}

	public static boolean getRandomBoolean() {
		return (Math.random() < 0.5);
	}

	public boolean equals(Object obj) {
		if (obj != null && obj instanceof QuiltViewPatch) {
			QuiltViewPatch size = (QuiltViewPatch) obj;
			return size.height_ratio == this.height_ratio && size.width_ratio == this.width_ratio;
		}

		return false;
	}

	public int hashCode() {
		return height_ratio + 100 * width_ratio;
	}

	public String toString() {
		return "Patch: " + height_ratio + " x " + width_ratio;
	}

	@Override
	public int compareTo(Object another) {
		if (another != null && another instanceof QuiltViewPatch) {
			QuiltViewPatch size = (QuiltViewPatch) another;
			if (size.equals(this))
				return 0;

			if (this.height_ratio < size.height_ratio)
				return -1;
			else if (this.height_ratio > size.height_ratio)
				return 1;

			if (this.width_ratio < size.width_ratio)
				return -1;
			else
				return 1;
		}
		return -1;
	}
}

package com.gregsprogrammingworks.timeclock.ui.main;

public class WorkShiftTimeSliceData {
    private static final String TAG = WorkShiftTimeSliceData.class.getSimpleName();

    final String sliceText;
    final String startText;
    final String endText;
    final String elapsedText;

    WorkShiftTimeSliceData(String sliceText, String startText, String endText, String elapsedText) {

        ValidParamsOrThrow(sliceText, startText, endText, elapsedText);

        this.sliceText = sliceText;
        this.startText = startText;
        this.endText = endText;
        this.elapsedText = elapsedText;
    }

    private static void ValidParamsOrThrow(String sliceText, String startText, String endText, String elapsedText) {
//        if (null != sliceText) {
//            return;
//        }
        if (null != startText && 0 < startText.length()) {
            return;
        }
        if (null != endText && 0 < endText.length()) {
            return;
        }
        if (null != elapsedText && 0 < elapsedText.length()) {
            return;
        }

        // If we're here there's at nothing to show
        throw new IllegalArgumentException(TAG + ": Nothing to show");
    }
}

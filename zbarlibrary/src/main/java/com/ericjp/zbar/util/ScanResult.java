package com.ericjp.zbar.util;

import android.graphics.PointF;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2019, by sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: ted
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2019-11-03 11:13
 */
public class ScanResult {
    public String result;
    public PointF[] resultPoints;

    public ScanResult(String result) {
        this.result = result;
    }

    public ScanResult(String result, PointF[] resultPoints) {
        this.result = result;
        this.resultPoints = resultPoints;
    }
}

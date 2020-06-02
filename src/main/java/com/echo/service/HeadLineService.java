package com.echo.service;

import com.echo.dataobject.HeadLineDO;

import java.util.List;

public interface HeadLineService {
    List<HeadLineDO> queryHeadLineList(Integer enableStatus);
}

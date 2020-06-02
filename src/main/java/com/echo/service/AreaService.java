package com.echo.service;

import com.echo.dataobject.AreaDO;

import java.util.List;

public interface AreaService {
    public List<AreaDO> queryArea();

    public String addArea(AreaDO areaDO);
}

package com.echo.service;

import com.echo.dataobject.OwnerDO;

public interface OwnerService {
    OwnerDO selectOwner(int userId);

}

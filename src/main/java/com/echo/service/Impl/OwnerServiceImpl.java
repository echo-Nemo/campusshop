package com.echo.service.Impl;

import com.echo.dao.OwnerDOMapper;
import com.echo.dataobject.OwnerDO;
import com.echo.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService {
    @Autowired
    OwnerDOMapper ownerDOMapper;

    @Override
    public OwnerDO selectOwner(int userId) {
        return ownerDOMapper.selectByPrimaryKey(userId);
    }
}
